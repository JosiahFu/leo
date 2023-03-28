package org.davincischools.leo.database.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity(name = Admin.ENTITY_NAME)
@Table(name = Admin.TABLE_NAME, schema = "leo_temp")
public class Admin implements Serializable {

  public static final String ENTITY_NAME = "Admin";
  public static final String TABLE_NAME = "admins";
  public static final String COLUMN_ID_NAME = "id";
  private static final long serialVersionUID = 7538037748653687312L;

  private Integer id;

  private User user;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = COLUMN_ID_NAME, nullable = false)
  public Integer getId() {
    return id;
  }

  public Admin setId(Integer id) {
    this.id = id;
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