package org.davincischools.leo.database.utils.repos;

import java.time.Instant;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.StudentSchool;
import org.davincischools.leo.database.daos.StudentSchoolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentSchoolRepository extends JpaRepository<StudentSchool, StudentSchoolId> {

  default StudentSchool saveStudentSchool(Student student, School school) {
    return saveAndFlush(
        new StudentSchool()
            .setCreationTime(Instant.now())
            .setId(new StudentSchoolId().setStudentId(student.getId()).setSchoolId(school.getId()))
            .setStudent(student)
            .setSchool(school));
  }
}
