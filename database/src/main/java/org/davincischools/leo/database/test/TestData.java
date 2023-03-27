package org.davincischools.leo.database.test;

import static org.davincischools.leo.database.utils.UserUtils.setPassword;

import com.google.common.collect.ImmutableSet;
import java.util.UUID;
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

  private final Database db;

  public final User mrsPuff;
  public final User spongeBob;
  public final User sandy;

  public final String password = UUID.randomUUID().toString();

  public TestData(@Autowired Database db) {
    this.db = db;

    db.getPortfolios().deleteAll();
    db.getProjectCycles().deleteAll();
    db.getProjectPosts().deleteAll();
    db.getProjects().deleteAll();
    db.getKnowledgeAndSkills().deleteAll();
    db.getAssignments().deleteAll();
    db.getUsers().deleteAll();
    db.getClasses().deleteAll();
    db.getAdmins().deleteAll();
    db.getTeachers().deleteAll();
    db.getStudents().deleteAll();
    db.getSchools().deleteAll();
    db.getDistricts().deleteAll();

    District district =
        db.getDistricts().save(new District().setDistrict("Bikini Bottom School District"));
    School school =
        db.getSchools()
            .save(
                new School()
                    .setSchool("Bikini Bottom Drivers School")
                    .setCity("Bikini Bottom")
                    .setDistrict(district));

    db.getUsers()
        .save(
            sandy =
                setPassword(
                    new User()
                        .setFirstName("Sandy")
                        .setLastName("Cheeks")
                        .setEmailAddress("sandy.cheeks@bikinibottom.com")
                        .setAdmin(db.getAdmins().save(new Admin().setDistrict(district))),
                    password));

    db.getUsers()
        .save(
            mrsPuff =
                setPassword(
                    new User()
                        .setFirstName("Poppy")
                        .setLastName("Puff")
                        .setEmailAddress("poppy.puff@bikinibottom.com")
                        .setTeacher(
                            db.getTeachers()
                                .save(new Teacher().setSchools(ImmutableSet.of(school)))),
                    password));

    db.getUsers()
        .save(
            spongeBob =
                setPassword(
                    new User()
                        .setFirstName("SpongeBob")
                        .setLastName("SquarePants")
                        .setEmailAddress("spongebob.squarepants@bikinibottom.com")
                        .setStudent(db.getStudents().save(new Student().setDistrict(district))),
                    password));
  }
}
