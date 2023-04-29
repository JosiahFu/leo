package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class AssignmentKnowledgeAndSkillId implements Serializable {

  public static final String COLUMN_ASSIGNMENTID_NAME = "assignment_id";
  public static final String COLUMN_KNOWLEDGEANDSKILLID_NAME = "knowledge_and_skill_id";
  private static final long serialVersionUID = 5010769700601342204L;

  private Integer assignmentId;

  private Integer knowledgeAndSkillId;

  @Column(name = COLUMN_ASSIGNMENTID_NAME, nullable = false)
  public Integer getAssignmentId() {
    return assignmentId;
  }

  public AssignmentKnowledgeAndSkillId setAssignmentId(Integer assignmentId) {
    this.assignmentId = assignmentId;
    return this;
  }

  @Column(name = COLUMN_KNOWLEDGEANDSKILLID_NAME, nullable = false)
  public Integer getKnowledgeAndSkillId() {
    return knowledgeAndSkillId;
  }

  public AssignmentKnowledgeAndSkillId setKnowledgeAndSkillId(Integer knowledgeAndSkillId) {
    this.knowledgeAndSkillId = knowledgeAndSkillId;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    AssignmentKnowledgeAndSkillId entity = (AssignmentKnowledgeAndSkillId) o;
    return Objects.equals(this.knowledgeAndSkillId, entity.knowledgeAndSkillId)
        && Objects.equals(this.assignmentId, entity.assignmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(knowledgeAndSkillId, assignmentId);
  }
}
