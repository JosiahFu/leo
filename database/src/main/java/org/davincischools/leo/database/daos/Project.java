package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity(name = Project.ENTITY_NAME)
@Table(name = Project.TABLE_NAME, schema = "leo_temp")
public class Project {

  public static final String ENTITY_NAME = "Project";
  public static final String TABLE_NAME = "project";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_SHORTDESCRQUILLZIP_NAME = "short_descr_quill_zip";
  public static final String COLUMN_LONGDESCR_NAME = "long_descr";
  public static final String COLUMN_LONGDESCRQUILLZIP_NAME = "long_descr_quill_zip";
  public static final String COLUMN_LOVEDESCR_NAME = "love_descr";
  public static final String COLUMN_LOVEDESCRQUILLZIP_NAME = "love_descr_quill_zip";
  public static final String COLUMN_WORLDNEEDSDESCR_NAME = "world_needs_descr";
  public static final String COLUMN_WORLDNEEDSDESCRQUILLZIP_NAME = "world_needs_descr_quill_zip";
  public static final String COLUMN_PAIDFORDESCR_NAME = "paid_for_descr";
  public static final String COLUMN_PAIDFORDESCRQUILLZIP_NAME = "paid_for_descr_quill_zip";
  public static final String COLUMN_GOODATDESCR_NAME = "good_at_descr";
  public static final String COLUMN_GOODATDESCRQUILLZIP_NAME = "good_at_descr_quill_zip";

  private Integer id;

  private Instant creationTime;

  private String name;

  private String shortDescr;

  private byte[] shortDescrQuillZip;

  private String longDescr;

  private byte[] longDescrQuillZip;

  private String loveDescr;

  private byte[] loveDescrQuillZip;

  private String worldNeedsDescr;

  private byte[] worldNeedsDescrQuillZip;

  private String paidForDescr;

  private byte[] paidForDescrQuillZip;

  private String goodAtDescr;

  private byte[] goodAtDescrQuillZip;

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

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public Project setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
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

  @Column(name = COLUMN_SHORTDESCR_NAME, nullable = false, length = 2048)
  public String getShortDescr() {
    return shortDescr;
  }

  public Project setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCRQUILLZIP_NAME)
  public byte[] getShortDescrQuillZip() {
    return shortDescrQuillZip;
  }

  public Project setShortDescrQuillZip(byte[] shortDescrQuillZip) {
    this.shortDescrQuillZip = shortDescrQuillZip;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCR_NAME, nullable = false)
  public String getLongDescr() {
    return longDescr;
  }

  public Project setLongDescr(String longDescr) {
    this.longDescr = longDescr;
    return this;
  }

  @Column(name = COLUMN_LONGDESCRQUILLZIP_NAME)
  public byte[] getLongDescrQuillZip() {
    return longDescrQuillZip;
  }

  public Project setLongDescrQuillZip(byte[] longDescrQuillZip) {
    this.longDescrQuillZip = longDescrQuillZip;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LOVEDESCR_NAME, nullable = false)
  public String getLoveDescr() {
    return loveDescr;
  }

  public Project setLoveDescr(String loveDescr) {
    this.loveDescr = loveDescr;
    return this;
  }

  @Column(name = COLUMN_LOVEDESCRQUILLZIP_NAME)
  public byte[] getLoveDescrQuillZip() {
    return loveDescrQuillZip;
  }

  public Project setLoveDescrQuillZip(byte[] loveDescrQuillZip) {
    this.loveDescrQuillZip = loveDescrQuillZip;
    return this;
  }

  @Lob
  @Column(name = COLUMN_WORLDNEEDSDESCR_NAME, nullable = false)
  public String getWorldNeedsDescr() {
    return worldNeedsDescr;
  }

  public Project setWorldNeedsDescr(String worldNeedsDescr) {
    this.worldNeedsDescr = worldNeedsDescr;
    return this;
  }

  @Column(name = COLUMN_WORLDNEEDSDESCRQUILLZIP_NAME)
  public byte[] getWorldNeedsDescrQuillZip() {
    return worldNeedsDescrQuillZip;
  }

  public Project setWorldNeedsDescrQuillZip(byte[] worldNeedsDescrQuillZip) {
    this.worldNeedsDescrQuillZip = worldNeedsDescrQuillZip;
    return this;
  }

  @Lob
  @Column(name = COLUMN_PAIDFORDESCR_NAME, nullable = false)
  public String getPaidForDescr() {
    return paidForDescr;
  }

  public Project setPaidForDescr(String paidForDescr) {
    this.paidForDescr = paidForDescr;
    return this;
  }

  @Column(name = COLUMN_PAIDFORDESCRQUILLZIP_NAME)
  public byte[] getPaidForDescrQuillZip() {
    return paidForDescrQuillZip;
  }

  public Project setPaidForDescrQuillZip(byte[] paidForDescrQuillZip) {
    this.paidForDescrQuillZip = paidForDescrQuillZip;
    return this;
  }

  @Lob
  @Column(name = COLUMN_GOODATDESCR_NAME, nullable = false)
  public String getGoodAtDescr() {
    return goodAtDescr;
  }

  public Project setGoodAtDescr(String goodAtDescr) {
    this.goodAtDescr = goodAtDescr;
    return this;
  }

  @Column(name = COLUMN_GOODATDESCRQUILLZIP_NAME)
  public byte[] getGoodAtDescrQuillZip() {
    return goodAtDescrQuillZip;
  }

  public Project setGoodAtDescrQuillZip(byte[] goodAtDescrQuillZip) {
    this.goodAtDescrQuillZip = goodAtDescrQuillZip;
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
