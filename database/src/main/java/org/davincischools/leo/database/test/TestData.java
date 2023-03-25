package org.davincischools.leo.database.test;

import java.util.UUID;
import org.davincischools.leo.database.daos.Database;
import org.davincischools.leo.database.daos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestData {

  public final User mrsPuff;
  public final User spongeBob;
  public final User sandy;
  public final String password = UUID.randomUUID().toString();

  Database db;

  public TestData(@Autowired Database db) {
    this.db = db;

    db.users.deleteAll();
    db.users.save(
        mrsPuff =
            new User()
                .setFirstName("Poppy")
                .setLastName("Puff")
                .setEmailAddress("poppy.puff@bikinibottom.com")
                .setPassword(password));
    db.users.save(
        spongeBob =
            new User()
                .setFirstName("SpongeBob")
                .setLastName("SquarePants")
                .setEmailAddress("spongebob.squarepants@bikinibottom.com")
                .setPassword(password));
    db.users.save(
        sandy =
            new User()
                .setFirstName("Sandy")
                .setLastName("Cheeks")
                .setEmailAddress("sandy.cheeks@bikinibottom.com")
                .setPassword(password));
  }
}
