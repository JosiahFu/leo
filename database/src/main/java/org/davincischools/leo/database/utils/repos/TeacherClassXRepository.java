package org.davincischools.leo.database.utils.repos;

import java.time.Instant;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherClassX;
import org.davincischools.leo.database.daos.TeacherClassXId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherClassXRepository extends JpaRepository<TeacherClassX, TeacherClassXId> {

  default TeacherClassX saveTeacherClassX(Teacher teacher, ClassX classX) {
    return saveAndFlush(
        new TeacherClassX()
            .setCreationTime(Instant.now())
            .setId(new TeacherClassXId().setTeacherId(teacher.getId()).setClassXId(classX.getId()))
            .setTeacher(teacher)
            .setClassX(classX));
  }
}
