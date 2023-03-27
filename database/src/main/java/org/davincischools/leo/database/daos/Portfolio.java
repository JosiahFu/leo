package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = Portfolio.ENTITY_NAME)
@Table(name = Portfolio.TABLE_NAME)
public class Portfolio {

  public static final String ENTITY_NAME = "Portfolio";
  public static final String TABLE_NAME = "portfolios";
  public static final String COLUMN_ID_NAME = "id";

  private Long id;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Long getId() {
    return id;
  }

  public Portfolio setId(Long id) {
    this.id = id;
    return this;
  }

  // TODO [JPA Buddy] generate columns from DB
}
