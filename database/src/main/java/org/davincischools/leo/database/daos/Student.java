package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = Student.ENTITY_NAME)
@Table(name = Student.TABLE_NAME, schema = "leo_temp")
public class Student {

  public static final String ENTITY_NAME = "Student";
  public static final String TABLE_NAME = "student";
  public static final String COLUMN_ID_NAME = "id";
  public static final String JOINTABLE_CLASSFIELDS_NAME = "class_student";
  public static final String JOINCOLUMNS_JOINCOLUMN_CLASSFIELDS_NAME = "student_id";
  public static final String INVERSEJOINCOLUMNS_JOINCOLUMN_CLASSFIELDS_NAME = "class_id";

  private Integer id;

  private Set<Project> projects = new LinkedHashSet<>();

  private Set<Class> classFields = new LinkedHashSet<>();

  private User user;

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

  @OneToMany(mappedBy = "student")
  public Set<Project> getProjects() {
    return projects;
  }

  public Student setProjects(Set<Project> projects) {
    this.projects = projects;
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

  public Student setClassFields(Set<Class> classFields) {
    this.classFields = classFields;
    return this;
  }

  @OneToOne(mappedBy = "student")
  public User getUser() {
    return user;
  }

  public Student setUser(User user) {
    this.user = user;
    return this;
  }
}
