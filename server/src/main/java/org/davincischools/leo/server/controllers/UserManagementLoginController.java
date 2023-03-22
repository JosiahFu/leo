package org.davincischools.leo.server.controllers;

import static org.davincischools.leo.database.daos.EntityUtils.checkRequiredMaxLength;
import static org.davincischools.leo.database.daos.EntityUtils.checkThat;
import static org.davincischools.leo.database.daos.User.MAX_EMAIL_ADDRESS_LENGTH;
import static org.davincischools.leo.database.daos.User.MAX_PASSWORD_LENGTH;

import java.util.Optional;
import org.davincischools.leo.database.daos.Database;
import org.davincischools.leo.database.daos.User;
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
  public LoginResponse getResource(@RequestBody Optional<LoginRequest> request) {
    request = Optional.of(request.orElse(LoginRequest.getDefaultInstance()));
    LoginResponse.Builder response = LoginResponse.newBuilder();

    checkLogin(request.get(), response);

    return response.build();
  }

  private void checkLogin(LoginRequest request, LoginResponse.Builder response) {
    if (setFieldErrors(request, response)) {
      response.setSuccess(false);
      return;
    }

    Optional<User> user = db.users.findByEmailAddress(request.getEmailAddress());
    if (user.isPresent() && user.get().checkPassword(request.getPassword())) {
      response.setSuccess(true);
      return;
    }

    response.setSuccess(false);
    response.setLoginFailure(true);
  }

  /** Checks fields for errors. Sets error messages and returns true if there are any errors. */
  private boolean setFieldErrors(LoginRequest request, LoginResponse.Builder response) {
    boolean inputValid = true;

    inputValid &=
        checkThat(
            User.isEmailAddressValid(request.getEmailAddress()),
            response::setEmailAddressError,
            "Invalid email address.");
    inputValid &=
        checkRequiredMaxLength(
            request.getEmailAddress(),
            "Email address",
            MAX_EMAIL_ADDRESS_LENGTH,
            response::setEmailAddressError);
    inputValid &=
        checkRequiredMaxLength(
            request.getPassword(), "Password", MAX_PASSWORD_LENGTH, response::setPasswordError);

    return !inputValid;
  }
}
