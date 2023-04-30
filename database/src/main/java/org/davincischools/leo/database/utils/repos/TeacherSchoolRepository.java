package org.davincischools.leo.database.utils.repos;

import java.time.Instant;
import java.util.List;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.TeacherSchool;
import org.davincischools.leo.database.daos.TeacherSchoolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
      "SELECT s FROM School s"
          + " INNER JOIN FETCH TeacherSchool ts"
          + " ON ts.school.id = s.id"
          + " INNER JOIN FETCH Teacher t"
          + " ON t.id = ts.teacher.id"
          + " INNER JOIN FETCH UserX u"
          + " ON u.teacher.id = t.id"
          + " WHERE u.id = (:userXId)")
  List<School> findSchoolsByUserXId(@Param("userXId") int userXId);

  @Modifying
  @Query(
      "DELETE TeacherSchool ts"
          + " WHERE ts.teacher.id = (:teacherId)"
          + " AND NOT ts.school.id IN (:schoolIds)")
  void keepSchoolsByTeacherId(
      @Param("teacherId") int teacherId, @Param("schoolIds") Iterable<Integer> schoolIdsToKeep);
}
