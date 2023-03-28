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

    db.getPortfolioPostRepository().deleteAll();
    db.getProjectPostCommentRepository().deleteAll();
    db.getPortfolioRepository().deleteAll();
    db.getProjectPostRepository().deleteAll();
    db.getProjectCycleRepository().deleteAll();
    db.getKnowledgeAndSkillRepository().deleteAll();
    db.getProjectRepository().deleteAll();
    db.getClassTeacherRepository().deleteAll();
    db.getClassStudentRepository().deleteAll();
    db.getAssignmentRepository().deleteAll();
    db.getTeacherSchoolRepository().deleteAll();
    db.getUserRepository().deleteAll();
    db.getClassRepository().deleteAll();
    db.getTeacherRepository().deleteAll();
    db.getStudentRepository().deleteAll();
    db.getAdminRepository().deleteAll();
    db.getSchoolRepository().deleteAll();
    db.getDistrictRepository().deleteAll();

    District district =
        db.getDistrictRepository()
            .save(new District().setDistrict("Bikini Bottom School District"));
    School school =
        db.getSchoolRepository()
            .save(
                new School()
                    .setSchool("Bikini Bottom Drivers School")
                    .setCity("Bikini Bottom")
                    .setDistrict(district));

    db.getUserRepository()
        .save(
            sandy =
                setPassword(
                    new User()
                        .setFirstName("Sandy")
                        .setLastName("Cheeks")
                        .setEmailAddress("sandy.cheeks@bikinibottom.com")
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
                        .setEmailAddress("poppy.puff@bikinibottom.com")
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
                        .setEmailAddress("spongebob.squarepants@bikinibottom.com")
                        .setDistrict(district)
                        .setStudent(db.getStudentRepository().save(new Student())),
                    password));
  }
}
