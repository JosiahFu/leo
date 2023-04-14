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

@Entity(name = ProjectPostComment.ENTITY_NAME)
@Table(name = ProjectPostComment.TABLE_NAME, schema = "leo_temp")
public class ProjectPostComment {

  public static final String ENTITY_NAME = "ProjectPostComment";
  public static final String TABLE_NAME = "project_post_comment";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_CREATIONTIME_NAME = "creation_time";
  public static final String COLUMN_COMMENT_NAME = "comment";
  public static final String COLUMN_COMMENTQUILLZIP_NAME = "comment_quill_zip";

  private Integer id;

  private Instant creationTime;

  private String comment;

  private byte[] commentQuillZip;

  private User user;

  private ProjectPost projectPost;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public ProjectPostComment setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_CREATIONTIME_NAME, nullable = false)
  public Instant getCreationTime() {
    return creationTime;
  }

  public ProjectPostComment setCreationTime(Instant creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  @Lob
  @Column(name = COLUMN_COMMENT_NAME, nullable = false)
  public String getComment() {
    return comment;
  }

  public ProjectPostComment setComment(String comment) {
    this.comment = comment;
    return this;
  }

  @Column(name = COLUMN_COMMENTQUILLZIP_NAME)
  public byte[] getCommentQuillZip() {
    return commentQuillZip;
  }

  public ProjectPostComment setCommentQuillZip(byte[] commentQuillZip) {
    this.commentQuillZip = commentQuillZip;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  public User getUser() {
    return user;
  }

  public ProjectPostComment setUser(User user) {
    this.user = user;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_post_id", nullable = false)
  public ProjectPost getProjectPost() {
    return projectPost;
  }

  public ProjectPostComment setProjectPost(ProjectPost projectPost) {
    this.projectPost = projectPost;
    return this;
  }
}
