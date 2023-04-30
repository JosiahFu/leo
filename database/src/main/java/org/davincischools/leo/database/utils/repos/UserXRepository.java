package org.davincischools.leo.database.utils.repos;

import java.util.List;
import java.util.Optional;
import org.davincischools.leo.database.daos.UserX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserXRepository extends JpaRepository<UserX, Integer> {

  @Query(
      "SELECT u, a, t, s FROM UserX u"
          + " LEFT JOIN FETCH AdminX a"
          + " ON a.id = u.adminX.id"
          + " LEFT JOIN FETCH Teacher t"
          + " ON t.id = u.teacher.id"
          + " LEFT JOIN FETCH Student s"
          + " ON s.id = u.student.id"
          + " WHERE u.emailAddress = (:emailAddress)")
  Optional<UserX> findFullUserXByEmailAddress(@Param("emailAddress") String emailAddress);

  @Query(
      "SELECT u, a, t, s FROM UserX u"
          + " JOIN FETCH District d"
          + " ON d.id = u.district.id"
          + " LEFT JOIN FETCH AdminX a"
          + " ON a.id = u.adminX.id"
          + " LEFT JOIN FETCH Teacher t"
          + " ON t.id = u.teacher.id"
          + " LEFT JOIN FETCH Student s"
          + " ON s.id = u.student.id"
          + " WHERE d.id = (:districtId)")
  List<UserX> findAllFullUserXsByDistrictId(@Param("districtId") int districtId);

  @Query(
      "SELECT u, a, t, s FROM UserX u"
          + " LEFT JOIN FETCH AdminX a"
          + " ON u.adminX.id = a.id"
          + " LEFT JOIN FETCH Teacher t"
          + " ON u.teacher.id = t.id"
          + " LEFT JOIN FETCH Student s"
          + " ON u.student.id = s.id"
          + " WHERE u.id = (:userXId)")
  Optional<UserX> findFullUserXByUserXId(@Param("userXId") int userXId);
}
