package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity(name = Portfolio.ENTITY_NAME)
@Table(name = Portfolio.TABLE_NAME, schema = "leo_temp")
public class Portfolio {

  public static final String ENTITY_NAME = "Portfolio";
  public static final String TABLE_NAME = "portfolio";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_SHORTDESCRQUILLZIP_NAME = "short_descr_quill_zip";
  public static final String COLUMN_LONGDESCR_NAME = "long_descr";
  public static final String COLUMN_LONGDESCRQUILLZIP_NAME = "long_descr_quill_zip";

  private Integer id;

  private Instant creationTime;

  private String name;

  private String shortDescr;

  private byte[] shortDescrQuillZip;

  private String longDescr;

  private byte[] longDescrQuillZip;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Portfolio setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public Portfolio setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public Portfolio setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCR_NAME, nullable = false, length = 2048)
  public String getShortDescr() {
    return shortDescr;
  }

  public Portfolio setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCRQUILLZIP_NAME)
  public byte[] getShortDescrQuillZip() {
    return shortDescrQuillZip;
  }

  public Portfolio setShortDescrQuillZip(byte[] shortDescrQuillZip) {
    this.shortDescrQuillZip = shortDescrQuillZip;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCR_NAME, nullable = false)
  public String getLongDescr() {
    return longDescr;
  }

  public Portfolio setLongDescr(String longDescr) {
    this.longDescr = longDescr;
    return this;
  }

  @Column(name = COLUMN_LONGDESCRQUILLZIP_NAME)
  public byte[] getLongDescrQuillZip() {
    return longDescrQuillZip;
  }

  public Portfolio setLongDescrQuillZip(byte[] longDescrQuillZip) {
    this.longDescrQuillZip = longDescrQuillZip;
    return this;
  }
}
