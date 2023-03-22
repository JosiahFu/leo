package org.davincischools.leo.database.daos;

import static org.davincischools.leo.database.daos.Database.DATABASE_SALT_KEY;

import com.google.common.base.Preconditions;
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
import java.util.Optional;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.testcontainers.shaded.org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.testcontainers.shaded.org.bouncycastle.crypto.params.Argon2Parameters;

@Entity
@Table(name = "users")
public class User {

  public static final int MAX_FIRST_NAME_LENGTH = 255;
  public static final int MAX_LAST_NAME_LENGTH = 255;
  public static final int MAX_EMAIL_ADDRESS_LENGTH = 254;
  public static final int MAX_PASSWORD_LENGTH = 128;

  public static boolean isEmailAddressValid(String emailAddress) {
    String[] split = emailAddress.split("@", 3);
    if (split.length != 2) {
      return false;
    }
    if (split[0].isEmpty() || split[1].isEmpty()) {
      return false;
    }
    return true;
  }

  @Repository
  public interface Repo extends CrudRepository<User, Long> {
    Optional<User> findByEmailAddress(String emailAddress);
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

  @Column(nullable = false, length = MAX_FIRST_NAME_LENGTH)
  private String firstName;

  @Column(nullable = false, length = MAX_LAST_NAME_LENGTH)
  private String lastName;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(nullable = false, unique = true, length = MAX_EMAIL_ADDRESS_LENGTH)
  private String emailAddress;

  @Column(nullable = false, length = 65)
  private byte[] passwordHash;

  public long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public User setFirstName(String firstName) {
    Preconditions.checkArgument(
        firstName.length() <= MAX_FIRST_NAME_LENGTH, "First name too long.");
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public User setLastName(String lastName) {
    Preconditions.checkArgument(lastName.length() <= MAX_LAST_NAME_LENGTH, "Last name too long.");
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
    Preconditions.checkArgument(
        emailAddress.length() <= MAX_EMAIL_ADDRESS_LENGTH, "Email address too long.");
    Preconditions.checkArgument(
        isEmailAddressValid(emailAddress), "Malformed email address: %s", emailAddress);
    this.emailAddress = emailAddress;
    return this;
  }

  private byte[] getPasswordHash(String password) {
    Preconditions.checkArgument(password.length() <= MAX_PASSWORD_LENGTH, "Password too long.");
    byte[] result = new byte[65];
    result[0] = 1; // Hashing mechanism version.

    Argon2BytesGenerator gen = new Argon2BytesGenerator();
    gen.init(ARGON_2_PARAMETERS);
    gen.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 1, result.length - 1);

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
