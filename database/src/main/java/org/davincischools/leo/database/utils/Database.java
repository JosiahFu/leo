package org.davincischools.leo.database.utils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.davincischools.leo.database.daos.Admin;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.Class;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.IkigaiInput;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.KnowledgeAndSkillAssignment;
import org.davincischools.leo.database.daos.KnowledgeAndSkillAssignmentId;
import org.davincischools.leo.database.daos.Log;
import org.davincischools.leo.database.daos.Portfolio;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectCycle;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.StudentClass;
import org.davincischools.leo.database.daos.StudentClassId;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClass;
import org.davincischools.leo.database.daos.TeacherClassId;
import org.davincischools.leo.database.daos.TeacherSchool;
import org.davincischools.leo.database.daos.TeacherSchoolId;
import org.davincischools.leo.database.daos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@EnableJpaRepositories(
    basePackageClasses = {User.class, Database.class},
    considerNestedRepositories = true)
@EntityScan(basePackageClasses = User.class)
public class Database {

  public static final int USER_MAX_EMAIL_ADDRESS_LENGTH =
      EntityUtils.getColumn(User.class, User.COLUMN_EMAILADDRESS_NAME).length();
  public static final int USER_MAX_FIRST_NAME_LENGTH =
      EntityUtils.getColumn(User.class, User.COLUMN_FIRSTNAME_NAME).length();
  public static final int USER_MAX_LAST_NAME_LENGTH =
      EntityUtils.getColumn(User.class, User.COLUMN_LASTNAME_NAME).length();
  public static final int USER_MIN_PASSWORD_LENGTH = 8;

  @Repository
  public interface AdminRepository extends JpaRepository<Admin, Integer> {}

