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

@Entity(name = District.ENTITY_NAME)
@Table(name = District.TABLE_NAME, schema = "leo_temp")
public class District {

  public static final String ENTITY_NAME = "District";
  public static final String TABLE_NAME = "district";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_DISTRICT_NAME = "district";

  private Integer id;

  private String district;

  private Set<School> schools = new LinkedHashSet<>();

  private Set<User> users = new LinkedHashSet<>();

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

  @Column(name = COLUMN_DISTRICT_NAME, nullable = false)
  public String getDistrict() {
    return district;
  }

  public District setDistrict(String district) {
    this.district = district;
    return this;
  }

  @OneToMany(mappedBy = "district")
  public Set<School> getSchools() {
    return schools;
  }

  public District setSchools(Set<School> schools) {
    this.schools = schools;
    return this;
  }

  @OneToMany(mappedBy = "district")
  public Set<User> getUsers() {
    return users;
  }

  public District setUsers(Set<User> users) {
    this.users = users;
    return this;
  }
}
