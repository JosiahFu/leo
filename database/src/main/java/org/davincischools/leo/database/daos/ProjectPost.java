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
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = ProjectPost.ENTITY_NAME)
@Table(name = ProjectPost.TABLE_NAME, schema = "leo_temp")
public class ProjectPost {

  public static final String ENTITY_NAME = "ProjectPost";
  public static final String TABLE_NAME = "project_post";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_TITLE_NAME = "title";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";
  public static final String COLUMN_POSTTIMEMICROSUTC_NAME = "post_time_micros_utc";

  private Integer id;

  private String title;

  private byte[] shortDescrQuill;

  private byte[] longDescrQuill;

  private Long postTimeMicrosUtc;

  private User user;

  private Set<ProjectPostComment> projectPostComments = new LinkedHashSet<>();

  private Set<PortfolioPost> portfolioPosts = new LinkedHashSet<>();

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

  @Column(name = COLUMN_TITLE_NAME, nullable = false)
  public String getTitle() {
    return title;
  }

  public ProjectPost setTitle(String title) {
    this.title = title;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCRQUILL_NAME, nullable = false)
  public byte[] getShortDescrQuill() {
    return shortDescrQuill;
  }

  public ProjectPost setShortDescrQuill(byte[] shortDescrQuill) {
    this.shortDescrQuill = shortDescrQuill;
    return this;
  }

  @Column(name = COLUMN_LONGDESCRQUILL_NAME, nullable = false)
  public byte[] getLongDescrQuill() {
    return longDescrQuill;
  }

  public ProjectPost setLongDescrQuill(byte[] longDescrQuill) {
    this.longDescrQuill = longDescrQuill;
    return this;
  }

  @Column(name = COLUMN_POSTTIMEMICROSUTC_NAME, nullable = false)
  public Long getPostTimeMicrosUtc() {
    return postTimeMicrosUtc;
  }

  public ProjectPost setPostTimeMicrosUtc(Long postTimeMicrosUtc) {
    this.postTimeMicrosUtc = postTimeMicrosUtc;
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

  @OneToMany(mappedBy = "projectPost")
  public Set<ProjectPostComment> getProjectPostComments() {
    return projectPostComments;
  }

  public ProjectPost setProjectPostComments(Set<ProjectPostComment> projectPostComments) {
    this.projectPostComments = projectPostComments;
    return this;
  }

  @OneToMany(mappedBy = "projectPost")
  public Set<PortfolioPost> getPortfolioPosts() {
    return portfolioPosts;
  }

  public ProjectPost setPortfolioPosts(Set<PortfolioPost> portfolioPosts) {
    this.portfolioPosts = portfolioPosts;
    return this;
  }
}
