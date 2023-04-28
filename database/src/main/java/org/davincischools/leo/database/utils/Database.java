package org.davincischools.leo.database.utils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.davincischools.leo.database.daos.AdminX;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.IkigaiInput;
import org.davincischools.leo.database.daos.Interest;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.KnowledgeAndSkillAssignment;
import org.davincischools.leo.database.daos.KnowledgeAndSkillAssignmentId;
import org.davincischools.leo.database.daos.Log;
import org.davincischools.leo.database.daos.LogReference;
import org.davincischools.leo.database.daos.Portfolio;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectCycle;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.StudentClassX;
import org.davincischools.leo.database.daos.StudentClassXId;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.daos.TeacherClassXId;
import org.davincischools.leo.database.daos.TeacherSchool;
import org.davincischools.leo.database.daos.TeacherSchoolId;
import org.davincischools.leo.database.daos.UserX;
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
    basePackageClasses = {UserX.class, Database.class},
    considerNestedRepositories = true)
@EntityScan(basePackageClasses = UserX.class)
public class Database {

  public static final int USER_MAX_EMAIL_ADDRESS_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_EMAILADDRESS_NAME).length();
  public static final int USER_MAX_FIRST_NAME_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_FIRSTNAME_NAME).length();
  public static final int USER_MAX_LAST_NAME_LENGTH =
      EntityUtils.getColumn(UserX.class, UserX.COLUMN_LASTNAME_NAME).length();
  public static final int USER_MIN_PASSWORD_LENGTH = 8;

  @Repository
  public interface AdminXRepository extends JpaRepository<AdminX, Integer> {}

  @Repository
  public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    @Query(
        "SELECT ks FROM KnowledgeAndSkill ks "
            + "INNER JOIN FETCH Assignment a "
            + "INNER JOIN FETCH KnowledgeAndSkillAssignment ksa "
            + "WHERE a.id = (:assignmentId) "
            + "AND a.id = ksa.assignment.id "
            + "AND ksa.knowledgeAndSkill.id = ks.id")
    List<KnowledgeAndSkill> findAllKnowledgeAndSkillsById(@Param("assignmentId") int assignmentId);
  }

  @Repository
  public interface ClassXRepository extends JpaRepository<ClassX, Integer> {}

  @Repository
  public interface DistrictRepository extends JpaRepository<District, Integer> {
    District findByName(String name);
  }

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
  public interface LogReferenceRepository extends JpaRepository<LogReference, Integer> {}

  @Repository
  public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {}

  @Repository
  public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query(
        "SELECT p FROM Project p "
            + "INNER JOIN IkigaiInput ii "
            + "INNER JOIN UserX u "
            + "WHERE u.id = (:userXId) "
            + "ORDER BY p.creationTime DESC")
    List<Project> findAllByUserXId(@Param("userXId") int userXId);
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

    Iterable<School> findAllByDistrictId(Integer districtId);
  }

  @Repository
  public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query(
        "SELECT u, s, c, a FROM UserX u "
            + "INNER JOIN FETCH Student s "
            + "INNER JOIN FETCH StudentClassX sc "
            + "INNER JOIN FETCH ClassX c "
            + "INNER JOIN FETCH Assignment a "
            + "WHERE u.id = (:userXId) "
            + "AND u.student.id = s.id "
            + "AND s.id = sc.student.id "
            + "AND sc.classX.id = c.id "
            + "AND c.id = a.classX.id")
    List<Object[]> _internal_findAllAssignmentsByStudentUserXId(@Param("userXId") int userXId);

    record StudentAssignment(UserX user, Student student, ClassX classX, Assignment assignment) {}

    default List<StudentAssignment> findAllAssignmentsByStudentUserXId(int userXId) {
      List<StudentAssignment> studentAssignments = new ArrayList<>();
      for (var result : _internal_findAllAssignmentsByStudentUserXId(userXId)) {
        studentAssignments.add(
            new StudentAssignment(
                (UserX) result[0],
                (Student) result[1],
                (ClassX) result[2],
                (Assignment) result[3]));
      }
      return studentAssignments;
    }
  }

  @Repository
  public interface StudentClassXRepository extends JpaRepository<StudentClassX, StudentClassXId> {

    default StudentClassX createStudentClassX(Student student, ClassX classX) {
      return new StudentClassX()
          .setCreationTime(Instant.now())
          .setId(new StudentClassXId().setStudentId(student.getId()).setClassXId(classX.getId()))
          .setStudent(student)
          .setClassX(classX);
    }
  }

  @Repository
  public interface TeacherRepository extends JpaRepository<Teacher, Integer> {}

  @Repository
  public interface TeacherClassXRepository extends JpaRepository<TeacherClassX, TeacherClassXId> {

    default TeacherClassX createTeacherClassX(Teacher teacher, ClassX classX) {
      return new TeacherClassX()
          .setCreationTime(Instant.now())
          .setId(new TeacherClassXId().setTeacherId(teacher.getId()).setClassXId(classX.getId()))
          .setTeacher(teacher)
          .setClassX(classX);
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
            + "INNER JOIN FETCH UserX u "
            + "WHERE u.id = (:userXId) "
            + "AND t.id = u.teacher.id "
            + "AND ts.teacher.id = t.id "
            + "AND ts.school.id = s.id ")
    List<School> findSchoolsByUserXId(@Param("userXId") int userXId);

    @Modifying
    @Query(
        "DELETE TeacherSchool ts "
            + "WHERE ts.teacher.id = (:teacherId) "
            + "AND NOT ts.school.id IN (:schoolIds)")
    void keepSchoolsByTeacherId(
        @Param("teacherId") int teacherId, @Param("schoolIds") Iterable<Integer> schoolIdsToKeep);
  }

  @Repository
  public interface UserXRepository extends JpaRepository<UserX, Integer> {

    @Query(
        "SELECT u "
            + "FROM UserX u "
            + "LEFT JOIN FETCH AdminX a ON u.adminX.id = a.id "
            + "LEFT JOIN FETCH Teacher t ON u.teacher.id = t.id "
            + "LEFT JOIN FETCH Student s ON u.student.id = s.id "
            + "WHERE u.emailAddress = (:emailAddress) ")
    Optional<UserX> findFullUserXByEmailAddress(@Param("emailAddress") String emailAddress);

    @Query(
        "SELECT u "
            + "FROM UserX u "
            + "JOIN FETCH District d "
            + "LEFT JOIN FETCH AdminX a "
            + "LEFT JOIN FETCH Teacher t "
            + "LEFT JOIN FETCH Student s "
            + "WHERE d.id = (:districtId) ")
    List<UserX> findAllFullUserXsByDistrictId(@Param("districtId") int districtId);

    @Query(
        "SELECT u "
            + "FROM UserX u "
            + "LEFT JOIN FETCH AdminX a ON u.adminX.id = a.id "
            + "LEFT JOIN FETCH Teacher t ON u.teacher.id = t.id "
            + "LEFT JOIN FETCH Student s ON u.student.id = s.id "
            + "WHERE u.id = (:userXId) ")
    Optional<UserX> findFullUserXByUserXId(@Param("userXId") int userXId);
  }

  @Autowired private AdminXRepository adminRepository;
  @Autowired private AssignmentRepository assignmentRepository;
  @Autowired private ClassXRepository classRepository;
  @Autowired private DistrictRepository districtRepository;
  @Autowired private IkigaiInputRepository ikigaiInputRepository;
  @Autowired private InterestRepository interestRepository;
  @Autowired private KnowledgeAndSkillRepository knowledgeAndSkillRepository;
  @Autowired private KnowledgeAndSkillAssignmentRepository knowledgeAndSkillAssignmentRepository;
  @Autowired private LogRepository logRepository;
  @Autowired private LogReferenceRepository logReferenceRepository;
  @Autowired private PortfolioRepository portfolioRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private ProjectCycleRepository projectCycleRepository;
  @Autowired private ProjectPostRepository projectPostRepository;
  @Autowired private ProjectPostCommentRepository projectPostCommentRepository;
  @Autowired private SchoolRepository schoolRepository;
  @Autowired private StudentRepository studentRepository;
  @Autowired private StudentClassXRepository studentClassXRepository;
  @Autowired private TeacherRepository teacherRepository;
  @Autowired private TeacherClassXRepository teacherClassXRepository;
  @Autowired private TeacherSchoolRepository teacherSchoolRepository;
  @Autowired private UserXRepository userRepository;

  public Database() {}

  public AdminXRepository getAdminXRepository() {
    return adminRepository;
  }

  public AssignmentRepository getAssignmentRepository() {
    return assignmentRepository;
  }

  public ClassXRepository getClassXRepository() {
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

  public LogReferenceRepository getLogReferenceRepository() {
    return logReferenceRepository;
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

  public StudentClassXRepository getStudentClassXRepository() {
    return studentClassXRepository;
  }

  public TeacherRepository getTeacherRepository() {
    return teacherRepository;
  }

  public TeacherClassXRepository getTeacherClassXRepository() {
    return teacherClassXRepository;
  }

  public TeacherSchoolRepository getTeacherSchoolRepository() {
    return teacherSchoolRepository;
  }

  public UserXRepository getUserXRepository() {
    return userRepository;
  }
}
