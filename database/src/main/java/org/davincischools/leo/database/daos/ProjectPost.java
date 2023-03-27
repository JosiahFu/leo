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
import java.time.Instant;

@Entity(name = ProjectPost.ENTITY_NAME)
@Table(name = ProjectPost.TABLE_NAME)
public class ProjectPost {

  public static final String ENTITY_NAME = "ProjectPost";
  public static final String TABLE_NAME = "project_posts";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_TITLE_NAME = "title";
  public static final String COLUMN_SHORTDESCR_NAME = "short_descr";
  public static final String COLUMN_POSTTIMEUTC_NAME = "post_time_utc";

  private Long id;

  private String title;

  private String shortDescr;

  private Instant postTimeUtc;

  private ProjectCycle projectCycle;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Long getId() {
    return id;
  }

  public ProjectPost setId(Long id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_TITLE_NAME, nullable = false)
  public String getTitle() {
    return title;
  }

  public ProjectPost setTitle(String title) {
    this.title = title;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCR_NAME, nullable = false)
  public String getShortDescr() {
    return shortDescr;
  }

  public ProjectPost setShortDescr(String shortDescr) {
    this.shortDescr = shortDescr;
    return this;
  }

  @Column(name = COLUMN_POSTTIMEUTC_NAME, nullable = false)
  public Instant getPostTimeUtc() {
    return postTimeUtc;
  }

  public ProjectPost setPostTimeUtc(Instant postTimeUtc) {
    this.postTimeUtc = postTimeUtc;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_cycle_id", nullable = false)
  public ProjectCycle getProjectCycle() {
    return projectCycle;
  }

  public ProjectPost setProjectCycle(ProjectCycle projectCycle) {
    this.projectCycle = projectCycle;
    return this;
  }
}
