package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = District.ENTITY_NAME)
@Table(name = District.TABLE_NAME, schema = "leo_temp")
public class District implements Serializable {

  public static final String ENTITY_NAME = "District";
  public static final String TABLE_NAME = "districts";
  public static final String COLUMN_ID_NAME = "id";
  public static final String COLUMN_DISTRICT_NAME = "district";
  private static final long serialVersionUID = -8027440663847399888L;


  private Integer id;

  private String district;

  private Set<School> schools = new LinkedHashSet<>();

  private User user;

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

  @OneToOne(mappedBy = "district")
  public User getUser() {
    return user;
  }

  public District setUser(User user) {
    this.user = user;
    return this;
  }

}