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
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = Project.ENTITY_NAME)
@Table(name = Project.TABLE_NAME)
public class Project {

  public static final String ENTITY_NAME = "Project";
  public static final String TABLE_NAME = "projects";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_TITLE_NAME = "title";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_STARTTIMEUTC_NAME = "start_time_utc";
  public static final String COLUMN_LOVE_NAME = "love";
  public static final String COLUMN_NEED_NAME = "need";
  public static final String COLUMN_PAID_NAME = "paid";

  private Long id;

  private String title;

  private String shortDescr;

  private Instant startTimeUtc;

  private String love;

  private String need;

  private String paid;

  private Assignment assignment;

  private Student student;

  private Set<KnowledgeAndSkill> knowledgeAndSkills = new LinkedHashSet<>();

  private Set<ProjectCycle> projectCycles = new LinkedHashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Long getId() {
    return id;
  }

  public Project setId(Long id) {
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

  @Column(name = COLUMN_SHORTDESCR_NAME, nullable = false)
  public String getShortDescr() {
    return shortDescr;
  }

  public Project setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Column(name = COLUMN_STARTTIMEUTC_NAME, nullable = false)
  public Instant getStartTimeUtc() {
    return startTimeUtc;
  }

  public Project setStartTimeUtc(Instant startTimeUtc) {
    this.startTimeUtc = startTimeUtc;
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
  public Set<KnowledgeAndSkill> getKnowledgeAndSkills() {
    return knowledgeAndSkills;
  }

  public Project setKnowledgeAndSkills(Set<KnowledgeAndSkill> knowledgeAndSkills) {
    this.knowledgeAndSkills = knowledgeAndSkills;
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
