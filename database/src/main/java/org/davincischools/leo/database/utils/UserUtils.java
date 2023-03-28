package org.davincischools.leo.database.utils;

import static org.davincischools.leo.database.utils.Database.USER_MAX_ENCODED_PASSWORD_UTF8_BLOB_LENGTH;

import com.google.common.base.Preconditions;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import org.davincischools.leo.database.daos.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

public class UserUtils {

  public enum Role {
    STUDENT,
    TEACHER,
    ADMIN
  }

  public static boolean checkPassword(User user, String password) {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder()
        .matches(password, new String(user.getEncodedPasswordUtf8(), StandardCharsets.UTF_8));
  }

  public static User setPassword(User user, String password) {
    byte[] encodedPassword =
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
            .encode(password)
            .getBytes(StandardCharsets.UTF_8);
    Preconditions.checkArgument(
        encodedPassword.length <= USER_MAX_ENCODED_PASSWORD_UTF8_BLOB_LENGTH,
        "Encoded password too long.");
    user.setEncodedPasswordUtf8(encodedPassword);
    return user;
  }

  public static boolean isEmailAddressValid(String emailAddress) {
    String[] split = emailAddress.split("@", 3);
    if (split.length != 2) {
      return false;
    }
    if (split[0].isEmpty() || split[1].isEmpty()) {
      return false;
    }
    if (split[1].indexOf('.') < 0) {
      return false;
    }
    return true;
  }

  public static EnumSet<Role> getRoles(User user) {
    EnumSet<Role> roles = EnumSet.noneOf(Role.class);
    if (user.getAdmin() != null) {
      roles.add(Role.ADMIN);
    }
    if (user.getTeacher() != null) {
      roles.add(Role.TEACHER);
    }
    if (user.getStudent() != null) {
      roles.add(Role.STUDENT);
    }
    return roles;
  }
}
