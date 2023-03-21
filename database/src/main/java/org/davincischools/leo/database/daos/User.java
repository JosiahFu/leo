package org.davincischools.leo.database.daos;

import static org.davincischools.leo.database.daos.Database.DATABASE_SALT_KEY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.testcontainers.shaded.org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.testcontainers.shaded.org.bouncycastle.crypto.params.Argon2Parameters;

@Entity
@Table(name = "users")
public class User {

  @Repository
  public interface Repo extends CrudRepository<User, Long> {
    User findByEmailAddress(String emailAddress);
  }

  private static final String SALT = Objects.requireNonNull(System.getProperty(DATABASE_SALT_KEY));
  private static final Argon2Parameters ARGON_2_PARAMETERS =
      new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
          .withVersion(Argon2Parameters.ARGON2_VERSION_13)
          .withIterations(4)
          .withMemoryPowOfTwo(20) // 1 MiB
          .withParallelism(1)
          .withSalt(SALT.getBytes(StandardCharsets.UTF_8))
          .build();

  public enum Role {
    STUDENT,
    TEACHER,
    ADMIN
  }

  @Id
  @Column(updatable = false)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users.id.PRIMARY")
  @GenericGenerator(name = "users.id.PRIMARY", strategy = "native")
  private long id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(nullable = false, unique = true)
  private String emailAddress;

  @Column(nullable = false)
  private byte[] passwordHash;

  public long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public User setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public User setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public Role getRole() {
    return role;
  }

  public User setRole(Role role) {
    this.role = role;
    return this;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public User setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
    return this;
  }

  private byte[] getPasswordHash(String password) {
    Argon2BytesGenerator gen = new Argon2BytesGenerator();
    gen.init(ARGON_2_PARAMETERS);
    byte[] result = new byte[64];
    gen.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0, result.length);
    return result;
  }

  public boolean checkPassword(String password) {
    return Arrays.equals(passwordHash, getPasswordHash(password));
  }

  public User setPassword(String password) {
    passwordHash = getPasswordHash(password);
    return this;
  }
}
