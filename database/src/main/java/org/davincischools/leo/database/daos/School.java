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
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = School.ENTITY_NAME)
@Table(name = School.TABLE_NAME, schema = "leo_temp")
public class School {

  public static final String ENTITY_NAME = "School";
  public static final String TABLE_NAME = "school";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_NAME_NAME = "name";
  public static final String COLUMN_CITY_NAME = "city";
  public static final String JOINTABLE_TEACHERS_NAME = "teacher_school";
  public static final String JOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME = "school_id";
  public static final String INVERSEJOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME = "teacher_id";

  private Integer id;

  private String name;

  private String city;

  private District district;

  private Set<Class> classFields = new LinkedHashSet<>();

  private Set<Teacher> teachers = new LinkedHashSet<>();

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public School setId(Integer id) {
    this.id = id;
    return this;
  }

  @Column(name = COLUMN_NAME_NAME, nullable = false)
  public String getName() {
    return name;
  }

  public School setName(String name) {
    this.name = name;
    return this;
  }

  @Column(name = COLUMN_CITY_NAME, nullable = false)
  public String getCity() {
    return city;
  }

  public School setCity(String city) {
    this.city = city;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "district_id", nullable = false)
  public District getDistrict() {
    return district;
  }

  public School setDistrict(District district) {
    this.district = district;
    return this;
  }

  @OneToMany(mappedBy = "school")
  public Set<Class> getClassFields() {
    return classFields;
  }

  public School setClassFields(Set<Class> classFields) {
    this.classFields = classFields;
    return this;
  }

  @ManyToMany
  @JoinTable(
      name = JOINTABLE_TEACHERS_NAME,
      joinColumns = @JoinColumn(name = JOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME),
      inverseJoinColumns = @JoinColumn(name = INVERSEJOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME))
  public Set<Teacher> getTeachers() {
    return teachers;
  }

  public School setTeachers(Set<Teacher> teachers) {
    this.teachers = teachers;
    return this;
  }
}
