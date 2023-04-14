package org.davincischools.leo.database.test;

import static org.davincischools.leo.database.utils.UserUtils.setPassword;

import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.davincischools.leo.database.daos.Admin;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.Class;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.utils.Database;
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

  public final Class chemistry_clazz;
  public final KnowledgeAndSkill chemistry_eks_1, chemistry_eks_2;
  public final Assignment chemistry_assignment;

  public final Class programming_clazz;
  public final KnowledgeAndSkill programming_eks_1, programming_eks_2;
  public final Assignment programming_assignment;

  public final String password = "password";

  public TestData(@Autowired Database db) {
    this.db = db;
    counter.incrementAndGet();

    // Rather than delete what's there, which could be dangerous since it is
    // possible that a misconfiguration could point this to a real database,
    // we create new users each time with unique ids.

    District district =
        db.getDistrictRepository()
            .save(
                new District()
                    .setCreationTime(Instant.now())
                    .setName("Wiseburn Unified School District " + counter.get()));

    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Communications High School " + counter.get())
                .setCity("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Connect (TK-8) " + counter.get())
                .setCity("Hawthorne, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Connect High School " + counter.get())
                .setCity("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Extension " + counter.get())
                .setCity("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Rise High, RISE-Richstone " + counter.get())
                .setCity("Hawthorne, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Rise High, RISE-APCH " + counter.get())
                .setCity("Los Angeles, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Rise High, RISE-New Earth " + counter.get())
                .setCity("Culver City, CA")
                .setDistrict(district));
    School school =
        db.getSchoolRepository()
            .save(
                new School()
                    .setCreationTime(Instant.now())
                    .setName("Da Vinci Science High School " + counter.get())
                    .setCity("El Segundo, CA")
                    .setDistrict(district));

    admin =
        createUser(
            db,
            setPassword(
                new User()
                    .setCreationTime(Instant.now())
                    .setFirstName("Scott")
                    .setLastName("Hendrickson")
                    .setEmailAddress("sahendrickson@gmail.com")
                    .setDistrict(district),
                password));

    teacher =
        createUser(
            db,
            setPassword(
                new User()
                    .setCreationTime(Instant.now())
                    .setFirstName("Steven")
                    .setLastName("Eno")
                    .setEmailAddress("seno@davincischools.org")
                    .setDistrict(district),
                password));

    student =
        createUser(
            db,
            setPassword(
                new User()
                    .setCreationTime(Instant.now())
                    .setFirstName("Steve")
                    .setLastName("Wallis")
                    .setEmailAddress("swallis@davincischools.org")
                    .setDistrict(district),
                password));

    addAdminPermission(db, admin);
    addTeacherPermission(db, teacher);
    addStudentPermission(db, admin, teacher, student);
    addTeachersToSchool(db.getTeacherSchoolRepository(), school, teacher);

    chemistry_clazz = createClass(db, school, "Chemistry I", "Intro to general chemistry.");
    addTeachersToClass(db.getTeacherClassRepository(), chemistry_clazz, teacher);
    addStudentsToClass(db.getStudentClassRepository(), chemistry_clazz, admin, teacher, student);

    chemistry_eks_1 =
        createKnowledgeAndSkill(
            db,
            chemistry_clazz,
            "Periodic Table",
            "I can recognize the basic elements on a periodic table.");
    chemistry_eks_2 =
        createKnowledgeAndSkill(
            db,
            chemistry_clazz,
            "Valence Electrons",
            "I can determine the number of valence electrons for each element.");

    chemistry_assignment =
        createAssignment(
            db,
            chemistry_clazz,
            "Valence Electrons",
            "Show that you understand valence electrons.",
            chemistry_eks_1,
            chemistry_eks_2);

    programming_clazz = createClass(db, school, "Computer Science I", "Intro to Programming.");
    addTeachersToClass(db.getTeacherClassRepository(), programming_clazz, teacher);
    addStudentsToClass(db.getStudentClassRepository(), programming_clazz, admin, teacher, student);

    programming_eks_1 =
        createKnowledgeAndSkill(
            db,
            programming_clazz,
            "Sort Functions",
            "I understand and can implement different sort functions.");
    programming_eks_2 =
        createKnowledgeAndSkill(
            db, programming_clazz, "Collections", "I can use Lists, Sets, and Maps.");

    programming_assignment =
        createAssignment(
            db,
            programming_clazz,
            "Sort Algorithms",
            "Show that you can implement sort algorithms.",
            programming_eks_1);
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

  public static void addAdminPermission(Database db, User... users) {
    for (var user : users) {
      if (user.getAdmin() == null) {
        user.setAdmin(db.getAdminRepository().save(new Admin().setCreationTime(Instant.now())));
        db.getUserRepository().save(user);
      }
    }
  }

  public static void addTeacherPermission(Database db, User... teachers) {
    for (var teacher : teachers) {
      if (teacher.getTeacher() == null) {
        teacher.setTeacher(
            db.getTeacherRepository().save(new Teacher().setCreationTime(Instant.now())));
        db.getUserRepository().save(teacher);
      }
    }
  }

  public static void addStudentPermission(Database db, User... students) {
    for (var student : students) {
      if (student.getStudent() == null) {
        student.setStudent(
            db.getStudentRepository().save(new Student().setCreationTime(Instant.now())));
        db.getUserRepository().save(student);
      }
    }
  }

  public static void addTeachersToSchool(
      TeacherSchoolRepository repo, School school, User... teachers) {
    Arrays.asList(teachers)
        .forEach(teacher -> repo.save(repo.createTeacherSchool(teacher.getTeacher(), school)));
  }

  public void addTeachersToClass(TeacherClassRepository repo, Class clazz, User... teachers) {
    Arrays.asList(teachers)
        .forEach(teacher -> repo.save(repo.createTeacherClass(teacher.getTeacher(), clazz)));
  }

  public void addStudentsToClass(StudentClassRepository repo, Class clazz, User... students) {
    Arrays.asList(students)
        .forEach(student -> repo.save(repo.createStudentClass(student.getStudent(), clazz)));
  }

  private Class createClass(Database db, School school, String name, String descr) {
    Class clazz =
        db.getClassRepository()
            .save(
                new Class()
                    .setCreationTime(Instant.now())
                    .setSchool(school)
                    .setName(name)
                    .setShortDescr(descr)
                    .setLongDescr(descr));
    return clazz;
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
                    .setCreationTime(Instant.now())
                    .setClassField(clazz)
                    .setName(name)
                    .setShortDescr(descr)
                    .setLongDescr(descr));

    var ksaRepo = db.getKnowledgeAndSkillAssignmentRepository();
    Arrays.asList(knowledgeAndSkills)
        .forEach(ks -> ksaRepo.save(ksaRepo.createKnowledgeAndSkillAssignment(ks, assignment)));
    return assignment;
  }

  public KnowledgeAndSkill createKnowledgeAndSkill(
      Database db, Class clazz, String name, String descr) {
    return db.getKnowledgeAndSkillRepository()
        .save(
            new KnowledgeAndSkill()
                .setCreationTime(Instant.now())
                .setClassField(clazz)
                .setName(name)
                .setShortDescr(descr)
                .setLongDescr(descr));
  }
}