  @Repository
  public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    @Query(
        "SELECT ks FROM KnowledgeAndSkill ks "
            + "INNER JOIN FETCH Assignment a "
            + "INNER JOIN FETCH KnowledgeAndSkillAssignment ksa "
            + "WHERE a.id = (:assignment_id) "
            + "AND a.id = ksa.assignment.id "
            + "AND ksa.knowledgeAndSkill.id = ks.id")
    public List<KnowledgeAndSkill> findAllKnowledgeAndSkillsById(
        @Param("assignment_id") int assignment_id);
  }

  @Repository
  public interface ClassRepository extends JpaRepository<Class, Integer> {}

  @Repository
  public interface DistrictRepository extends JpaRepository<District, Integer> {}

  @Repository
  public interface IkigaiInputRepository extends JpaRepository<IkigaiInput, Integer> {}

  @Repository
  public interface InterestRepository extends JpaRepository<Interest, Integer> {}

  @Repository
  public interface KnowledgeAndSkillRepository extends JpaRepository<KnowledgeAndSkill, Integer> {}

  @Repository
  public interface KnowledgeAndSkillAssignmentRepository
      extends JpaRepository<KnowledgeAndSkillAssignment, KnowledgeAndSkillAssignmentId> {

    default KnowledgeAndSkillAssignmentId createKnowledgeAndSkillAssignmentId(
        KnowledgeAndSkill knowledgeAndSkill, Assignment assignment) {
      return new KnowledgeAndSkillAssignmentId()
          .setKnowledgeAndSkillId(knowledgeAndSkill.getId())
          .setAssignmentId(assignment.getId());
    }

    default KnowledgeAndSkillAssignment createKnowledgeAndSkillAssignment(
        KnowledgeAndSkill knowledgeAndSkill, Assignment assignment) {
      return new KnowledgeAndSkillAssignment()
          .setCreationTime(Instant.now())
          .setId(createKnowledgeAndSkillAssignmentId(knowledgeAndSkill, assignment))
          .setKnowledgeAndSkill(knowledgeAndSkill)
          .setAssignment(assignment);
    }
  }

  @Repository
  public interface LogRepository extends JpaRepository<Log, Integer> {}

  @Repository
  public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {}

  @Repository
  public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query(
        "SELECT p FROM Project p "
            + "INNER JOIN IkigaiInput ii "
            + "INNER JOIN User u "
            + "WHERE u.id = (:user_id) "
            + "ORDER BY p.creationTime DESC")
    List<Project> findAllByUserId(@Param("user_id") int userId);
  }

  @Repository
  public interface ProjectCycleRepository extends JpaRepository<ProjectCycle, Integer> {}

  @Repository
  public interface ProjectPostRepository extends JpaRepository<ProjectPost, Integer> {}

  @Repository
  public interface ProjectPostCommentRepository
      extends JpaRepository<ProjectPostComment, Integer> {}

  @Repository
  public interface SchoolRepository extends JpaRepository<School, Integer> {

    Iterable<School> findAllByDistrictId(Integer district_id);
  }

  @Repository
  public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query(
        "SELECT u, s, c, a FROM User u "
            + "INNER JOIN FETCH Student s "
            + "INNER JOIN FETCH StudentClass sc "
            + "INNER JOIN FETCH Class c "
            + "INNER JOIN FETCH Assignment a "
            + "WHERE u.id = (:user_id) "
            + "AND u.student.id = s.id "
            + "AND s.id = sc.student.id "
            + "AND sc.classField.id = c.id "
            + "AND c.id = a.classField.id")
    public List<Object[]> _internal_findAllAssignmentsByStudentUserId(@Param("user_id") int userId);

    public record StudentAssignment(
        User user, Student student, Class classField, Assignment assignment) {}

    default List<StudentAssignment> findAllAssignmentsByStudentUserId(int user_id) {
      List<StudentAssignment> studentAssignments = new ArrayList<>();
      for (var result : _internal_findAllAssignmentsByStudentUserId(user_id)) {
        studentAssignments.add(
            new StudentAssignment(
                (User) result[0], (Student) result[1], (Class) result[2], (Assignment) result[3]));
      }
      return studentAssignments;
    }
  }

  @Repository
  public interface StudentClassRepository extends JpaRepository<StudentClass, StudentClassId> {

    default StudentClass createStudentClass(Student student, Class classField) {
      return new StudentClass()
          .setCreationTime(Instant.now())
          .setId(new StudentClassId().setStudentId(student.getId()).setClassId(classField.getId()))
          .setStudent(student)
          .setClassField(classField);
    }
  }

  @Repository
  public interface TeacherRepository extends JpaRepository<Teacher, Integer> {}

  @Repository
  public interface TeacherClassRepository extends JpaRepository<TeacherClass, TeacherClassId> {

    default TeacherClass createTeacherClass(Teacher teacher, Class classField) {
      return new TeacherClass()
          .setCreationTime(Instant.now())
          .setId(new TeacherClassId().setTeacherId(teacher.getId()).setClassId(classField.getId()))
          .setTeacher(teacher)
          .setClassField(classField);
    }
  }

  @Repository
  public interface TeacherSchoolRepository extends JpaRepository<TeacherSchool, TeacherSchoolId> {

    default TeacherSchoolId createTeacherSchoolId(Teacher teacher, School school) {
      return new TeacherSchoolId().setTeacherId(teacher.getId()).setSchoolId(school.getId());
    }

    default TeacherSchool createTeacherSchool(Teacher teacher, School school) {
      return new TeacherSchool()
          .setCreationTime(Instant.now())
          .setCreationTime(Instant.now())
          .setId(createTeacherSchoolId(teacher, school))
          .setTeacher(teacher)
          .setSchool(school);
    }

    @Query(
        "SELECT s FROM School s "
            + "INNER JOIN FETCH TeacherSchool ts "
            + "INNER JOIN FETCH Teacher t "
            + "INNER JOIN FETCH User u "
            + "WHERE u.id = (:user_id) "
            + "AND t.id = u.teacher.id "
            + "AND ts.teacher.id = t.id "
            + "AND ts.school.id = s.id ")
    List<School> findSchoolsByUserId(@Param("user_id") int userId);

    @Modifying
    @Query(
        "DELETE TeacherSchool ts "
            + "WHERE ts.teacher.id = (:teacher_id) "
            + "AND NOT ts.school.id IN (:school_ids)")
    void keepSchoolsByTeacherId(
        @Param("teacher_id") int teacherId, @Param("school_ids") Iterable<Integer> schoolIdsToKeep);
  }

  @Repository
  public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(
        "SELECT u "
            + "FROM User u "
            + "LEFT JOIN FETCH Admin a "
            + "LEFT JOIN FETCH Teacher t "
            + "LEFT JOIN FETCH Student s "
            + "WHERE u.emailAddress = (:email_address) ")
    Optional<User> findFullUserByEmailAddress(@Param("email_address") String emailAddress);

    @Query(
        "SELECT u "
            + "FROM User u "
            + "JOIN FETCH District d "
            + "LEFT JOIN FETCH Admin a "
            + "LEFT JOIN FETCH Teacher t "
            + "LEFT JOIN FETCH Student s "
            + "WHERE d.id = (:district_id) ")
    List<User> findAllFullUsersByDistrictId(@Param("district_id") int districtId);

    @Query(
        "SELECT u "
            + "FROM User u "
            + "LEFT JOIN FETCH Admin a "
            + "LEFT JOIN FETCH Teacher t "
            + "LEFT JOIN FETCH Student s "
            + "WHERE u.id = (:user_id) ")
    Optional<User> findFullUserByUserId(@Param("user_id") int userId);
  }

  @Autowired private AdminRepository adminRepository;
  @Autowired private AssignmentRepository assignmentRepository;
  @Autowired private ClassRepository classRepository;
  @Autowired private DistrictRepository districtRepository;
  @Autowired private IkigaiInputRepository ikigaiInputRepository;
  @Autowired private InterestRepository interestRepository;
  @Autowired private KnowledgeAndSkillRepository knowledgeAndSkillRepository;
  @Autowired private KnowledgeAndSkillAssignmentRepository knowledgeAndSkillAssignmentRepository;
  @Autowired private LogRepository logRepository;
  @Autowired private PortfolioRepository portfolioRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private ProjectCycleRepository projectCycleRepository;
  @Autowired private ProjectPostRepository projectPostRepository;
  @Autowired private ProjectPostCommentRepository projectPostCommentRepository;
  @Autowired private SchoolRepository schoolRepository;
  @Autowired private StudentRepository studentRepository;
  @Autowired private StudentClassRepository studentClassRepository;
  @Autowired private TeacherRepository teacherRepository;
  @Autowired private TeacherClassRepository teacherClassRepository;
  @Autowired private TeacherSchoolRepository teacherSchoolRepository;
  @Autowired private UserRepository userRepository;

  public Database() {}

  public AdminRepository getAdminRepository() {
    return adminRepository;
  }

  public AssignmentRepository getAssignmentRepository() {
    return assignmentRepository;
  }

  public ClassRepository getClassRepository() {
    return classRepository;
  }

  public DistrictRepository getDistrictRepository() {
    return districtRepository;
  }

  public IkigaiInputRepository getIkigaiInputRepository() {
    return ikigaiInputRepository;
  }

  public InterestRepository getInterestRepository() {
    return interestRepository;
  }

  public KnowledgeAndSkillRepository getKnowledgeAndSkillRepository() {
    return knowledgeAndSkillRepository;
  }

  public KnowledgeAndSkillAssignmentRepository getKnowledgeAndSkillAssignmentRepository() {
    return knowledgeAndSkillAssignmentRepository;
  }

  public LogRepository getLogRepository() {
    return logRepository;
  }

  public PortfolioRepository getPortfolioRepository() {
    return portfolioRepository;
  }

  public ProjectRepository getProjectRepository() {
    return projectRepository;
  }

  public ProjectCycleRepository getProjectCycleRepository() {
    return projectCycleRepository;
  }

  public ProjectPostRepository getProjectPostRepository() {
    return projectPostRepository;
  }

  public ProjectPostCommentRepository getProjectPostCommentRepository() {
    return projectPostCommentRepository;
  }

  public SchoolRepository getSchoolRepository() {
    return schoolRepository;
  }

  public StudentRepository getStudentRepository() {
    return studentRepository;
  }

  public StudentClassRepository getStudentClassRepository() {
    return studentClassRepository;
  }

  public TeacherRepository getTeacherRepository() {
    return teacherRepository;
  }

  public TeacherClassRepository getTeacherClassRepository() {
    return teacherClassRepository;
  }

  public TeacherSchoolRepository getTeacherSchoolRepository() {
    return teacherSchoolRepository;
  }

  public UserRepository getUserRepository() {
    return userRepository;
  }
}
