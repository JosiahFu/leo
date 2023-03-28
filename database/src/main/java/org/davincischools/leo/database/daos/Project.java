package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = Project.ENTITY_NAME)
@Table(name = Project.TABLE_NAME, schema = "leo_temp")
public class Project implements Serializable {

  public static final String ENTITY_NAME = "Project";
  public static final String TABLE_NAME = "projects";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_TITLE_NAME = "title";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";
  public static final String COLUMN_LOVE_NAME = "love";
  public static final String COLUMN_NEED_NAME = "need";
  public static final String COLUMN_PAID_NAME = "paid";
  public static final String COLUMN_STARTTIMEMICROSUTC_NAME = "start_time_micros_utc";
  private static final long serialVersionUID = -5262263287519406971L;


  private Integer id;

  private String title;

  private byte[] shortDescrQuill;

  private byte[] longDescrQuill;

  private String love;

  private String need;

  private String paid;

  private Long startTimeMicrosUtc;

  private Assignment assignment;

  private Student student;

  private Set<ProjectCycle> projectCycles = new LinkedHashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Project setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_TITLE_NAME, nullable = false)
  public String getTitle() {
    return title;
  }

  public Project setTitle(String title) {
    this.title = title;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCRQUILL_NAME, nullable = false)
  public byte[] getShortDescrQuill() {
    return shortDescrQuill;
  }

  public Project setShortDescrQuill(byte[] shortDescrQuill) {
    this.shortDescrQuill = shortDescrQuill;
    return this;
  }

  @Column(name = COLUMN_LONGDESCRQUILL_NAME, nullable = false)
  public byte[] getLongDescrQuill() {
    return longDescrQuill;
  }

  public Project setLongDescrQuill(byte[] longDescrQuill) {
    this.longDescrQuill = longDescrQuill;
    return this;
  }

  @Column(name = COLUMN_LOVE_NAME, nullable = false)
  public String getLove() {
    return love;
  }

  public Project setLove(String love) {
    this.love = love;
    return this;
  }

  @Column(name = COLUMN_NEED_NAME, nullable = false)
  public String getNeed() {
    return need;
  }

  public Project setNeed(String need) {
    this.need = need;
    return this;
  }

  @Column(name = COLUMN_PAID_NAME, nullable = false)
  public String getPaid() {
    return paid;
  }

  public Project setPaid(String paid) {
    this.paid = paid;
    return this;
  }

  @Column(name = COLUMN_STARTTIMEMICROSUTC_NAME, nullable = false)
  public Long getStartTimeMicrosUtc() {
    return startTimeMicrosUtc;
  }

  public Project setStartTimeMicrosUtc(Long startTimeMicrosUtc) {
    this.startTimeMicrosUtc = startTimeMicrosUtc;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  public Assignment getAssignment() {
    return assignment;
  }

  public Project setAssignment(Assignment assignment) {
    this.assignment = assignment;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "student_id", nullable = false)
  public Student getStudent() {
    return student;
  }

  public Project setStudent(Student student) {
    this.student = student;
    return this;
  }

  @OneToMany(mappedBy = "project")
  public Set<ProjectCycle> getProjectCycles() {
    return projectCycles;
  }

  public Project setProjectCycles(Set<ProjectCycle> projectCycles) {
    this.projectCycles = projectCycles;
    return this;
  }

}