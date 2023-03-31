package org.davincischools.leo.server.controllers;

import static org.davincischools.leo.database.utils.EntityUtils.checkRequired;
import static org.davincischools.leo.database.utils.EntityUtils.checkRequiredMaxLength;
import static org.davincischools.leo.database.utils.EntityUtils.checkThat;

import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.davincischools.leo.database.daos.Admin;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.davincischools.leo.protos.user_management.GetUsersRequest;
import org.davincischools.leo.protos.user_management.RemoveUserRequest;
import org.davincischools.leo.protos.user_management.UpsertUserRequest;
import org.davincischools.leo.protos.user_management.UserInformationResponse;
import org.davincischools.leo.protos.user_management.UserInformationResponse.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/UserManagementService/GetUsers")
  @ResponseBody
  public UserInformationResponse getUsers(@RequestBody Optional<GetUsersRequest> request) {
    request = Optional.of(request.orElse(GetUsersRequest.getDefaultInstance()));
    UserInformationResponse.Builder response = UserInformationResponse.newBuilder();
    getAllUsers(request.get().getDistrictId(), -1, response);
    response.setSuccess(true);
    return response.build();
  }

  @PostMapping(value = "/api/protos/UserManagementService/UpsertUser")
  @ResponseBody
  @Transactional
  public UserInformationResponse upsertUser(@RequestBody Optional<UpsertUserRequest> requestBody) {
    UpsertUserRequest request = requestBody.orElse(UpsertUserRequest.getDefaultInstance());
    UserInformationResponse.Builder response = UserInformationResponse.newBuilder();

    Optional<User> existingUser = Optional.empty();
    if (request.getUser().hasId()) {
      existingUser = db.getUserRepository().findById(request.getUser().getId());
    }

    if (setFieldErrors(request, response, existingUser)) {
      response.setSuccess(false);
      return response.build();
    }

    User user =
        new User()
            .setDistrict(new District().setId(request.getUser().getDistrictId()))
            .setFirstName(request.getUser().getFirstName())
            .setLastName(request.getUser().getLastName())
            .setEmailAddress(request.getUser().getEmailAddress());
    existingUser.ifPresent(
        e -> {
          user.setId(e.getId());
          user.setAdmin(e.getAdmin());
          user.setTeacher(e.getTeacher());
          user.setStudent(e.getStudent());
          user.setEncodedPasswordUtf8(e.getEncodedPasswordUtf8());
        });

    if (!request.getUser().getPassword().isEmpty()) {
      UserUtils.setPassword(user, request.getUser().getPassword());
    }

    if ((user.getAdmin() != null) ^ request.getUser().getIsAdmin()) {
      if (request.getUser().getIsAdmin()) {
        user.setAdmin(db.getAdminRepository().save(new Admin()));
      } else {
        user.setAdmin(null);
      }
    }

    if ((user.getTeacher() != null) ^ request.getUser().getIsTeacher()) {
      if (request.getUser().getIsTeacher()) {
        user.setTeacher(db.getTeacherRepository().save(new Teacher()));
      } else {
        user.setTeacher(null);
      }
    }

    if ((user.getStudent() != null) ^ request.getUser().getIsStudent()) {
      if (request.getUser().getIsStudent()) {
        user.setStudent(db.getStudentRepository().save(new Student()));
      } else {
        user.setStudent(null);
      }
    }

    db.getUserRepository().save(user);

    existingUser.ifPresent(
        e -> {
          if (e.getAdmin() != null && user.getAdmin() == null) {
            db.getAdminRepository().delete(e.getAdmin());
          }
          if (e.getTeacher() != null && user.getTeacher() == null) {
            db.getTeacherRepository().delete(e.getTeacher());
          }
          if (e.getStudent() != null && user.getStudent() == null) {
            db.getStudentRepository().delete(e.getStudent());
          }
        });

    getAllUsers(request.getUser().getDistrictId(), user.getId(), response);
    response.setSuccess(true);
    return response.build();
  }

  /** Checks fields for errors. Sets error messages and returns true if there are any errors. */
  private boolean setFieldErrors(
      UpsertUserRequest request,
      UserInformationResponse.Builder response,
      Optional<User> existingUser) {
    boolean inputValid = true;

    inputValid &=
        checkRequiredMaxLength(
            request.getUser().getFirstName(),
            "First name",
            Database.USER_MAX_FIRST_NAME_LENGTH,
            response::setFirstNameError);
    inputValid &=
        checkRequiredMaxLength(
            request.getUser().getLastName(),
            "Last name",
            Database.USER_MAX_LAST_NAME_LENGTH,
            response::setLastNameError);
    inputValid &=
        checkThat(
            UserUtils.isEmailAddressValid(request.getUser().getEmailAddress()),
            response::setEmailAddressError,
            "Invalid email address.");
    inputValid &=
        checkRequiredMaxLength(
            request.getUser().getEmailAddress(),
            "Email address",
            Database.USER_MAX_EMAIL_ADDRESS_LENGTH,
            response::setEmailAddressError);

    inputValid &=
        checkThat(
            request.getUser().getPassword().equals(request.getUser().getVerifyPassword()),
            error -> {
              response.setPasswordError(error);
              response.setVerifyPasswordError(error);
            },
            "Passwords do not match.");

    if (!existingUser.isPresent()) {
      inputValid &=
          checkRequired(request.getUser().getPassword(), "Password", response::setPasswordError);
      inputValid &=
          checkThat(
              request.getUser().getPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
              response::setPasswordError,
              "Password must be at least %s characters long.",
              Database.USER_MIN_PASSWORD_LENGTH);

      inputValid &=
          checkRequired(
              request.getUser().getVerifyPassword(), "Password", response::setVerifyPasswordError);
      inputValid &=
          checkThat(
              request.getUser().getVerifyPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
              response::setVerifyPasswordError,
              "Password must be at least %s characters long.",
              Database.USER_MIN_PASSWORD_LENGTH);
    } else {
      if (!request.getUser().getPassword().isEmpty()
          || !request.getUser().getVerifyPassword().isEmpty()) {
        inputValid &=
            checkThat(
                request.getUser().getPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
                response::setPasswordError,
                "Password must be at least %s characters long.",
                Database.USER_MIN_PASSWORD_LENGTH);
        inputValid &=
            checkThat(
                request.getUser().getVerifyPassword().length() >= Database.USER_MIN_PASSWORD_LENGTH,
                response::setVerifyPasswordError,
                "Password must be at least %s characters long.",
                Database.USER_MIN_PASSWORD_LENGTH);
      }
    }

    Optional<User> emailUser =
        db.getUserRepository().findByEmailAddress(request.getUser().getEmailAddress());
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

  @PostMapping(value = "/api/protos/UserManagementService/RemoveUser")
  @ResponseBody
  @Transactional
  public UserInformationResponse removeUser(@RequestBody Optional<RemoveUserRequest> request) {
    request = Optional.of(request.orElse(RemoveUserRequest.getDefaultInstance()));
    UserInformationResponse.Builder response = UserInformationResponse.newBuilder();
    db.getUserRepository().deleteById(request.get().getUserId());
    getAllUsers(request.get().getDistrictId(), -1, response);
    response.setSuccess(true);
    return response.build();
  }

  private UserInformationResponse.Builder getAllUsers(
      int districtId, int nextUserId, Builder response) {
    response.setDistrictId(districtId);
    response.setNextUserId(nextUserId);
    response.addAllUsers(
        StreamSupport.stream(
                db.getUserRepository().findAllByDistrictId(districtId).spliterator(), false)
            .map(
                user ->
                    org.davincischools.leo.protos.user_management.User.newBuilder()
                        .setId(user.getId())
                        .setDistrictId(user.getDistrict().getId())
                        .setFirstName(user.getFirstName())
                        .setLastName(user.getLastName())
                        .setEmailAddress(user.getEmailAddress())
                        .setIsAdmin(user.getAdmin() != null)
                        .setIsTeacher(user.getTeacher() != null)
                        .setIsStudent(user.getStudent() != null)
                        .build())
            .collect(Collectors.toList()));
    return response;
  }
}
