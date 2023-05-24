package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.Hibernate;

@Embeddable
public class AssignmentProjectDefinitionId implements Serializable {

  public static final String COLUMN_ASSIGNMENTID_NAME = "assignment_id";
  public static final String COLUMN_PROJECTDEFINITIONID_NAME = "project_definition_id";
  private static final long serialVersionUID = -474924656082250321L;

  private Integer assignmentId;

  private Integer projectDefinitionId;

  @Column(name = COLUMN_ASSIGNMENTID_NAME, nullable = false)
  public Integer getAssignmentId() {
    return assignmentId;
  }

  public AssignmentProjectDefinitionId setAssignmentId(Integer assignmentId) {
    this.assignmentId = assignmentId;
    return this;
  }

  @Column(name = COLUMN_PROJECTDEFINITIONID_NAME, nullable = false)
  public Integer getProjectDefinitionId() {
    return projectDefinitionId;
  }

  public AssignmentProjectDefinitionId setProjectDefinitionId(Integer projectDefinitionId) {
    this.projectDefinitionId = projectDefinitionId;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    AssignmentProjectDefinitionId entity = (AssignmentProjectDefinitionId) o;
    return Objects.equals(this.projectDefinitionId, entity.projectDefinitionId)
        && Objects.equals(this.assignmentId, entity.assignmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(projectDefinitionId, assignmentId);
  }
}
