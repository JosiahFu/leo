package org.davincischools.leo.database.daos;

import org.davincischools.leo.database.daos.User.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@Component
@EnableJpaRepositories(basePackageClasses = Database.class, considerNestedRepositories = true)
@EntityScan(basePackageClasses = Database.class)
public class Database {

  public static final String DATABASE_SALT_KEY = "project_leo.database.salt";

  public final Repo users;

  public Database(@Autowired Repo users) {
    this.users = users;
  }
}
