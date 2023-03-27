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
@Table(name = KnowledgeAndSkill.TABLE_NAME)
public class KnowledgeAndSkill {

  public static final String ENTITY_NAME = "KnowledgeAndSkill";
  public static final String TABLE_NAME = "knowledge_and_skills";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_TITLE_NAME = "title";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_MASTERY_NAME = "mastery";

  private Long id;

  private String title;

  private String shortDescr;

  private String mastery;

  private Project project;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Long getId() {
    return id;
  }

  public KnowledgeAndSkill setId(Long id) {
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

  @Column(name = COLUMN_SHORTDESCR_NAME, nullable = false)
  public String getShortDescr() {
    return shortDescr;
  }

  public KnowledgeAndSkill setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
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
  @JoinColumn(name = "project_id", nullable = false)
  public Project getProject() {
    return project;
  }

  public KnowledgeAndSkill setProject(Project project) {
    this.project = project;
    return this;
  }
}
