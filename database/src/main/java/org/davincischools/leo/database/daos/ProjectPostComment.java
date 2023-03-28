package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity(name = ProjectPostComment.ENTITY_NAME)
@Table(name = ProjectPostComment.TABLE_NAME, schema = "leo_temp", indexes = {
    @Index(name = "project_post_id", columnList = "project_post_id")
})
public class ProjectPostComment implements Serializable {

  public static final String ENTITY_NAME = "ProjectPostComment";
  public static final String TABLE_NAME = "project_post_comments";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_COMMENTQUILL_NAME = "comment_quill";
  public static final String COLUMN_ORDERINDEX_NAME = "order_index";
  private static final long serialVersionUID = 1658844183243386953L;


  private Integer id;

  private byte[] commentQuill;

  private Integer orderIndex;

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

  @Column(name = COLUMN_COMMENTQUILL_NAME, nullable = false)
  public byte[] getCommentQuill() {
    return commentQuill;
  }

  public ProjectPostComment setCommentQuill(byte[] commentQuill) {
    this.commentQuill = commentQuill;
    return this;
  }

  @Column(name = COLUMN_ORDERINDEX_NAME, nullable = false)
  public Integer getOrderIndex() {
    return orderIndex;
  }

  public ProjectPostComment setOrderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
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