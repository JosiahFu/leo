package org.davincischools.leo.database.utils.repos;

import java.time.Instant;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkill;
import org.davincischools.leo.database.daos.AssignmentKnowledgeAndSkillId;
import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentKnowledgeAndSkillRepository
    extends JpaRepository<AssignmentKnowledgeAndSkill, AssignmentKnowledgeAndSkillId> {

  default AssignmentKnowledgeAndSkillId createAssignmentKnowledgeAndSkillId(
      KnowledgeAndSkill knowledgeAndSkill, Assignment assignment) {
    return new AssignmentKnowledgeAndSkillId()
        .setKnowledgeAndSkillId(knowledgeAndSkill.getId())
        .setAssignmentId(assignment.getId());
  }

  default AssignmentKnowledgeAndSkill createAssignmentKnowledgeAndSkill(
      KnowledgeAndSkill knowledgeAndSkill, Assignment assignment) {
    return new AssignmentKnowledgeAndSkill()
        .setCreationTime(Instant.now())
        .setId(createAssignmentKnowledgeAndSkillId(knowledgeAndSkill, assignment))
        .setKnowledgeAndSkill(knowledgeAndSkill)
        .setAssignment(assignment);
  }
}
