package org.davincischools.leo.database.admin;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;

@SpringBootApplication(scanBasePackageClasses = Database.class)
public class AdminUtils {

  private static final Joiner ERROR_JOINER = Joiner.on("\n - ");

  private static final Logger log = LogManager.getLogger();

  private record Error(String value, Exception e) {}

  @Autowired ApplicationContext context;

  @Value("${createDistrict:}")
  String createDistrict;

  @Value("${createAdmin:}")
  String createAdmin;

  @Value("${importStudents:}")
  String importStudents;

  @Value("${delimiter:[\\t,]}")
  String delimiter;

  public void createDistrict() {
    Database db = context.getBean(Database.class);

    log.atInfo().log("Creating district: " + createDistrict);

    db.getDistrictRepository()
        .save(new District().setCreationTime(Instant.now()).setName(createDistrict));

    log.atInfo().log("Done.");
  }

  public void createAdmin() {
    Database db = context.getBean(Database.class);
    checkArgument(createDistrict != null, "--createDistrict required.");
    District district = db.getDistrictRepository().findByName(createDistrict);
    checkArgument(district != null, "District doesn't exist: {}", createDistrict);

    log.atInfo().log("Creating admin user: {}", createAdmin);

    String password =
        new RandomStringGenerator.Builder()
            .withinRange(new char[] {'a', 'z'}, new char[] {'0', '9'})
            .build()
            .generate(20);

    Optional<UserX> user = db.getUserXRepository().findFullUserXByEmailAddress(createAdmin);
    db.getUserXRepository()
        .save(
            UserUtils.setPassword(
                new UserX()
                    .setId(user.map(UserX::getId).orElse(null))
                    .setCreationTime(user.map(UserX::getCreationTime).orElse(Instant.now()))
                    .setAdminX(
                        db.getAdminXRepository().save(new AdminX().setCreationTime(Instant.now())))
                    .setDistrict(district)
                    .setAdminX(
                        db.getAdminXRepository().save(new AdminX().setCreationTime(Instant.now())))
                    .setFirstName("NEW ADMIN")
                    .setLastName("NEW ADMIN")
                    .setEmailAddress(createAdmin),
                password));

    log.atWarn()
        .log(
            "IMPORTANT! Log in and change the temporary password: login: \"{}\", temporary"
                + " password: \"{}\"",
            createAdmin,
            password);
    log.atInfo().log("Done.");
  }

  public void importStudents() throws IOException {
    Database db = context.getBean(Database.class);
    checkArgument(createDistrict != null, "--createDistrict required.");
    District district = db.getDistrictRepository().findByName(createDistrict);
    checkArgument(district != null, "District doesn't exist: {}", createDistrict);
    checkArgument(importStudents != null, "--importStudents required.");

    log.atInfo().log("Imported students: {}", importStudents);

    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(transactionManager.getEntityManagerFactory());

    List<Error> errors = Collections.synchronizedList(new ArrayList<>());

    List<UserX> userXs =
        Files.readLines(new File(importStudents), StandardCharsets.UTF_8).stream()
            .<UserX>map(
                line -> {
                  try {
                    String[] cells = line.split(delimiter);
                    if (cells.length != 5) {
                      throw new IllegalArgumentException("Unexpected number of columns.");
                    }

                    int id = Integer.parseInt(cells[0]);
                    int grade = Integer.parseInt(cells[1]);
                    String lastName = cells[2];
                    String firstName = cells[3];
                    String emailAddress = cells[4];

                    checkArgument(!lastName.isEmpty(), "Last name required.");
                    checkArgument(!firstName.isEmpty(), "First name required.");
                    checkArgument(!emailAddress.isEmpty(), "Email address required.");

                    UserX userX =
                        db.getUserXRepository()
                            .findFullUserXByEmailAddress(emailAddress)
                            .orElse(new UserX().setCreationTime(Instant.now()))
                            .setEmailAddress(emailAddress)
                            .setFirstName(firstName)
                            .setLastName(lastName)
                            .setDistrict(new District().setId(district.getId()));
                    userX.setStudent(
                        Optional.ofNullable(userX.getStudent())
                            .orElse(new Student().setCreationTime(Instant.now()))
                            .setStudentId(id)
                            .setGrade(grade));

                    log.atTrace().log("Imported: {}", line);

                    return userX;
                  } catch (Exception e) {
                    log.atError().withThrowable(e).log("Error: {}", line);
                    errors.add(new Error(line, e));
                    return null;
                  }
                })
            .filter(Objects::nonNull)
            .parallel()
            .map(
                userX -> {
                  return userX.getEncodedPassword() == null
                      ? UserUtils.setPassword(
                          userX, userX.getLastName() + userX.getStudent().getStudentId())
                      : userX;
                })
            .toList();

    db.getStudentRepository().saveAllAndFlush(Lists.transform(userXs, UserX::getStudent));
    db.getUserXRepository().saveAll(userXs);

    if (!errors.isEmpty()) {
      log.atError()
          .log(
              "There were errors during the import of the following students:\n - {}",
              ERROR_JOINER.join(Lists.transform(errors, e -> e.value + ": " + e.e.getMessage())));
    }

    log.atInfo().log("Done.");
  }

  @Transactional
  public void processCommands() throws IOException {
    if (!createDistrict.isEmpty()) {
      createDistrict();
    }
    if (!createAdmin.isEmpty()) {
      createAdmin();
    }
    if (!importStudents.isEmpty()) {
      importStudents();
    }
  }

  public static void main(String[] argsArray) throws IOException {
    try {
      ApplicationContext context = SpringApplication.run(AdminUtils.class, argsArray);
      context.getBean(AdminUtils.class).processCommands();
      System.exit(0);
    } catch (Exception e) {
      log.atError().withThrowable(e).log("Failed to execute commands.");
    }
  }
}
