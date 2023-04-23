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

  private static final AtomicInteger counter =
      new AtomicInteger(new Random().nextInt(Integer.MAX_VALUE - 1000));

  private final Database db;

  public final UserX teacher;
  public final UserX student;
  public final UserX admin;

  public final ClassX chemistryClassX;
  public final KnowledgeAndSkill chemistryEks1, chemistryEks2;
  public final Assignment chemistryAssignment;

  public final ClassX programmingClassX;
  public final KnowledgeAndSkill programmingEks1, programmingEks2;
  public final Assignment programmingAssignment;

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
                new UserX()
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
                new UserX()
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
                new UserX()
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

    chemistryClassX = createClassX(db, school, "Chemistry I", "Intro to general chemistry.");
    addTeachersToClassX(db.getTeacherClassXRepository(), chemistryClassX, teacher);
    addStudentsToClassX(db.getStudentClassXRepository(), chemistryClassX, admin, teacher, student);

    chemistryEks1 =
        createKnowledgeAndSkill(
            db,
            chemistryClassX,
            "Periodic Table",
            "I can recognize the basic elements on a periodic table.");
    chemistryEks2 =
        createKnowledgeAndSkill(
            db,
            chemistryClassX,
            "Valence Electrons",
            "I can determine the number of valence electrons for each element.");

    chemistryAssignment =
        createAssignment(
            db,
            chemistryClassX,
            "Valence Electrons",
            "Show that you understand valence electrons.",
            chemistryEks1,
            chemistryEks2);

    programmingClassX = createClassX(db, school, "Computer Science I", "Intro to Programming.");
    addTeachersToClassX(db.getTeacherClassXRepository(), programmingClassX, teacher);
    addStudentsToClassX(
        db.getStudentClassXRepository(), programmingClassX, admin, teacher, student);

    programmingEks1 =
        createKnowledgeAndSkill(
            db,
            programmingClassX,
            "Sort Functions",
            "I understand and can implement different sort functions.");
    programmingEks2 =
        createKnowledgeAndSkill(
            db, programmingClassX, "Collections", "I can use Lists, Sets, and Maps.");

    programmingAssignment =
        createAssignment(
            db,
            programmingClassX,
            "Sort Algorithms",
            "Show that you can implement sort algorithms.",
            programmingEks1);
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
        int idNum = counter.incrementAndGet();
        student.setStudent(
            db.getStudentRepository()
                .save(
                    new Student()
                        .setCreationTime(Instant.now())
                        .setGrade(idNum)
                        .setStudentId(idNum)));
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

  private ClassX createClassX(Database db, School school, String name, String descr) {
    ClassX classX =
        db.getClassXRepository()
            .save(
                new ClassX()
                    .setCreationTime(Instant.now())
                    .setSchool(school)
                    .setName(name)
                    .setShortDescr(descr)
                    .setShortDescrQuill(QuillInitializer.toQuillDelta(descr))
                    .setLongDescr(descr)
                    .setLongDescrQuill(QuillInitializer.toQuillDelta(descr)));
    return classX;
  }

  private Assignment createAssignment(
      Database db,
      ClassX classX,
      String name,
      String descr,
      KnowledgeAndSkill... knowledgeAndSkills) {
    Assignment assignment =
        db.getAssignmentRepository()
            .save(
                new Assignment()
                    .setCreationTime(Instant.now())
                    .setClassX(classX)
                    .setName(name)
                    .setShortDescr(descr)
                    .setShortDescrQuill(QuillInitializer.toQuillDelta(descr))
                    .setLongDescr(descr)
                    .setLongDescrQuill(QuillInitializer.toQuillDelta(descr)));

    var ksaRepo = db.getKnowledgeAndSkillAssignmentRepository();
    Arrays.asList(knowledgeAndSkills)
        .forEach(ks -> ksaRepo.save(ksaRepo.createKnowledgeAndSkillAssignment(ks, assignment)));
    return assignment;
  }

  public KnowledgeAndSkill createKnowledgeAndSkill(
      Database db, ClassX classX, String name, String descr) {
    return db.getKnowledgeAndSkillRepository()
        .save(
            new KnowledgeAndSkill()
                .setCreationTime(Instant.now())
                .setClassX(classX)
                .setName(name)
                .setShortDescr(descr)
                .setShortDescrQuill(QuillInitializer.toQuillDelta(descr))
                .setLongDescr(descr)
                .setLongDescrQuill(QuillInitializer.toQuillDelta(descr)));
  }
}
