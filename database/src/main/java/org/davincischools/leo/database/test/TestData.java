package org.davincischools.leo.database.test;

import static org.davincischools.leo.database.utils.UserUtils.setPassword;

import com.google.common.collect.ImmutableSet;
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

  private static final AtomicInteger counter = new AtomicInteger(0);

  private final Database db;

  public final User mrsPuff;
  public final User spongeBob;
  public final User sandy;
  public final String password = "password_" + counter.incrementAndGet();

  public TestData(@Autowired Database db) {
    this.db = db;

    // Rather than delete what's there, which could be dangerous since it is
    // possible that a misconfiguration could point this to a real database,
    // we create new users each time with unique ids.

    District district =
        db.getDistrictRepository().save(new District().setName("Bikini Bottom School District"));

    School school =
        db.getSchoolRepository()
            .save(
                new School()
                    .setName("Bikini Bottom Drivers School")
                    .setCity("Bikini Bottom")
                    .setDistrict(district));

    db.getUserRepository()
        .save(
            sandy =
                setPassword(
                    new User()
                        .setFirstName("Sandy")
                        .setLastName("Cheeks")
                        .setEmailAddress("sandy.cheeks." + password + "@bikinibottom.com")
                        .setDistrict(district)
                        .setAdmin(db.getAdminRepository().save(new Admin())),
                    password));

    db.getUserRepository()
        .save(
            mrsPuff =
                setPassword(
                    new User()
                        .setFirstName("Poppy")
                        .setLastName("Puff")
                        .setEmailAddress("poppy.puff." + password + "@bikinibottom.com")
                        .setDistrict(district)
                        .setTeacher(
                            db.getTeacherRepository()
                                .save(new Teacher().setSchools(ImmutableSet.of(school)))),
                    password));

    db.getUserRepository()
        .save(
            spongeBob =
                setPassword(
                    new User()
                        .setFirstName("SpongeBob")
                        .setLastName("SquarePants")
                        .setEmailAddress("spongebob.squarepants." + password + "@bikinibottom.com")
                        .setDistrict(district)
                        .setStudent(db.getStudentRepository().save(new Student())),
                    password));
  }
}
