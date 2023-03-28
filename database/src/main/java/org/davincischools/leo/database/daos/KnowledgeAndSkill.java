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

@Entity(name = KnowledgeAndSkill.ENTITY_NAME)
@Table(name = KnowledgeAndSkill.TABLE_NAME, schema = "leo_temp")
public class KnowledgeAndSkill {

  public static final String ENTITY_NAME = "KnowledgeAndSkill";
  public static final String TABLE_NAME = "knowledge_and_skill";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_TITLE_NAME = "title";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";
  public static final String COLUMN_MASTERY_NAME = "mastery";

  private Integer id;

  private String title;

  private byte[] shortDescrQuill;

  private byte[] longDescrQuill;

  private String mastery;

  private Class classField;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public KnowledgeAndSkill setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_TITLE_NAME, nullable = false)
  public String getTitle() {
    return title;
  }

  public KnowledgeAndSkill setTitle(String title) {
    this.title = title;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCRQUILL_NAME, nullable = false)
  public byte[] getShortDescrQuill() {
    return shortDescrQuill;
  }

  public KnowledgeAndSkill setShortDescrQuill(byte[] shortDescrQuill) {
    this.shortDescrQuill = shortDescrQuill;
    return this;
  }

  @Column(name = COLUMN_LONGDESCRQUILL_NAME, nullable = false)
  public byte[] getLongDescrQuill() {
    return longDescrQuill;
  }

  public KnowledgeAndSkill setLongDescrQuill(byte[] longDescrQuill) {
    this.longDescrQuill = longDescrQuill;
    return this;
  }

  @Column(name = COLUMN_MASTERY_NAME, nullable = false, length = 1024)
  public String getMastery() {
    return mastery;
  }

  public KnowledgeAndSkill setMastery(String mastery) {
    this.mastery = mastery;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_id", nullable = false)
  public Class getClassField() {
    return classField;
  }

  public KnowledgeAndSkill setClassField(Class classField) {
    this.classField = classField;
    return this;
  }
}
