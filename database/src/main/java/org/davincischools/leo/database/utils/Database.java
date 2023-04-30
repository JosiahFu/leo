package org.davincischools.leo.database.utils;

import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.repos.AdminXRepository;
import org.davincischools.leo.database.utils.repos.AssignmentKnowledgeAndSkillRepository;
import org.davincischools.leo.database.utils.repos.AssignmentRepository;
import org.davincischools.leo.database.utils.repos.ClassXRepository;
import org.davincischools.leo.database.utils.repos.DistrictRepository;
import org.davincischools.leo.database.utils.repos.InterestRepository;
import org.davincischools.leo.database.utils.repos.KnowledgeAndSkillRepository;
import org.davincischools.leo.database.utils.repos.LogReferenceRepository;
import org.davincischools.leo.database.utils.repos.LogRepository;
import org.davincischools.leo.database.utils.repos.PortfolioRepository;
import org.davincischools.leo.database.utils.repos.ProjectCycleRepository;
import org.davincischools.leo.database.utils.repos.ProjectInputRepository;
import org.davincischools.leo.database.utils.repos.ProjectPostCommentRepository;
import org.davincischools.leo.database.utils.repos.ProjectPostRepository;
import org.davincischools.leo.database.utils.repos.ProjectRepository;
import org.davincischools.leo.database.utils.repos.SchoolRepository;
import org.davincischools.leo.database.utils.repos.StudentClassXRepository;
import org.davincischools.leo.database.utils.repos.StudentRepository;
import org.davincischools.leo.database.utils.repos.StudentSchoolRepository;
import org.davincischools.leo.database.utils.repos.TeacherClassXRepository;
import org.davincischools.leo.database.utils.repos.TeacherRepository;
import org.davincischools.leo.database.utils.repos.TeacherSchoolRepository;
import org.davincischools.leo.database.utils.repos.UserXRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

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

  @Autowired private AdminXRepository adminRepository;
  @Autowired private AssignmentKnowledgeAndSkillRepository assignmentKnowledgeAndSkillRepository;
  @Autowired private AssignmentRepository assignmentRepository;
  @Autowired private ClassXRepository classRepository;
  @Autowired private DistrictRepository districtRepository;
  @Autowired private InterestRepository interestRepository;
  @Autowired private KnowledgeAndSkillRepository knowledgeAndSkillRepository;
  @Autowired private LogReferenceRepository logReferenceRepository;
  @Autowired private LogRepository logRepository;
  @Autowired private PortfolioRepository portfolioRepository;
  @Autowired private ProjectCycleRepository projectCycleRepository;
  @Autowired private ProjectInputRepository projectInputRepository;
  @Autowired private ProjectPostCommentRepository projectPostCommentRepository;
  @Autowired private ProjectPostRepository projectPostRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private SchoolRepository schoolRepository;
  @Autowired private StudentClassXRepository studentClassXRepository;
  @Autowired private StudentRepository studentRepository;
  @Autowired private StudentSchoolRepository studentSchoolRepository;
  @Autowired private TeacherClassXRepository teacherClassXRepository;
  @Autowired private TeacherRepository teacherRepository;
  @Autowired private TeacherSchoolRepository teacherSchoolRepository;
  @Autowired private UserXRepository userRepository;

  public Database() {}

  public AdminXRepository getAdminXRepository() {
    return adminRepository;
  }

  public AssignmentKnowledgeAndSkillRepository getAssignmentKnowledgeAndSkillRepository() {
    return assignmentKnowledgeAndSkillRepository;
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

  public InterestRepository getInterestRepository() {
    return interestRepository;
  }

  public KnowledgeAndSkillRepository getKnowledgeAndSkillRepository() {
    return knowledgeAndSkillRepository;
  }

  public LogReferenceRepository getLogReferenceRepository() {
    return logReferenceRepository;
  }

  public LogRepository getLogRepository() {
    return logRepository;
  }

  public PortfolioRepository getPortfolioRepository() {
    return portfolioRepository;
  }

  public ProjectCycleRepository getProjectCycleRepository() {
    return projectCycleRepository;
  }

  public ProjectInputRepository getProjectInputRepository() {
    return projectInputRepository;
  }

  public ProjectPostCommentRepository getProjectPostCommentRepository() {
    return projectPostCommentRepository;
  }

  public ProjectPostRepository getProjectPostRepository() {
    return projectPostRepository;
  }

  public ProjectRepository getProjectRepository() {
    return projectRepository;
  }

  public SchoolRepository getSchoolRepository() {
    return schoolRepository;
  }

  public StudentClassXRepository getStudentClassXRepository() {
    return studentClassXRepository;
  }

  public StudentRepository getStudentRepository() {
    return studentRepository;
  }

  public StudentSchoolRepository getStudentSchoolRepository() {
    return studentSchoolRepository;
  }

  public TeacherClassXRepository getTeacherClassXRepository() {
    return teacherClassXRepository;
  }

  public TeacherRepository getTeacherRepository() {
    return teacherRepository;
  }

  public TeacherSchoolRepository getTeacherSchoolRepository() {
    return teacherSchoolRepository;
  }

  public UserXRepository getUserXRepository() {
    return userRepository;
  }
}
