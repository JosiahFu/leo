package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = Teacher.ENTITY_NAME)
@Table(name = Teacher.TABLE_NAME, schema = "leo_temp")
public class Teacher {

  public static final String ENTITY_NAME = "Teacher";
  public static final String TABLE_NAME = "teacher";
  public static final String COLUMN_ID_NAME = "id";
  public static final String JOINTABLE_SCHOOLS_NAME = "teacher_school";
  public static final String JOINCOLUMNS_JOINCOLUMN_SCHOOLS_NAME = "teacher_id";
  public static final String INVERSEJOINCOLUMNS_JOINCOLUMN_SCHOOLS_NAME = "school_id";
  public static final String JOINTABLE_CLASSFIELDS_NAME = "class_teacher";
  public static final String JOINCOLUMNS_JOINCOLUMN_CLASSFIELDS_NAME = "teacher_id";
  public static final String INVERSEJOINCOLUMNS_JOINCOLUMN_CLASSFIELDS_NAME = "class_id";

  private Integer id;

  private User user;

  private Set<School> schools = new LinkedHashSet<>();

  private Set<Class> classFields = new LinkedHashSet<>();

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

  @OneToOne(mappedBy = "teacher")
  public User getUser() {
    return user;
  }

  public Teacher setUser(User user) {
    this.user = user;
    return this;
  }

  @ManyToMany
  @JoinTable(
      name = JOINTABLE_SCHOOLS_NAME,
      joinColumns = @JoinColumn(name = JOINCOLUMNS_JOINCOLUMN_SCHOOLS_NAME),
      inverseJoinColumns = @JoinColumn(name = INVERSEJOINCOLUMNS_JOINCOLUMN_SCHOOLS_NAME))
  public Set<School> getSchools() {
    return schools;
  }

  public Teacher setSchools(Set<School> schools) {
    this.schools = schools;
    return this;
  }

  @ManyToMany
  @JoinTable(
      name = JOINTABLE_CLASSFIELDS_NAME,
      joinColumns = @JoinColumn(name = JOINCOLUMNS_JOINCOLUMN_CLASSFIELDS_NAME),
      inverseJoinColumns = @JoinColumn(name = INVERSEJOINCOLUMNS_JOINCOLUMN_CLASSFIELDS_NAME))
  public Set<Class> getClassFields() {
    return classFields;
  }

  public Teacher setClassFields(Set<Class> classFields) {
    this.classFields = classFields;
    return this;
  }
}
