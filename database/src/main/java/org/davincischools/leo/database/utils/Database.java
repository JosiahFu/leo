package org.davincischools.leo.database.utils;

import java.util.Optional;
import org.davincischools.leo.database.daos.Admin;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.Class;
import org.davincischools.leo.database.daos.ClassStudent;
import org.davincischools.leo.database.daos.ClassTeacher;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Portfolio;
import org.davincischools.leo.database.daos.PortfolioPost;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectCycle;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.ProjectPostComment;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherSchool;
import org.davincischools.leo.database.daos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
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
  public static final int USER_MAX_ENCODED_PASSWORD_UTF8_BLOB_LENGTH = 65535;

  @Repository
  public interface AdminRepository extends JpaRepository<Admin, Integer> {}

  @Repository
  public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {}

  @Repository
  public interface ClassRepository extends JpaRepository<Class, Integer> {}

  @Repository
  public interface ClassStudentRepository extends JpaRepository<ClassStudent, Integer> {}

  @Repository
  public interface ClassTeacherRepository extends JpaRepository<ClassTeacher, Integer> {}

  @Repository
  public interface DistrictRepository extends JpaRepository<District, Integer> {}

  @Repository
  public interface KnowledgeAndSkillRepository extends JpaRepository<KnowledgeAndSkill, Integer> {}

  @Repository
  public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {}

  @Repository
  public interface PortfolioPostRepository extends JpaRepository<PortfolioPost, Integer> {}

  @Repository
  public interface ProjectRepository extends JpaRepository<Project, Integer> {}

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
  public interface StudentRepository extends JpaRepository<Student, Integer> {}

  @Repository
  public interface TeacherRepository extends JpaRepository<Teacher, Integer> {}

  @Repository
  public interface TeacherSchoolRepository extends JpaRepository<TeacherSchool, Integer> {}

  @Repository
  public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmailAddress(String emailAddress);

    Iterable<User> findAllByDistrictId(int districtId);

    @Query(
        "SELECT u FROM User u "
            + "LEFT JOIN FETCH Admin a "
            + "LEFT JOIN FETCH Teacher t "
            + "LEFT JOIN FETCH Student s "
            + "WHERE u.id = (:user_id)")
    Optional<User> findByIdWithRoles(@Param("user_id") int user_id);
  }

  @Autowired private AdminRepository adminRepository;
  @Autowired private AssignmentRepository assignmentRepository;
  @Autowired private ClassRepository classRepository;
  @Autowired private ClassStudentRepository classStudentRepository;
  @Autowired private ClassTeacherRepository classTeacherRepository;
  @Autowired private DistrictRepository districtRepository;
  @Autowired private KnowledgeAndSkillRepository knowledgeAndSkillRepository;
  @Autowired private PortfolioRepository portfolioRepository;
  @Autowired private PortfolioPostRepository portfolioPostRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private ProjectCycleRepository projectCycleRepository;
  @Autowired private ProjectPostRepository projectPostRepository;
  @Autowired private ProjectPostCommentRepository projectPostCommentRepository;
  @Autowired private SchoolRepository schoolRepository;
  @Autowired private StudentRepository studentRepository;
  @Autowired private TeacherRepository teacherRepository;
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

  public ClassStudentRepository getClassStudentRepository() {
    return classStudentRepository;
  }

  public ClassTeacherRepository getClassTeacherRepository() {
    return classTeacherRepository;
  }

  public DistrictRepository getDistrictRepository() {
    return districtRepository;
  }

  public KnowledgeAndSkillRepository getKnowledgeAndSkillRepository() {
    return knowledgeAndSkillRepository;
  }

  public PortfolioRepository getPortfolioRepository() {
    return portfolioRepository;
  }

  public PortfolioPostRepository getPortfolioPostRepository() {
    return portfolioPostRepository;
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

  public TeacherRepository getTeacherRepository() {
    return teacherRepository;
  }

  public TeacherSchoolRepository getTeacherSchoolRepository() {
    return teacherSchoolRepository;
  }

  public UserRepository getUserRepository() {
    return userRepository;
  }
}
