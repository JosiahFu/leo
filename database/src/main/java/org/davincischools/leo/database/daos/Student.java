package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = Student.ENTITY_NAME)
@Table(name = Student.TABLE_NAME)
public class Student {

  public static final String ENTITY_NAME = "Student";
  public static final String TABLE_NAME = "students";
  public static final String COLUMN_ID_NAME = "id";
  public static final String JOINTABLE_CLASSES_NAME = "classes_students";
  public static final String JOINCOLUMNS_JOINCOLUMN_CLASSES_NAME = "student_id";
  public static final String INVERSEJOINCOLUMNS_JOINCOLUMN_CLASSES_NAME = "class_id";

  private Long id;

  private District district;

  private Set<Project> projects = new LinkedHashSet<>();

  private Set<Class> classes = new LinkedHashSet<>();

  private User user;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Long getId() {
    return id;
  }

  public Student setId(Long id) {
    this.id = id;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "district_id", nullable = false)
  public District getDistrict() {
    return district;
  }

  public Student setDistrict(District district) {
    this.district = district;
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
      name = JOINTABLE_CLASSES_NAME,
      joinColumns = @JoinColumn(name = JOINCOLUMNS_JOINCOLUMN_CLASSES_NAME),
      inverseJoinColumns = @JoinColumn(name = INVERSEJOINCOLUMNS_JOINCOLUMN_CLASSES_NAME))
  public Set<Class> getClasses() {
    return classes;
  }

  public Student setClasses(Set<Class> classes) {
    this.classes = classes;
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
