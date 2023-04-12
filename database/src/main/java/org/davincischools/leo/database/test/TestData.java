package org.davincischools.leo.database.test;

import static org.davincischools.leo.database.utils.UserUtils.setPassword;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.davincischools.leo.database.daos.Admin;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherSchool;
import org.davincischools.leo.database.daos.TeacherSchoolId;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.utils.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestData {

  private static final AtomicInteger counter =
      new AtomicInteger(new Random().nextInt(Integer.MAX_VALUE - 1000));

  private final Database db;

  public final User teacher;
  public final User student;
  public final User admin;
  public final String password = "password";

  public TestData(@Autowired Database db) {
    this.db = db;
    counter.incrementAndGet();

    // Rather than delete what's there, which could be dangerous since it is
    // possible that a misconfiguration could point this to a real database,
    // we create new users each time with unique ids.

    District district =
        db.getDistrictRepository()
            .save(new District().setName("Wiseburn Unified School District " + counter.get()));

    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Communications High School " + counter.get())
                .setCity("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Connect (TK-8) " + counter.get())
                .setCity("Hawthorne, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Connect High School " + counter.get())
                .setCity("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Extension " + counter.get())
                .setCity("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Rise High, RISE-Richstone " + counter.get())
                .setCity("Hawthorne, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Rise High, RISE-APCH " + counter.get())
                .setCity("Los Angeles, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Rise High, RISE-New Earth " + counter.get())
                .setCity("Culver City, CA")
                .setDistrict(district));
    School school =
        db.getSchoolRepository()
            .save(
                new School()
                    .setName("Da Vinci Science High School " + counter.get())
                    .setCity("El Segundo, CA")
                    .setDistrict(district));

    admin = createUser(setPassword(
        new User()
            .setFirstName("Scott " + counter.get())
            .setLastName("Hendrickson")
            .setEmailAddress("sahendrickson@gmail.com")
            .setDistrict(district),
        password));
    if (admin.getAdmin() == null) {
      admin.setAdmin(db.getAdminRepository().save(new Admin()));
      db.getUserRepository().save(admin);
    }

    teacher = createUser(
        setPassword(
            new User()
                .setFirstName("Steven")
                .setLastName("Eno")
                .setEmailAddress("seno@davincischools.org")
                .setDistrict(district),
            password));
    if (teacher.getTeacher() == null) {
      teacher.setTeacher(db.getTeacherRepository().save(new Teacher()));
      db.getUserRepository().save(teacher);
    }

    student = createUser(setPassword(
        new User()
            .setFirstName("Steve")
            .setLastName("Wallis")
            .setEmailAddress("swallis@davincischools.org")
            .setDistrict(district),
        password));
    if (student.getTeacher() == null) {
      student.setStudent(db.getStudentRepository().save(new Student()));
      db.getUserRepository().save(student);
    }
  }

  private User createUser(User template) {
    return db.getUserRepository().findFullUserByEmailAddress(template.getEmailAddress())
        .or(() -> {
          db.getUserRepository().save(template);
          return db.getUserRepository().findFullUserByEmailAddress(template.getEmailAddress());
        }).orElseThrow();
  }
}
