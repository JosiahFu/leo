package org.davincischools.leo.database.test;

import static org.davincischools.leo.database.utils.UserUtils.setPassword;

import com.google.common.collect.ImmutableSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.davincischools.leo.database.daos.Admin;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.utils.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestData {

  private static final AtomicInteger counter = new AtomicInteger(new Random().nextInt(Integer.MAX_VALUE - 1000));

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
        db.getDistrictRepository().save(new District().setName("Wiseburn Unified School District"));

    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Communications High School")
                .setCity("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Connect (TK-8)")
                .setCity("Hawthorne, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Connect High School")
                .setCity("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Extension")
                .setCity("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Rise High, RISE-Richstone")
                .setCity("Hawthorne, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Rise High, RISE-APCH")
                .setCity("Los Angeles, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Rise High, RISE-New Earth")
                .setCity("Culver City, CA")
                .setDistrict(district));
    School school =
    db.getSchoolRepository()
        .save(
            new School()
                .setName("Da Vinci Science High School")
                .setCity("El Segundo, CA")
                .setDistrict(district));

    db.getUserRepository()
        .save(
            admin =
                setPassword(
                    new User()
                        .setFirstName("Scott")
                        .setLastName("Hendrickson")
                        .setEmailAddress("sahendrickson." + counter.get() + "@davincischools.org")
                        .setDistrict(district)
                        .setAdmin(db.getAdminRepository().save(new Admin())),
                    password));

    db.getUserRepository()
        .save(
            teacher =
                setPassword(
                    new User()
                        .setFirstName("Steven")
                        .setLastName("Eno")
                        .setEmailAddress("seno." + counter.get() + "@davincischools.org")
                        .setDistrict(district)
                        .setTeacher(
                            db.getTeacherRepository()
                                .save(new Teacher().setSchools(ImmutableSet.of(school)))),
                    password));

    db.getUserRepository()
        .save(
            student =
                setPassword(
                    new User()
                        .setFirstName("Steve")
                        .setLastName("Wallis")
                        .setEmailAddress("swallis." + counter.get() + "@davincischools.org")
                        .setDistrict(district)
                        .setStudent(db.getStudentRepository().save(new Student())),
                    password));
  }
}
