package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity(name = Admin.ENTITY_NAME)
@Table(name = Admin.TABLE_NAME)
public class Admin {

  public static final String ENTITY_NAME = "Admin";
  public static final String TABLE_NAME = "admins";
  public static final String COLUMN_ID_NAME = "id";

  private Long id;

  private District district;

  private User user;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Long getId() {
    return id;
  }

  public Admin setId(Long id) {
    this.id = id;
    return this;
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "district_id", nullable = false)
  public District getDistrict() {
    return district;
  }

  public Admin setDistrict(District district) {
    this.district = district;
    return this;
  }

  @OneToOne(mappedBy = "admin")
  public User getUser() {
    return user;
  }

  public Admin setUser(User user) {
    this.user = user;
    return this;
  }
}
