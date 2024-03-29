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

@Entity(name = Project.ENTITY_NAME)
@Table(name = Project.TABLE_NAME, schema = "leo_temp")
public class Project {

  public static final String ENTITY_NAME = "Project";
  public static final String TABLE_NAME = "project";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";
  public static final String COLUMN_LOVE_NAME = "love";
  public static final String COLUMN_NEED_NAME = "need";
  public static final String COLUMN_PAID_NAME = "paid";
  public static final String COLUMN_STARTTIMEMICROSUTC_NAME = "start_time_micros_utc";

  private Integer id;

  private String name;

  private byte[] shortDescrQuill;

  private byte[] longDescrQuill;

  private String love;

  private String need;

  private String paid;

  private Long startTimeMicrosUtc;

  private Assignment assignment;

  private Student student;

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

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public Project setName(String name) {
    this.name = name;
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
}
