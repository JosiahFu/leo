package org.davincischools.leo.database.utils.repos;

import java.util.ArrayList;
import java.util.List;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.UserX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

  @Query(
      "SELECT u, s, c, a FROM UserX u"
          + " INNER JOIN FETCH Student s"
          + " ON s.id = u.student.id"
          + " INNER JOIN FETCH StudentClassX sc"
          + " ON sc.student.id = s.id"
          + " INNER JOIN FETCH ClassX c"
          + " ON c.id = sc.classX.id"
          + " INNER JOIN FETCH Assignment a"
          + " ON a.classX.id = c.id"
          + " WHERE u.id = (:userXId)")
  List<Object[]> _internal_findAllAssignmentsByStudentUserXId(@Param("userXId") int userXId);

  record StudentAssignment(UserX user, Student student, ClassX classX, Assignment assignment) {}

  default List<StudentAssignment> findAllAssignmentsByStudentUserXId(int userXId) {
    List<StudentAssignment> studentAssignments = new ArrayList<>();
    for (var result : _internal_findAllAssignmentsByStudentUserXId(userXId)) {
      studentAssignments.add(
          new StudentAssignment(
              (UserX) result[0], (Student) result[1], (ClassX) result[2], (Assignment) result[3]));
    }
    return studentAssignments;
  }
}
