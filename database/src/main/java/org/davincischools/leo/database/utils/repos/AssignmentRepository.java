package org.davincischools.leo.database.utils.repos;

import java.util.List;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

  @Query(
      "SELECT ks FROM KnowledgeAndSkill ks"
          + " INNER JOIN FETCH AssignmentKnowledgeAndSkill aks"
          + " ON aks.knowledgeAndSkill.id = ks.id"
          + " INNER JOIN FETCH Assignment a"
          + " ON a.id = aks.assignment.id"
          + " WHERE a.id = (:assignmentId)")
  List<KnowledgeAndSkill> findAllKnowledgeAndSkillsById(@Param("assignmentId") int assignmentId);
}
