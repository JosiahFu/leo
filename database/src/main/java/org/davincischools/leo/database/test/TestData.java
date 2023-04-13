package org.davincischools.leo.database.test;

import static org.davincischools.leo.database.utils.UserUtils.setPassword;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.davincischools.leo.database.daos.Admin;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.Class;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.KnowledgeAndSkillAssignment;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.Database.KnowledgeAndSkillAssignmentRepository;
import org.davincischools.leo.database.utils.Database.StudentClassRepository;
import org.davincischools.leo.database.utils.Database.TeacherClassRepository;
import org.davincischools.leo.database.utils.Database.TeacherSchoolRepository;
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
  public final Class clazz;
  public final KnowledgeAndSkill knowledgeAndSkill1, knowledgeAndSkill2;
  public final Assignment assignment;
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

    admin =
        createUser(
            db,
            setPassword(
                new User()
                    .setFirstName("Scott")
                    .setLastName("Hendrickson")
                    .setEmailAddress("sahendrickson@gmail.com")
                    .setDistrict(district),
                password));
    addAdminPermission(db, admin);

    teacher =
        createUser(
            db,
            setPassword(
                new User()
                    .setFirstName("Steven")
                    .setLastName("Eno")
                    .setEmailAddress("seno@davincischools.org")
                    .setDistrict(district),
                password));
    addAdminPermission(db, teacher);
    addTeacherPermission(db, teacher);
    addTeacherToSchool(db.getTeacherSchoolRepository(), teacher.getTeacher(), school);

    student =
        createUser(
            db,
            setPassword(
                new User()
                    .setFirstName("Steve")
                    .setLastName("Wallis")
                    .setEmailAddress("swallis@davincischools.org")
                    .setDistrict(district),
                password));
    addStudentPermissions(db, student);

    clazz =
        db.getClassRepository()
            .save(
                new Class()
                    .setName("AP Chemistry AB")
                    .setShortDescr("Intro to general chemistry.")
                    .setLongDescr("A general chemistry class.")
                    .setSchool(school));
    addTeacherToClass(db.getTeacherClassRepository(), teacher, clazz);
    addStudentToClass(db.getStudentClassRepository(), student, clazz);
    knowledgeAndSkill1 =
        addKnowledgeAndSkill(
            db, clazz, "Periodic Table", "I can recognize the basic elements on a periodic table.");
    knowledgeAndSkill2 =
        addKnowledgeAndSkill(
            db,
            clazz,
            "Valence Electrons",
            "I can determine the number of valence electrons for each element.");
    assignment =
        createAssignment(
            db,
            clazz,
            "Valence Electrons",
            "Show that you understand valence electrons.",
            knowledgeAndSkill1);
  }

  private Assignment createAssignment(
      Database db,
      Class clazz,
      String name,
      String descr,
      KnowledgeAndSkill... knowledgeAndSkills) {
    Assignment assignment =
        db.getAssignmentRepository()
            .save(
                new Assignment()
                    .setClassField(clazz)
                    .setName(name)
                    .setShortDescr(descr)
                    .setLongDescr(descr));
    for (var knowledgeAndSkill : knowledgeAndSkills) {
      addKnowledgeAndSkillToAssignment(
          db.getKnowledgeAndSkillAssignmentRepository(), knowledgeAndSkill, assignment);
    }
    return assignment;
  }

  public KnowledgeAndSkillAssignment addKnowledgeAndSkillToAssignment(
      KnowledgeAndSkillAssignmentRepository repo,
      KnowledgeAndSkill knowledgeAndSkill,
      Assignment assignment) {
    return repo.save(repo.createKnowledgeAndSkillAssignment(knowledgeAndSkill, assignment));
  }

  public KnowledgeAndSkill addKnowledgeAndSkill(
      Database db, Class clazz, String name, String descr) {
    return db.getKnowledgeAndSkillRepository()
        .save(
            new KnowledgeAndSkill()
                .setClassField(clazz)
                .setName(name)
                .setShortDescr(descr)
                .setLongDescr(descr));
  }

  public void addTeacherToClass(TeacherClassRepository repo, User teacher, Class clazz) {
    repo.save(repo.createTeacherClass(teacher.getTeacher(), clazz));
  }

  public void addStudentToClass(StudentClassRepository repo, User student, Class clazz) {
    repo.save(repo.createStudentClass(student.getStudent(), clazz));
  }

  public static User createUser(Database db, User template) {
    return db.getUserRepository()
        .findFullUserByEmailAddress(template.getEmailAddress())
        .or(
            () -> {
              db.getUserRepository().save(template);
              return db.getUserRepository().findFullUserByEmailAddress(template.getEmailAddress());
            })
        .orElseThrow();
  }

  public static void addAdminPermission(Database db, User user) {
    if (user.getAdmin() == null) {
      user.setAdmin(db.getAdminRepository().save(new Admin()));
      db.getUserRepository().save(user);
    }
  }

  public static void addTeacherPermission(Database db, User teacher) {
    if (teacher.getTeacher() == null) {
      teacher.setTeacher(db.getTeacherRepository().save(new Teacher()));
      db.getUserRepository().save(teacher);
    }
  }

  public static void addTeacherToSchool(
      TeacherSchoolRepository repo, Teacher teacher, School school) {
    if (repo.findById(repo.createTeacherSchoolId(teacher, school)).isEmpty()) {
      repo.save(repo.createTeacherSchool(teacher, school));
    }
  }

  public static void addStudentPermissions(Database db, User student) {
    if (student.getTeacher() == null) {
      student.setStudent(db.getStudentRepository().save(new Student()));
      db.getUserRepository().save(student);
    }
  }
}
