package org.davincischools.leo.database.admin;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
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
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.School;
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

  @Value("${importTeachers:}")
  String importTeachers;

  @Value("${importStudents:}")
  String importStudents;

  @Value("${importEks:}")
  String importEks;

  @Value("${delimiter:[\\t,]}")
  String delimiter;

  public District createDistrict() {
    checkArgument(createDistrict != null, "--createDistrict required.");

    return db.createDistrict(createDistrict);
  }

  public UserX createAdmin() {
    checkArgument(createAdmin != null, "--createAdmin required.");

    String password =
        new RandomStringGenerator.Builder()
            .withinRange(new char[] {'a', 'z'}, new char[] {'0', '9'})
            .build()
            .generate(20);

    District district = createDistrict();
    UserX admin =
        db.createUserX(district, createAdmin, userX -> UserUtils.setPassword(userX, password));
    db.addAdminXPermission(admin);

    log.atWarn()
        .log(
            "IMPORTANT! Log in and change the temporary password:"
                + " login: \"{}\", temporary password: \"{}\"",
            createAdmin,
            password);

    return admin;
  }

  public void importTeachers() throws IOException {
    checkArgument(importTeachers != null, "--importTeachers required.");

    District district = createDistrict();

    List<Error> errors = Collections.synchronizedList(new ArrayList<>());

    List<UserX> userXs =
        Files.readLines(new File(importTeachers), StandardCharsets.UTF_8).stream()
            .map(
                line -> {
                  try {
                    String[] cells = line.split(delimiter);
                    if (cells.length < 4) {
                      throw new IllegalArgumentException("Unexpected number of columns.");
                    }

                    String firstName = cells[0];
                    String lastName = cells[1];
                    String emailAddress = cells[2];
                    String schoolNickname = cells[3];

                    checkArgument(!firstName.isEmpty(), "First name required.");
                    checkArgument(!lastName.isEmpty(), "Last name required.");
                    checkArgument(!emailAddress.isEmpty(), "Email address required.");
                    checkArgument(!schoolNickname.isEmpty(), "School nickname required.");
                    checkArgument(emailAddress.contains("@"), "Invalid e-mail address.");

                    UserX teacher =
                        db.createUserX(
                            district,
                            emailAddress,
                            userX -> userX.setFirstName(firstName).setLastName(lastName));
                    db.addTeacherPermission(teacher);

                    School school = db.createSchool(district, schoolNickname);
                    db.addTeachersToSchool(school, teacher.getTeacher());

                    if (cells.length >= 5) {
                      String className = cells[4];
                      checkArgument(!className.isEmpty(), "Class name required.");

                      ClassX classX = db.createClassX(school, className, c -> {});
                      db.addTeachersToClassX(classX, teacher.getTeacher());

                      for (int eksIndex : new int[] {5, 7}) {
                        if (cells.length >= eksIndex + 2) {
                          String eksName = cells[eksIndex];
                          String eksDescr = cells[eksIndex + 1];

                          checkArgument(!eksName.isEmpty(), "EKS name required.");
                          checkArgument(!eksDescr.isEmpty(), "EKS description required.");

                          db.createAssignment(
                              classX,
                              "Assignment: " + eksName,
                              db.createKnowledgeAndSkill(classX, eksName, eksDescr));
                        }
                      }
                    }

                    db.addTeachersToSchool(school, teacher.getTeacher());

                    log.atInfo().log("Imported: {}", line);

                    return teacher;
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
                  if (userX.getEncodedPassword().equals(Database.INVALID_ENCODED_PASSWORD)) {
                    UserUtils.setPassword(userX, userX.getLastName());
                  }
                  return userX;
                })
            .toList();

    db.getUserXRepository().saveAllAndFlush(userXs);

    if (!errors.isEmpty()) {
      log.atError()
          .log(
              "There were errors during the import of the following teachers:\n - {}\n",
              ERROR_JOINER.join(
                  Lists.transform(errors, e -> e.value + "  --  " + e.e.getMessage())));
    }

    log.atInfo().log("Done.");
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
                    if (cells.length < 6) {
                      throw new IllegalArgumentException("Unexpected number of columns.");
                    }

                    int id = Integer.parseInt(cells[0]);
                    int grade = Integer.parseInt(cells[1]);
                    String lastName = cells[2];
                    String firstName = cells[3];
                    String emailAddress = cells[4];
                    String schoolNickname = cells[5];

                    checkArgument(!lastName.isEmpty(), "Last name required.");
                    checkArgument(!firstName.isEmpty(), "First name required.");
                    checkArgument(!emailAddress.isEmpty(), "Email address required.");
                    checkArgument(!schoolNickname.isEmpty(), "School nickname required.");

                    UserX student =
                        db.createUserX(
                            district,
                            emailAddress,
                            userX -> userX.setFirstName(firstName).setLastName(lastName));
                    db.addStudentPermission(
                        u -> u.getStudent().setStudentId(id).setGrade(grade), student);

                    School school =
                        db.getSchoolRepository()
                            .findByNickname(district.getId(), schoolNickname)
                            .orElseThrow();
                    db.addStudentsToSchool(
                        db.getSchoolRepository()
                            .findByNickname(district.getId(), schoolNickname)
                            .orElseThrow(),
                        student.getStudent());
                    db.getClassXRepository()
                        .findAllBySchool(school)
                        .forEach(
                            classX -> {
                              db.addStudentsToClassX(classX, student.getStudent());
                            });

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
                  if (userX.getEncodedPassword().equals(Database.INVALID_ENCODED_PASSWORD)) {
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
              "There were errors during the import of the following students:\n - {}\n",
              ERROR_JOINER.join(
                  Iterables.transform(errors, e -> e.value + "  --  " + e.e.getMessage())));
    }
  }

  public void processCommands() throws IOException {
    if (!createDistrict.isEmpty()) {
      log.atInfo().log("Creating district: {}", createDistrict);
      District district = createDistrict();

      // For now, just create one of each school.
      db.createSchool(district, "DVC");
      db.createSchool(district, "DVConnect");
      db.createSchool(district, "DVD");
      db.createSchool(district, "DVFlex");
      db.createSchool(district, "DVRise");
      db.createSchool(district, "DVS");
    }

    if (!importTeachers.isEmpty()) {
      log.atInfo().log("Importing teachers: {}", importStudents);
      importTeachers();
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
