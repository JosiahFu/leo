package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class KnowledgeAndSkillAssignmentId implements Serializable {

  public static final String COLUMN_KNOWLEDGEANDSKILLID_NAME = "knowledge_and_skill_id";
  public static final String COLUMN_ASSIGNMENTID_NAME = "assignment_id";
  private static final long serialVersionUID = 7542218027963944365L;

  private Integer knowledgeAndSkillId;

  private Integer assignmentId;

  @Column(name = COLUMN_KNOWLEDGEANDSKILLID_NAME, nullable = false)
  public Integer getKnowledgeAndSkillId() {
    return knowledgeAndSkillId;
  }

  public KnowledgeAndSkillAssignmentId setKnowledgeAndSkillId(Integer knowledgeAndSkillId) {
    this.knowledgeAndSkillId = knowledgeAndSkillId;
    return this;
  }

  @Column(name = COLUMN_ASSIGNMENTID_NAME, nullable = false)
  public Integer getAssignmentId() {
    return assignmentId;
  }

  public KnowledgeAndSkillAssignmentId setAssignmentId(Integer assignmentId) {
    this.assignmentId = assignmentId;
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
    KnowledgeAndSkillAssignmentId entity = (KnowledgeAndSkillAssignmentId) o;
    return Objects.equals(this.knowledgeAndSkillId, entity.knowledgeAndSkillId)
        && Objects.equals(this.assignmentId, entity.assignmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(knowledgeAndSkillId, assignmentId);
  }
}
