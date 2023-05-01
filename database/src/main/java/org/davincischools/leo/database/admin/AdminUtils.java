package org.davincischools.leo.database.admin;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.test.TestData;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackageClasses = {Database.class, TestData.class})
public class AdminUtils {

  private static final Joiner ERROR_JOINER = Joiner.on("\n - ");

  private static final Logger log = LogManager.getLogger();

  private record Error(String value, Exception e) {}

  @Autowired ApplicationContext context;
  @Autowired Database db;

  @Value("${createDistrict:}")
  String createDistrict;

  @Value("${createAdmin:}")
  String createAdmin;

  @Value("${importStudents:}")
  String importStudents;

  @Value("${delimiter:[\\t,]}")
  String delimiter;

  public District createDistrict() {
    checkArgument(createDistrict != null, "--createDistrict required.");

    return db.createDistrict(createDistrict);
  }

  public UserX createAdmin() {
    checkArgument(createAdmin != null, "--createAdmin required.");

    District district = createDistrict();
    UserX admin = db.createUserX(district, createAdmin, userX -> db.addAdminXPermission(userX));

    String password =
        new RandomStringGenerator.Builder()
            .withinRange(new char[] {'a', 'z'}, new char[] {'0', '9'})
            .build()
            .generate(20);
    UserUtils.setPassword(admin, password);

    log.atWarn()
        .log(
            "IMPORTANT! Log in and change the temporary password:"
                + " login: \"{}\", temporary password: \"{}\"",
            createAdmin,
            password);

    return admin;
  }

  public void importStudents() throws IOException {
    checkArgument(importStudents != null, "--importStudents required.");

    District district = createDistrict();

    List<Error> errors = Collections.synchronizedList(new ArrayList<>());

    List<UserX> students =
        Files.readLines(new File(importStudents), StandardCharsets.UTF_8).stream()
            .map(
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

                    UserX student =
                        db.createUserX(
                            district,
                            emailAddress,
                            userX -> userX.setFirstName(firstName).setLastName(lastName));
                    db.addStudentPermission(
                        u -> u.getStudent().setStudentId(id).setGrade(grade), student);

                    return student;
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
                  if (userX.getEncodedPassword().equals("INVALID_ENCODED_PASSWORD")) {
                    UserUtils.setPassword(
                        userX, userX.getLastName() + userX.getStudent().getStudentId());
                  }
                  return userX;
                })
            .toList();

    db.getUserXRepository().saveAllAndFlush(students);

    if (!errors.isEmpty()) {
      log.atError()
          .log(
              "There were errors during the import of the following students:\n - {}",
              ERROR_JOINER.join(Lists.transform(errors, e -> e.value + ": " + e.e.getMessage())));
    }
  }

  @Transactional
  public void processCommands() throws IOException {
    if (!createDistrict.isEmpty()) {
      log.atInfo().log("Creating district: {}", createDistrict);
      createDistrict();
    }
    if (!importStudents.isEmpty()) {
      log.atInfo().log("Importing students: {}", importStudents);
      importStudents();
    }
    if (!createAdmin.isEmpty()) {
      log.atInfo().log("Creating admin: {}", createAdmin);
      createAdmin();
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
