package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = District.ENTITY_NAME)
@Table(name = District.TABLE_NAME, schema = "leo_temp")
public class District {

  public static final String ENTITY_NAME = "District";
  public static final String TABLE_NAME = "district";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_NAME_NAME = "name";

  private Integer id;

  private String name;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public District setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public District setName(String name) {
    this.name = name;
    return this;
  }
}
