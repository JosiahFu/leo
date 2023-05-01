package org.davincischools.leo.database.utils.repos;

import java.util.Optional;
import org.davincischools.leo.database.daos.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {

  Iterable<School> findAllByDistrictId(Integer districtId);

  @Query(
      "SELECT s"
          + " FROM School s"
          + " WHERE s.district.id = (:districtId)"
          + " AND s.nickname = (:nickname)")
  Optional<School> findByNickname(
      @Param("districtId") int districtId, @Param("nickname") String nickname);

  @Query(
      "SELECT s FROM School s"
          + " INNER JOIN FETCH TeacherSchool ts"
          + " ON ts.school.id = s.id"
          + " WHERE ts.teacher.id = (:teacherId)")
  Iterable<School> findAllByTeacherId(@Param("teacherId") int teacherId);
}
