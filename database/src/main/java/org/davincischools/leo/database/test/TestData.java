package org.davincischools.leo.database.test;

import static org.davincischools.leo.database.utils.UserUtils.setPassword;

import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.Database.StudentClassXRepository;
import org.davincischools.leo.database.utils.Database.TeacherClassXRepository;
import org.davincischools.leo.database.utils.Database.TeacherSchoolRepository;
import org.davincischools.leo.database.utils.QuillInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestData {

  public static final String PASSWORD = "password";
  // This is random in order to avoid collisions with other test instances.
  public static final AtomicInteger counter =
      new AtomicInteger(new Random().nextInt(Integer.MAX_VALUE - 1000));

  private final Database db;

  private UserX teacher;
  private UserX student;
  private UserX admin;

  private ClassX chemistryClassX;
  private KnowledgeAndSkill chemistryEks1, chemistryEks2;
  private Assignment chemistryAssignment;

  private ClassX programmingClassX;
  private KnowledgeAndSkill programmingEks1, programmingEks2;
  private Assignment programmingAssignment;

  public TestData(@Autowired Database db) {
    this.db = db;
  }

  public UserX getTeacher() {
    return teacher;
  }

  public UserX getStudent() {
    return student;
  }

  public UserX getAdmin() {
    return admin;
  }

  public ClassX getChemistryClassX() {
    return chemistryClassX;
  }

  public KnowledgeAndSkill getChemistryEks1() {
    return chemistryEks1;
  }

  public KnowledgeAndSkill getChemistryEks2() {
    return chemistryEks2;
  }

  public Assignment getChemistryAssignment() {
    return chemistryAssignment;
  }

  public ClassX getProgrammingClassX() {
    return programmingClassX;
  }

  public KnowledgeAndSkill getProgrammingEks1() {
    return programmingEks1;
  }

  public KnowledgeAndSkill getProgrammingEks2() {
    return programmingEks2;
  }

  public Assignment getProgrammingAssignment() {
    return programmingAssignment;
  }

  public void addTestData(int count) {
    // Rather than delete what's there, which could be dangerous since it is
    // possible that a misconfiguration could point this to a real database,
    // we create new users each time with unique ids.

    District district =
        db.getDistrictRepository()
            .save(
                new District()
                    .setCreationTime(Instant.now())
                    .setName("Wiseburn Unified School District " + count));

    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Communications High School " + count)
                .setAddress("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Connect (TK-8) " + count)
                .setAddress("Hawthorne, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Connect High School " + count)
                .setAddress("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Extension " + count)
                .setAddress("El Segundo, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Rise High, RISE-Richstone " + count)
                .setAddress("Hawthorne, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Rise High, RISE-APCH " + count)
                .setAddress("Los Angeles, CA")
                .setDistrict(district));
    db.getSchoolRepository()
        .save(
            new School()
                .setCreationTime(Instant.now())
                .setName("Da Vinci Rise High, RISE-New Earth " + count)
                .setAddress("Culver City, CA")
                .setDistrict(district));
    School school =
        db.getSchoolRepository()
            .save(
                new School()
                    .setCreationTime(Instant.now())
                    .setName("Da Vinci Science High School " + count)
                    .setAddress("El Segundo, CA")
                    .setDistrict(district));

    admin =
        createUser(
            db,
            setPassword(
                new UserX()
                    .setCreationTime(Instant.now())
                    .setFirstName("Scott")
                    .setLastName("Hendrickson")
                    .setEmailAddress("sahendrickson-" + count + "@gmail.com")
                    .setDistrict(district),
                PASSWORD));

    teacher =
        createUser(
            db,
            setPassword(
                new UserX()
                    .setCreationTime(Instant.now())
                    .setFirstName("Steven")
                    .setLastName("Eno")
                    .setEmailAddress("seno-" + count + "@davincischools.org")
                    .setDistrict(district),
                PASSWORD));

    student =
        createUser(
            db,
            setPassword(
                new UserX()
                    .setCreationTime(Instant.now())
                    .setFirstName("Steve")
                    .setLastName("Wallis")
                    .setEmailAddress("swallis-" + count + "@davincischools.org")
                    .setDistrict(district),
                PASSWORD));

    addAdminPermission(db, admin);
    addTeacherPermission(db, teacher);
    addStudentPermission(db, admin, teacher, student);
    addTeachersToSchool(db.getTeacherSchoolRepository(), school, teacher);

    chemistryClassX = createClassX(db, school, "Intro to general chemistry.");
    addTeachersToClassX(db.getTeacherClassXRepository(), chemistryClassX, teacher);
    addStudentsToClassX(db.getStudentClassXRepository(), chemistryClassX, admin, teacher, student);

    chemistryEks1 =
        createKnowledgeAndSkill(
            db, chemistryClassX, "I can recognize the basic elements on a periodic table.");
    chemistryEks2 =
        createKnowledgeAndSkill(
            db,
            chemistryClassX,
            "I can determine the number of valence electrons for each element.");

    chemistryAssignment =
        createAssignment(
            db,
            chemistryClassX,
            "Show that you understand valence electrons.",
            chemistryEks1,
            chemistryEks2);

    programmingClassX = createClassX(db, school, "Intro to Programming.");
    addTeachersToClassX(db.getTeacherClassXRepository(), programmingClassX, teacher);
    addStudentsToClassX(
        db.getStudentClassXRepository(), programmingClassX, admin, teacher, student);

    programmingEks1 =
        createKnowledgeAndSkill(
            db, programmingClassX, "I understand and can implement different sort functions.");
    programmingEks2 =
        createKnowledgeAndSkill(db, programmingClassX, "I can use Lists, Sets, and Maps.");

    programmingAssignment =
        createAssignment(
            db, programmingClassX, "Show that you can implement sort algorithms.", programmingEks1);
  }

  public static UserX createUser(Database db, UserX template) {
    return db.getUserXRepository()
        .findFullUserXByEmailAddress(template.getEmailAddress())
        .or(
            () -> {
              db.getUserXRepository().save(template);
              return db.getUserXRepository()
                  .findFullUserXByEmailAddress(template.getEmailAddress());
            })
        .orElseThrow();
  }

  public static void addAdminPermission(Database db, UserX... users) {
    for (var user : users) {
      if (user.getAdminX() == null) {
        user.setAdminX(db.getAdminXRepository().save(new AdminX().setCreationTime(Instant.now())));
        db.getUserXRepository().save(user);
      }
    }
  }

  public static void addTeacherPermission(Database db, UserX... teachers) {
    for (var teacher : teachers) {
      if (teacher.getTeacher() == null) {
        teacher.setTeacher(
            db.getTeacherRepository().save(new Teacher().setCreationTime(Instant.now())));
        db.getUserXRepository().save(teacher);
      }
    }
  }

  public static void addStudentPermission(Database db, UserX... students) {
    for (var student : students) {
      if (student.getStudent() == null) {
        student.setStudent(
            db.getStudentRepository()
                .save(
                    new Student()
                        .setCreationTime(Instant.now())
                        .setGrade(9)
                        .setStudentId(student.getId())));
        db.getUserXRepository().save(student);
      }
    }
  }

  public static void addTeachersToSchool(
      TeacherSchoolRepository repo, School school, UserX... teachers) {
    Arrays.asList(teachers)
        .forEach(teacher -> repo.save(repo.createTeacherSchool(teacher.getTeacher(), school)));
  }

  public void addTeachersToClassX(TeacherClassXRepository repo, ClassX classX, UserX... teachers) {
    Arrays.asList(teachers)
        .forEach(teacher -> repo.save(repo.createTeacherClassX(teacher.getTeacher(), classX)));
  }

  public void addStudentsToClassX(StudentClassXRepository repo, ClassX classX, UserX... students) {
    Arrays.asList(students)
        .forEach(student -> repo.save(repo.createStudentClassX(student.getStudent(), classX)));
  }

  public ClassX createClassX(Database db, School school, String descr) {
    return db.getClassXRepository()
        .save(
            new ClassX()
                .setCreationTime(Instant.now())
                .setSchool(school)
                .setName(descr)
                .setShortDescr(descr)
                .setShortDescrQuill(QuillInitializer.toQuillDelta(descr))
                .setLongDescr(descr)
                .setLongDescrQuill(QuillInitializer.toQuillDelta(descr)));
  }

  public Assignment createAssignment(
      Database db, ClassX classX, String descr, KnowledgeAndSkill... knowledgeAndSkills) {
    Assignment assignment =
        db.getAssignmentRepository()
            .save(
                new Assignment()
                    .setCreationTime(Instant.now())
                    .setClassX(classX)
                    .setName(descr)
                    .setShortDescr(descr)
                    .setShortDescrQuill(QuillInitializer.toQuillDelta(descr))
                    .setLongDescr(descr)
                    .setLongDescrQuill(QuillInitializer.toQuillDelta(descr)));

    Arrays.asList(knowledgeAndSkills)
        .forEach(
            ks ->
                db.getAssignmentKnowledgeAndSkillRepository()
                    .save(
                        db.getAssignmentKnowledgeAndSkillRepository()
                            .createAssignmentKnowledgeAndSkill(ks, assignment)));
    return assignment;
  }

  public KnowledgeAndSkill createKnowledgeAndSkill(Database db, ClassX classX, String descr) {
    return db.getKnowledgeAndSkillRepository()
        .save(
            new KnowledgeAndSkill()
                .setCreationTime(Instant.now())
                .setClassX(classX)
                .setName(descr)
                .setShortDescr(descr)
                .setShortDescrQuill(QuillInitializer.toQuillDelta(descr))
                .setLongDescr(descr)
                .setLongDescrQuill(QuillInitializer.toQuillDelta(descr)));
  }
}
