package org.davincischools.leo.database.utils.repos;

import java.util.List;
import org.davincischools.leo.database.daos.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

  @Query(
      "SELECT p FROM Project p"
          + " INNER JOIN ProjectInput ii"
          + " ON ii.id = p.projectInput.id"
          + " INNER JOIN UserX u"
          + " ON u.id = ii.userX.id"
          + " WHERE u.id = (:userXId)"
          + " ORDER BY p.creationTime DESC")
  List<Project> findAllByUserXId(@Param("userXId") int userXId);
}
