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

@Entity(name = PortfolioPost.ENTITY_NAME)
@Table(name = PortfolioPost.TABLE_NAME, schema = "leo_temp")
public class PortfolioPost {

  public static final String ENTITY_NAME = "PortfolioPost";
  public static final String TABLE_NAME = "portfolio_post";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";
  public static final String COLUMN_ORDERINDEX_NAME = "order_index";

  private Integer id;

  private String name;

  private byte[] shortDescrQuill;

  private byte[] longDescrQuill;

  private Integer orderIndex;

  private Portfolio portfolio;

  private ProjectPost projectPost;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public PortfolioPost setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public PortfolioPost setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCRQUILL_NAME, nullable = false)
  public byte[] getShortDescrQuill() {
    return shortDescrQuill;
  }

  public PortfolioPost setShortDescrQuill(byte[] shortDescrQuill) {
    this.shortDescrQuill = shortDescrQuill;
    return this;
  }

  @Column(name = COLUMN_LONGDESCRQUILL_NAME, nullable = false)
  public byte[] getLongDescrQuill() {
    return longDescrQuill;
  }

  public PortfolioPost setLongDescrQuill(byte[] longDescrQuill) {
    this.longDescrQuill = longDescrQuill;
    return this;
  }

  @Column(name = COLUMN_ORDERINDEX_NAME, nullable = false)
  public Integer getOrderIndex() {
    return orderIndex;
  }

  public PortfolioPost setOrderIndex(Integer orderIndex) {
    this.orderIndex = orderIndex;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "portfolio_id", nullable = false)
  public Portfolio getPortfolio() {
    return portfolio;
  }

  public PortfolioPost setPortfolio(Portfolio portfolio) {
    this.portfolio = portfolio;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "project_post_id", nullable = false)
  public ProjectPost getProjectPost() {
    return projectPost;
  }

  public PortfolioPost setProjectPost(ProjectPost projectPost) {
    this.projectPost = projectPost;
    return this;
  }
}
