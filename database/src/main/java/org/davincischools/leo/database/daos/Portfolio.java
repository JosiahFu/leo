package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = Portfolio.ENTITY_NAME)
@Table(name = Portfolio.TABLE_NAME, schema = "leo_temp")
public class Portfolio {

  public static final String ENTITY_NAME = "Portfolio";
  public static final String TABLE_NAME = "portfolio";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_SHORTDESCRQUILL_NAME = "short_descr_quill";
  public static final String COLUMN_LONGDESCRQUILL_NAME = "long_descr_quill";

  private Integer id;

  private String name;

  private byte[] shortDescrQuill;

  private byte[] longDescrQuill;

  private Set<PortfolioPost> portfolioPosts = new LinkedHashSet<>();

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

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public Portfolio setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_SHORTDESCRQUILL_NAME, nullable = false)
  public byte[] getShortDescrQuill() {
    return shortDescrQuill;
  }

  public Portfolio setShortDescrQuill(byte[] shortDescrQuill) {
    this.shortDescrQuill = shortDescrQuill;
    return this;
  }

  @Column(name = COLUMN_LONGDESCRQUILL_NAME, nullable = false)
  public byte[] getLongDescrQuill() {
    return longDescrQuill;
  }

  public Portfolio setLongDescrQuill(byte[] longDescrQuill) {
    this.longDescrQuill = longDescrQuill;
    return this;
  }

  @OneToMany(mappedBy = "portfolio")
  public Set<PortfolioPost> getPortfolioPosts() {
    return portfolioPosts;
  }

  public Portfolio setPortfolioPosts(Set<PortfolioPost> portfolioPosts) {
    this.portfolioPosts = portfolioPosts;
    return this;
  }
}
