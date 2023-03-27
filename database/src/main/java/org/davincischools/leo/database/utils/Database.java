package org.davincischools.leo.database.utils;

import java.util.Optional;
import org.davincischools.leo.database.daos.Admin;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.Class;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.davincischools.leo.database.daos.Portfolio;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.ProjectCycle;
import org.davincischools.leo.database.daos.ProjectPost;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
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
  public static final int USER_MAX_PASSWORD_BLOB_LENGTH =
      EntityUtils.getColumn(User.class, User.COLUMN_ENCODEDPASSWORD_NAME).length();

  @Repository
  public interface AdminsRepository extends CrudRepository<Admin, Long> {}

  @Repository
  public interface AssignmentsRepository extends CrudRepository<Assignment, Long> {}

  @Repository
  public interface ClassesRepository extends CrudRepository<Class, Long> {}

  @Repository
  public interface DistrictsRepository extends CrudRepository<District, Long> {}

  @Repository
  public interface KnowledgeAndSkillsRepository extends CrudRepository<KnowledgeAndSkill, Long> {}

  @Repository
  public interface PortfoliosRepository extends CrudRepository<Portfolio, Long> {}

  @Repository
  public interface ProjectsRepository extends CrudRepository<Project, Long> {}

  @Repository
  public interface ProjectCyclesRepository extends CrudRepository<ProjectCycle, Long> {}

  @Repository
  public interface ProjectPostsRepository extends CrudRepository<ProjectPost, Long> {}

  @Repository
  public interface SchoolsRepository extends CrudRepository<School, Long> {}

  @Repository
  public interface StudentsRepository extends CrudRepository<Student, Long> {}

  @Repository
  public interface TeachersRepository extends CrudRepository<Teacher, Long> {}

  @Repository
  public interface UsersRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmailAddress(String emailAddress);
  }

  @Autowired private AdminsRepository admins;
  @Autowired private AssignmentsRepository assignments;
  @Autowired private ClassesRepository classes;
  @Autowired private DistrictsRepository districts;
  @Autowired private KnowledgeAndSkillsRepository knowledgeAndSkills;
  @Autowired private PortfoliosRepository portfolios;
  @Autowired private ProjectsRepository projects;
  @Autowired private ProjectCyclesRepository projectCycles;
  @Autowired private ProjectPostsRepository projectPosts;
  @Autowired private SchoolsRepository schools;
  @Autowired private StudentsRepository students;
  @Autowired private TeachersRepository teachers;
  @Autowired private UsersRepository users;

  public Database() {}

  public AdminsRepository getAdmins() {
    return admins;
  }

  public AssignmentsRepository getAssignments() {
    return assignments;
  }

  public ClassesRepository getClasses() {
    return classes;
  }

  public DistrictsRepository getDistricts() {
    return districts;
  }

  public KnowledgeAndSkillsRepository getKnowledgeAndSkills() {
    return knowledgeAndSkills;
  }

  public PortfoliosRepository getPortfolios() {
    return portfolios;
  }

  public ProjectsRepository getProjects() {
    return projects;
  }

  public ProjectCyclesRepository getProjectCycles() {
    return projectCycles;
  }

  public ProjectPostsRepository getProjectPosts() {
    return projectPosts;
  }

  public SchoolsRepository getSchools() {
    return schools;
  }

  public StudentsRepository getStudents() {
    return students;
  }

  public TeachersRepository getTeachers() {
    return teachers;
  }

  public UsersRepository getUsers() {
    return users;
  }
}
