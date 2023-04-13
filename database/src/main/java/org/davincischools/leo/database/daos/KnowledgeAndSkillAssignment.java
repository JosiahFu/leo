package org.davincischools.leo.database.daos;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity(name = KnowledgeAndSkillAssignment.ENTITY_NAME)
@Table(name = KnowledgeAndSkillAssignment.TABLE_NAME, schema = "leo_temp")
public class KnowledgeAndSkillAssignment {

  public static final String ENTITY_NAME = "KnowledgeAndSkillAssignment";
  public static final String TABLE_NAME = "knowledge_and_skill_assignment";

  private KnowledgeAndSkillAssignmentId id;

  private KnowledgeAndSkill knowledgeAndSkill;

  private Assignment assignment;

  @EmbeddedId
  public KnowledgeAndSkillAssignmentId getId() {
    return id;
  }

  public KnowledgeAndSkillAssignment setId(KnowledgeAndSkillAssignmentId id) {
    this.id = id;
    return this;
  }

  @MapsId("knowledgeAndSkillId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "knowledge_and_skill_id", nullable = false)
  public KnowledgeAndSkill getKnowledgeAndSkill() {
    return knowledgeAndSkill;
  }

  public KnowledgeAndSkillAssignment setKnowledgeAndSkill(KnowledgeAndSkill knowledgeAndSkill) {
    this.knowledgeAndSkill = knowledgeAndSkill;
    return this;
  }

  @MapsId("assignmentId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  public Assignment getAssignment() {
    return assignment;
  }

  public KnowledgeAndSkillAssignment setAssignment(Assignment assignment) {
    this.assignment = assignment;
    return this;
  }
}
