package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = Teacher.ENTITY_NAME)
@Table(name = Teacher.TABLE_NAME, schema = "leo_temp")
public class Teacher {

  public static final String ENTITY_NAME = "Teacher";
  public static final String TABLE_NAME = "teacher";
  public static final String COLUMN_ID_NAME = "id";

  private Integer id;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Teacher setId(Integer id) {
    this.id = id;
    return this;
  }

  // TODO [JPA Buddy] generate columns from DB
}
