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

@Entity(name = Assignment.ENTITY_NAME)
@Table(name = Assignment.TABLE_NAME, schema = "leo_temp")
public class Assignment {

  public static final String ENTITY_NAME = "Assignment";
  public static final String TABLE_NAME = "assignment";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_SHORTDESCRQUILLZIP_NAME = "short_descr_quill_zip";
  public static final String COLUMN_LONGDESCR_NAME = "long_descr";
  public static final String COLUMN_LONGDESCRQUILLZIP_NAME = "long_descr_quill_zip";

  private Integer id;

  private String name;

  private String shortDescr;

  private byte[] shortDescrQuillZip;

  private String longDescr;

  private byte[] longDescrQuillZip;

  private Class classField;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Assignment setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public Assignment setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCR_NAME, nullable = false, length = 2048)
  public String getShortDescr() {
    return shortDescr;
  }

  public Assignment setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCRQUILLZIP_NAME)
  public byte[] getShortDescrQuillZip() {
    return shortDescrQuillZip;
  }

  public Assignment setShortDescrQuillZip(byte[] shortDescrQuillZip) {
    this.shortDescrQuillZip = shortDescrQuillZip;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCR_NAME, nullable = false)
  public String getLongDescr() {
    return longDescr;
  }

  public Assignment setLongDescr(String longDescr) {
    this.longDescr = longDescr;
    return this;
  }

  @Column(name = COLUMN_LONGDESCRQUILLZIP_NAME)
  public byte[] getLongDescrQuillZip() {
    return longDescrQuillZip;
  }

  public Assignment setLongDescrQuillZip(byte[] longDescrQuillZip) {
    this.longDescrQuillZip = longDescrQuillZip;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "class_id", nullable = false)
  public Class getClassField() {
    return classField;
  }

  public Assignment setClassField(Class classField) {
    this.classField = classField;
    return this;
  }
}
