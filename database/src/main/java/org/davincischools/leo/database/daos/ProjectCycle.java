package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = ProjectCycle.ENTITY_NAME)
@Table(name = ProjectCycle.TABLE_NAME, schema = "leo_temp")
public class ProjectCycle {

  public static final String ENTITY_NAME = "ProjectCycle";
  public static final String TABLE_NAME = "project_cycle";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";
  public static final String COLUMN_STARTTIMEMICROSUTC_NAME = "start_time_micros_utc";

  private Integer id;

  private String name;

  private byte[] shortDescrQuill;

  private byte[] longDescrQuill;

  private Long startTimeMicrosUtc;

  private Project project;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectCycle setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public ProjectCycle setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCRQUILL_NAME, nullable = false)
  public byte[] getShortDescrQuill() {
    return shortDescrQuill;
  }

  public ProjectCycle setShortDescrQuill(byte[] shortDescrQuill) {
    this.shortDescrQuill = shortDescrQuill;
    return this;
  }

  @Column(name = COLUMN_LONGDESCRQUILL_NAME, nullable = false)
  public byte[] getLongDescrQuill() {
    return longDescrQuill;
  }

  public ProjectCycle setLongDescrQuill(byte[] longDescrQuill) {
    this.longDescrQuill = longDescrQuill;
    return this;
  }

  @Column(name = COLUMN_STARTTIMEMICROSUTC_NAME, nullable = false)
  public Long getStartTimeMicrosUtc() {
    return startTimeMicrosUtc;
  }

  public ProjectCycle setStartTimeMicrosUtc(Long startTimeMicrosUtc) {
    this.startTimeMicrosUtc = startTimeMicrosUtc;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  public Project getProject() {
    return project;
  }

  public ProjectCycle setProject(Project project) {
    this.project = project;
    return this;
  }
}
