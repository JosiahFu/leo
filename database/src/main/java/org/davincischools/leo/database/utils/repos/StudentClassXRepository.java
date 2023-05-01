package org.davincischools.leo.database.utils.repos;

import java.time.Instant;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.StudentClassX;
import org.davincischools.leo.database.daos.StudentClassXId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentClassXRepository extends JpaRepository<StudentClassX, StudentClassXId> {

  default StudentClassX saveStudentClassX(Student student, ClassX classX) {
    return saveAndFlush(
        new StudentClassX()
            .setCreationTime(Instant.now())
            .setId(new StudentClassXId().setStudentId(student.getId()).setClassXId(classX.getId()))
            .setStudent(student)
            .setClassX(classX));
  }
}
