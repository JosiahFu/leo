package org.davincischools.leo.database.daos;

import com.google.common.base.Preconditions;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Repository;

@Entity
@Table(name = "users")
public class User {

  public static final int MAX_FIRST_NAME_LENGTH = 255;
  public static final int MAX_LAST_NAME_LENGTH = 255;
  public static final int MAX_EMAIL_ADDRESS_LENGTH = 254;
  public static final int MAX_PASSWORD_LENGTH = 128;
  public static final int MAX_ENCODED_PASSWORD_LENGTH = 65535;

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

  @Column(nullable = false, unique = true, length = MAX_EMAIL_ADDRESS_LENGTH)
  private String emailAddress;

  @Column(nullable = false)
  private byte[] encodedPassword;

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

  public boolean checkPassword(String password) {
    Preconditions.checkArgument(password.length() <= MAX_PASSWORD_LENGTH, "Password too long.");
    return PasswordEncoderFactories.createDelegatingPasswordEncoder()
        .matches(password, new String(encodedPassword, StandardCharsets.UTF_8));
  }

  public User setPassword(String password) {
    Preconditions.checkArgument(password.length() <= MAX_PASSWORD_LENGTH, "Password too long.");
    byte[] encodedPassword =
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
            .encode(password)
            .getBytes(StandardCharsets.UTF_8);
    Preconditions.checkArgument(
        encodedPassword.length <= MAX_ENCODED_PASSWORD_LENGTH, "Encoded password too long.");
    this.encodedPassword = encodedPassword;
    return this;
  }
}
