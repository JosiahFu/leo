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

@Entity(name = ProjectPost.ENTITY_NAME)
@Table(name = ProjectPost.TABLE_NAME, schema = "leo_temp")
public class ProjectPost {

  public static final String ENTITY_NAME = "ProjectPost";
  public static final String TABLE_NAME = "project_post";
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

  private User user;

  private Project project;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectPost setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectPost setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public ProjectPost setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCR_NAME, nullable = false, length = 2048)
  public String getShortDescr() {
    return shortDescr;
  }

  public ProjectPost setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCRQUILLZIP_NAME)
  public byte[] getShortDescrQuillZip() {
    return shortDescrQuillZip;
  }

  public ProjectPost setShortDescrQuillZip(byte[] shortDescrQuillZip) {
    this.shortDescrQuillZip = shortDescrQuillZip;
    return this;
  }

  @Lob
  @Column(name = COLUMN_LONGDESCR_NAME, nullable = false)
  public String getLongDescr() {
    return longDescr;
  }

  public ProjectPost setLongDescr(String longDescr) {
    this.longDescr = longDescr;
    return this;
  }

  @Column(name = COLUMN_LONGDESCRQUILLZIP_NAME)
  public byte[] getLongDescrQuillZip() {
    return longDescrQuillZip;
  }

  public ProjectPost setLongDescrQuillZip(byte[] longDescrQuillZip) {
    this.longDescrQuillZip = longDescrQuillZip;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  public User getUser() {
    return user;
  }

  public ProjectPost setUser(User user) {
    this.user = user;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  public Project getProject() {
    return project;
  }

  public ProjectPost setProject(Project project) {
    this.project = project;
    return this;
  }
}
