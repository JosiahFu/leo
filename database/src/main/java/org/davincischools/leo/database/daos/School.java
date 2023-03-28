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
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = School.ENTITY_NAME)
@Table(name = School.TABLE_NAME, schema = "leo_temp")
public class School implements Serializable {

  public static final String ENTITY_NAME = "School";
  public static final String TABLE_NAME = "schools";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_SCHOOL_NAME = "school";
  public static final String COLUMN_CITY_NAME = "city";
  public static final String JOINTABLE_TEACHERS_NAME = "teachers_schools";
  public static final String JOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME = "school_id";
  public static final String INVERSEJOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME = "teacher_id";
  private static final long serialVersionUID = -6786946202509475457L;


  private Integer id;

  private String school;

  private String city;

  private District district;

  private Set<Teacher> teachers = new LinkedHashSet<>();

  private Set<Class> classes = new LinkedHashSet<>();

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

  @Column(name = COLUMN_SCHOOL_NAME, nullable = false)
  public String getSchool() {
    return school;
  }

  public School setSchool(String school) {
    this.school = school;
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

  @ManyToMany
  @JoinTable(name = JOINTABLE_TEACHERS_NAME,
      joinColumns = @JoinColumn(name = JOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME),
      inverseJoinColumns = @JoinColumn(name = INVERSEJOINCOLUMNS_JOINCOLUMN_TEACHERS_NAME))
  public Set<Teacher> getTeachers() {
    return teachers;
  }

  public School setTeachers(Set<Teacher> teachers) {
    this.teachers = teachers;
    return this;
  }

  @OneToMany(mappedBy = "school")
  public Set<Class> getClasses() {
    return classes;
  }

  public School setClasses(Set<Class> classes) {
    this.classes = classes;
    return this;
  }

}