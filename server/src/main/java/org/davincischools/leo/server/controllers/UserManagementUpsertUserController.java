package org.davincischools.leo.server.controllers;

import static com.google.common.base.Preconditions.checkArgument;
import static org.davincischools.leo.database.utils.EntityUtils.checkRequired;
import static org.davincischools.leo.database.utils.EntityUtils.checkRequiredMaxLength;
import static org.davincischools.leo.database.utils.EntityUtils.checkThat;

import java.util.Optional;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.davincischools.leo.protos.user_management.UpsertUserRequest;
import org.davincischools.leo.protos.user_management.UpsertUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserManagementUpsertUserController {

  @Autowired Database db;

  // If the client sends an empty MessageRequest then the body of the
  // request will have no bytes. Spring will then send either an optional
  // value that's empty, or a null value if the parameter is @Nullable.
  // So, handlers need to accept this type of input.
  @PostMapping(value = "/api/protos/UserManagementService/UpsertUser")
  @ResponseBody
  public UpsertUserResponse getResource(@RequestBody Optional<UpsertUserRequest> request) {
    request = Optional.of(request.orElse(UpsertUserRequest.getDefaultInstance()));
    UpsertUserResponse.Builder response = UpsertUserResponse.newBuilder();

    switch (request.get().getOperation()) {
      case CHECK -> checkUser(request.get(), response);
      case INSERT -> insertUser(request.get(), response);
      case UPDATE -> updateUser(request.get(), response);
    }

    return response.build();
  }

  private void checkUser(UpsertUserRequest request, UpsertUserResponse.Builder response) {
    Optional<User> user = Optional.empty();
    if (request.hasId()) {
      checkArgument(!request.hasRole(), "Role cannot be changed.");
      user = db.getUserRepository().findById(request.getId());
    } else {
      checkArgument(request.hasRole(), "Role is required.");
    }

    if (setFieldErrors(request, response, Optional.empty())) {
      response.setSuccess(false);
      return;
    }
    response.setSuccess(true);
  }

  private void insertUser(UpsertUserRequest request, UpsertUserResponse.Builder response) {
    checkArgument(!request.hasId());
    checkArgument(request.hasRole(), "Role is required.");

    if (setFieldErrors(request, response, Optional.empty())) {
      response.setSuccess(false);
      return;
    }

    db.getUserRepository()
        .save(
            UserUtils.setPassword(
                new User()
                    .setFirstName(request.getFirstName())
                    .setLastName(request.getLastName())
                    .setEmailAddress(request.getEmailAddress()),
                request.getPassword()));

    response.setSuccess(true);
  }

  private void updateUser(UpsertUserRequest request, UpsertUserResponse.Builder response) {
    checkArgument(request.hasId());
    checkArgument(!request.hasRole(), "Role cannot be changed.");

    Optional<User> user = db.getUserRepository().findById(request.getId());
    if (user.isEmpty()) {
      throw new IllegalArgumentException("User ID does not exist: " + request.getId());
    }

    if (setFieldErrors(request, response, user)) {
      response.setSuccess(false);
      return;
    }

    if (request.hasFirstName()) {
      user.get().setFirstName(request.getFirstName());
    }
    if (request.hasLastName()) {
      user.get().setLastName(request.getLastName());
    }
    if (request.hasEmailAddress()) {
      user.get().setEmailAddress(request.getEmailAddress());
    }
    if (request.hasPassword() && !request.getPassword().isEmpty()) {
      UserUtils.setPassword(user.get(), request.getPassword());
    }

    db.getUserRepository().save(user.get());

    response.setSuccess(true);
  }

  /** Checks fields for errors. Sets error messages and returns true if there are any errors. */
  private boolean setFieldErrors(
      UpsertUserRequest request, UpsertUserResponse.Builder response, Optional<User> existingUser) {
    boolean inputValid = true;

    inputValid &=
        checkRequiredMaxLength(
            request.getFirstName(),
            "First name",
            Database.USER_MAX_FIRST_NAME_LENGTH,
            response::setFirstNameError);
    inputValid &=
        checkRequiredMaxLength(
            request.getLastName(),
            "Last name",
            Database.USER_MAX_LAST_NAME_LENGTH,
            response::setLastNameError);
    inputValid &=
        checkThat(
            UserUtils.isEmailAddressValid(request.getEmailAddress()),
            response::setEmailAddressError,
            "Invalid email address.");
    inputValid &=
        checkRequiredMaxLength(
            request.getEmailAddress(),
            "Email address",
            Database.USER_MAX_EMAIL_ADDRESS_LENGTH,
            response::setEmailAddressError);

    inputValid &=
        checkThat(
            request.getPassword().equals(request.getVerifyPassword()),
            error -> {
              response.setPasswordError(error);
              response.setVerifyPasswordError(error);
            },
            "Passwords do not match.");

    if (!existingUser.isPresent()) {
      inputValid &= checkRequired(request.getPassword(), "Password", response::setPasswordError);
      inputValid &=
          checkThat(
              request.getPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
              response::setVerifyPasswordError,
              "Password must be at least %s characters long.",
              Database.USER_MIN_PASSWORD_LENGTH);

      inputValid &=
          checkRequired(request.getVerifyPassword(), "Password", response::setVerifyPasswordError);
      inputValid &=
          checkThat(
              request.getVerifyPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
              response::setVerifyPasswordError,
              "Password must be at least %s characters long.",
              Database.USER_MIN_PASSWORD_LENGTH);
    } else {
      if (request.hasPassword() || request.hasVerifyPassword()) {
        inputValid &=
            checkThat(
                request.getPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
                response::setVerifyPasswordError,
                "Password must be at least %s characters long.",
                Database.USER_MIN_PASSWORD_LENGTH);
        inputValid &=
            checkThat(
                request.getVerifyPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
                response::setVerifyPasswordError,
                "Password must be at least %s characters long.",
                Database.USER_MIN_PASSWORD_LENGTH);
      }
    }

    Optional<User> emailUser = db.getUserRepository().findByEmailAddress(request.getEmailAddress());
    if (emailUser.isPresent()) {
      inputValid &=
          checkThat(
              existingUser.isPresent()
                  && emailUser.get().getId().equals(existingUser.get().getId()),
              response::setEmailAddressError,
              "Email address is already in use.");
    }

    return !inputValid;
  }
}
