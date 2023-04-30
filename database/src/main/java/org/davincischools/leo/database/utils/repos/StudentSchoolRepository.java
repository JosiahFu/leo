package org.davincischools.leo.database.utils.repos;

import java.time.Instant;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.StudentSchool;
import org.davincischools.leo.database.daos.StudentSchoolId;
import org.davincischools.leo.database.daos.TeacherSchool;
import org.davincischools.leo.database.daos.TeacherSchoolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentSchoolRepository extends JpaRepository<TeacherSchool, TeacherSchoolId> {

  default StudentSchool createStudentSchool(Student student, School school) {
    return new StudentSchool()
        .setCreationTime(Instant.now())
        .setId(new StudentSchoolId().setStudentId(student.getId()).setSchoolId(school.getId()))
        .setStudent(student)
        .setSchool(school);
  }
}
