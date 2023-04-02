package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = Student.ENTITY_NAME)
@Table(name = Student.TABLE_NAME, schema = "leo_temp")
public class Student {

  public static final String ENTITY_NAME = "Student";
  public static final String TABLE_NAME = "student";
  public static final String COLUMN_ID_NAME = "id";

  private Integer id;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Student setId(Integer id) {
    this.id = id;
    return this;
  }

  // TODO [JPA Buddy] generate columns from DB
}
