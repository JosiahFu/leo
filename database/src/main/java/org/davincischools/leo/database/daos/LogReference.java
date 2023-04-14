package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity(name = LogReference.ENTITY_NAME)
@Table(name = LogReference.TABLE_NAME, schema = "leo_temp")
public class LogReference {

  public static final String ENTITY_NAME = "LogReference";
  public static final String TABLE_NAME = "log_reference";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_LOGID_NAME = "log_id";
  public static final String COLUMN_IKIGAIINPUTID_NAME = "ikigai_input_id";
  public static final String COLUMN_PROJECTID_NAME = "project_id";

  private Integer id;

  private Instant creationTime;

  private Integer logId;

  private Integer ikigaiInputId;

  private Integer projectId;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public LogReference setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public LogReference setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_LOGID_NAME, nullable = false)
  public Integer getLogId() {
    return logId;
  }

  public LogReference setLogId(Integer logId) {
    this.logId = logId;
    return this;
  }

  @Column(name = COLUMN_IKIGAIINPUTID_NAME)
  public Integer getIkigaiInputId() {
    return ikigaiInputId;
  }

  public LogReference setIkigaiInputId(Integer ikigaiInputId) {
    this.ikigaiInputId = ikigaiInputId;
    return this;
  }

  @Column(name = COLUMN_PROJECTID_NAME)
  public Integer getProjectId() {
    return projectId;
  }

  public LogReference setProjectId(Integer projectId) {
    this.projectId = projectId;
    return this;
  }
}
