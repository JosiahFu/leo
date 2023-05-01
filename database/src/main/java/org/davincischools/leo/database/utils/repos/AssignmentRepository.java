package org.davincischools.leo.database.utils.repos;

import java.util.Optional;
import org.davincischools.leo.database.daos.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

  @Query(
      "SELECT a"
          + " FROM Assignment a"
          + " JOIN FETCH StudentClassX sc"
          + " ON sc.classX.id = a.classX.id"
          + " WHERE sc.student.id = (:studentId)")
  Iterable<Assignment> findAllByStudentId(@Param("studentId") int studentId);

  @Query(
      "SELECT a"
          + " FROM Assignment a"
          + " WHERE a.classX.id = (:classXId)"
          + " AND a.name = (:name)")
  Optional<Assignment> findByName(@Param("classXId") int classXId, @Param("name") String name);
}
