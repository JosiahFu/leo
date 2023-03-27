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

@Entity(name = ProjectCycle.ENTITY_NAME)
@Table(name = ProjectCycle.TABLE_NAME)
public class ProjectCycle {

  public static final String ENTITY_NAME = "ProjectCycle";
  public static final String TABLE_NAME = "project_cycles";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_TITLE_NAME = "title";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_STARTTIMEUTC_NAME = "start_time_utc";

  private Long id;

  private String title;

  private String shortDescr;

  private Instant startTimeUtc;

  private Project project;

  private Set<ProjectPost> projectPosts = new LinkedHashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Long getId() {
    return id;
  }

  public ProjectCycle setId(Long id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_TITLE_NAME, nullable = false)
  public String getTitle() {
    return title;
  }

  public ProjectCycle setTitle(String title) {
    this.title = title;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCR_NAME, nullable = false)
  public String getShortDescr() {
    return shortDescr;
  }

  public ProjectCycle setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Column(name = COLUMN_STARTTIMEUTC_NAME, nullable = false)
  public Instant getStartTimeUtc() {
    return startTimeUtc;
  }

  public ProjectCycle setStartTimeUtc(Instant startTimeUtc) {
    this.startTimeUtc = startTimeUtc;
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

  @OneToMany(mappedBy = "projectCycle")
  public Set<ProjectPost> getProjectPosts() {
    return projectPosts;
  }

  public ProjectCycle setProjectPosts(Set<ProjectPost> projectPosts) {
    this.projectPosts = projectPosts;
    return this;
  }
}
