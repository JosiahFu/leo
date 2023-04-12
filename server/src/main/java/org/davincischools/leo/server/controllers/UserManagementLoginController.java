package org.davincischools.leo.server.controllers;

import static org.davincischools.leo.database.utils.EntityUtils.checkRequiredMaxLength;
import static org.davincischools.leo.database.utils.EntityUtils.checkThat;

import java.util.Optional;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.davincischools.leo.protos.user_management.LoginRequest;
import org.davincischools.leo.protos.user_management.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserManagementLoginController {

  @Autowired Database db;

  // If the client sends an empty MessageRequest then the body of the
  // request will have no bytes. Spring will then send either an optional
  // value that's empty, or a null value if the parameter is @Nullable.
  // So, handlers need to accept this type of input.
  @PostMapping(value = "/api/protos/UserManagementService/Login")
  @ResponseBody
  public LoginResponse getResource(@RequestBody Optional<LoginRequest> optionalRequest) {
    var request = optionalRequest.orElse(LoginRequest.getDefaultInstance());
    var response = LoginResponse.newBuilder();

    checkLogin(request, response);

    return response.build();
  }

  private void checkLogin(LoginRequest request, LoginResponse.Builder response) {
    if (setFieldErrors(request, response)) {
      response.setSuccess(false);
      return;
    }

    try {
      Optional<User> user =
          db.getUserRepository().findFullUserByEmailAddress(request.getEmailAddress());
      if (!user.isPresent() || !UserUtils.checkPassword(user.get(), request.getPassword())) {
        response.setSuccess(false);
        response.setLoginFailure(true);
        return;
      }

      user = db.getUserRepository().findFullUserByUserId(user.get().getId());
      if (!user.isPresent()) {
        response.setSuccess(false);
        response.setLoginFailure(true);
        return;
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }

    response.setSuccess(true);
  }

  /** Checks fields for errors. Sets error messages and returns true if there are any errors. */
  private boolean setFieldErrors(LoginRequest request, LoginResponse.Builder response) {
    boolean inputValid = true;

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
            request.getPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
            response::setPasswordError,
            "Password must be at least %s characters long.",
            Database.USER_MIN_PASSWORD_LENGTH);

    return !inputValid;
  }
}
