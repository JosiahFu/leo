package org.davincischools.leo.server.controllers;

import static org.davincischools.leo.database.utils.EntityUtils.checkRequiredMaxLength;
import static org.davincischools.leo.database.utils.EntityUtils.checkThat;

import java.util.Optional;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.davincischools.leo.protos.user_management.LoginRequest;
import org.davincischools.leo.protos.user_management.LoginResponse;
import org.davincischools.leo.server.utils.LogUtils;
import org.davincischools.leo.server.utils.LogUtils.LogExecutionError;
import org.davincischools.leo.server.utils.LogUtils.LogOperations;
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
  public LoginResponse getResource(@RequestBody Optional<LoginRequest> optionalRequest)
      throws LogExecutionError {
    return LogUtils.executeAndLog(
            db,
            optionalRequest.orElse(LoginRequest.getDefaultInstance()),
            (request, log) -> {
              var response = LoginResponse.newBuilder();

              checkLogin(request, response, log);

              return response.build();
            })
        .finish();
  }

  private void checkLogin(LoginRequest request, LoginResponse.Builder response, LogOperations log) {
    if (setFieldErrors(request, response)) {
      response.setSuccess(false);
      return;
    }

    Optional<UserX> user = db.getUserXRepository().findByEmailAddress(request.getEmailAddress());
    if (!user.isPresent() || !UserUtils.checkPassword(user.get(), request.getPassword())) {
      response.setSuccess(false);
      response.setLoginFailure(true);
      return;
    }
    log.setUserX(user.get());

    response.setSuccess(true);
    response.setUser(
        org.davincischools.leo.protos.user_management.User.newBuilder()
            .setId(user.get().getId())
            .setDistrictId(user.get().getDistrict().getId())
            .setFirstName(user.get().getFirstName())
            .setLastName(user.get().getLastName())
            .setEmailAddress(user.get().getEmailAddress())
            .setIsAdmin(user.get().getAdminX() != null)
            .setIsTeacher(user.get().getTeacher() != null)
            .setIsStudent(user.get().getStudent() != null)
            .build());
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
