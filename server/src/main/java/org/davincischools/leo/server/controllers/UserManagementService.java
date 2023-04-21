package org.davincischools.leo.server.controllers;

import static org.davincischools.leo.database.utils.EntityUtils.checkRequired;
import static org.davincischools.leo.database.utils.EntityUtils.checkRequiredMaxLength;
import static org.davincischools.leo.database.utils.EntityUtils.checkThat;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import javax.security.auth.login.AccountNotFoundException;
import org.davincischools.leo.database.daos.Admin;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.Student;
import org.davincischools.leo.database.daos.Teacher;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.UserUtils;
import org.davincischools.leo.protos.user_management.GetUserDetailsRequest;
import org.davincischools.leo.protos.user_management.GetUserDetailsResponse;
import org.davincischools.leo.protos.user_management.GetUsersRequest;
import org.davincischools.leo.protos.user_management.RemoveUserRequest;
import org.davincischools.leo.protos.user_management.UpsertUserRequest;
import org.davincischools.leo.protos.user_management.UserInformationResponse;
import org.davincischools.leo.protos.user_management.UserInformationResponse.Builder;
import org.davincischools.leo.server.utils.DataAccess;
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
  public UserInformationResponse getUsers(@RequestBody Optional<GetUsersRequest> optionalRequest) {
    var request = optionalRequest.orElse(GetUsersRequest.getDefaultInstance());
    var response = UserInformationResponse.newBuilder();
    getAllFullUsers(request.getDistrictId(), -1, response);
    response.setSuccess(true);
    return response.build();
  }

  @PostMapping(value = "/api/protos/UserManagementService/GetUserDetails")
  @ResponseBody
  public GetUserDetailsResponse getUserDetails(
      @RequestBody Optional<GetUserDetailsRequest> optionalRequest)
      throws AccountNotFoundException {
    var request = optionalRequest.orElse(GetUserDetailsRequest.getDefaultInstance());
    var response = GetUserDetailsResponse.newBuilder();

    Optional<User> user = db.getUserRepository().findFullUserByUserId(request.getUserId());

    if (user.isPresent()) {
      response.setUser(DataAccess.convertFullUserToProto(user.get()));
      response.addAllSchoolIds(
          Lists.transform(
              db.getTeacherSchoolRepository().findSchoolsByUserId(request.getUserId()),
              School::getId));
    }

    return response.build();
  }

  @PostMapping(value = "/api/protos/UserManagementService/UpsertUser")
  @ResponseBody
  @Transactional
  public UserInformationResponse upsertUser(
      @RequestBody Optional<UpsertUserRequest> optionalRequest) {
    var request = optionalRequest.orElse(UpsertUserRequest.getDefaultInstance());
    var response = UserInformationResponse.newBuilder();

    Optional<User> existingUser = Optional.empty();
    if (request.getUser().hasId()) {
      existingUser = db.getUserRepository().findFullUserByUserId(request.getUser().getId());
    }

    if (setFieldErrors(request, response, existingUser)) {
      response.setSuccess(false);
      return response.build();
    }

    User user =
        new User()
            .setCreationTime(Instant.now())
            .setDistrict(
                new District()
                    .setCreationTime(Instant.now())
                    .setId(request.getUser().getDistrictId()))
            .setFirstName(request.getUser().getFirstName())
            .setLastName(request.getUser().getLastName())
            .setEmailAddress(request.getUser().getEmailAddress());
    existingUser.ifPresent(
        e -> {
          user.setId(e.getId());
          user.setAdmin(e.getAdmin());
          user.setTeacher(e.getTeacher());
          user.setStudent(e.getStudent());
          user.setEncodedPassword(e.getEncodedPassword());
        });

    if (!request.getUser().getPassword().isEmpty()) {
      UserUtils.setPassword(user, request.getUser().getPassword());
    }

    if ((user.getAdmin() != null) ^ request.getUser().getIsAdmin()) {
      if (request.getUser().getIsAdmin()) {
        user.setAdmin(db.getAdminRepository().save(new Admin().setCreationTime(Instant.now())));
      } else {
        user.setAdmin(null);
      }
    }

    if ((user.getTeacher() != null) ^ request.getUser().getIsTeacher()) {
      if (request.getUser().getIsTeacher()) {
        user.setTeacher(
            db.getTeacherRepository().save(new Teacher().setCreationTime(Instant.now())));
      } else {
        user.setTeacher(null);
      }
    }
    if (user.getTeacher() != null) {
      // Add any missing schools.
      ImmutableSet<Integer> schoolIdsToAdd = ImmutableSet.copyOf(request.getSchoolIdsList());

      if (existingUser.isPresent()) {
        // Remove any extraneous schools.
        db.getTeacherSchoolRepository()
            .keepSchoolsByTeacherId(user.getTeacher().getId(), request.getSchoolIdsList());

        // Calculate missing schools.
        List<Integer> existingSchoolIds =
            Lists.transform(
                db.getTeacherSchoolRepository().findSchoolsByUserId(user.getId()), School::getId);
        schoolIdsToAdd =
            Sets.difference(
                    Sets.newHashSet(request.getSchoolIdsList()), Sets.newHashSet(existingSchoolIds))
                .immutableCopy();
      }

      // Add missing schools.
      for (int schoolId : schoolIdsToAdd) {
        db.getTeacherSchoolRepository()
            .save(
                db.getTeacherSchoolRepository()
                    .createTeacherSchool(
                        user.getTeacher(),
                        new School().setCreationTime(Instant.now()).setId(schoolId)));
      }
    }

    if ((user.getStudent() != null) ^ request.getUser().getIsStudent()) {
      if (request.getUser().getIsStudent()) {
        // TODO: Set the student ID.
        user.setStudent(
            db.getStudentRepository()
                .save(new Student().setCreationTime(Instant.now()).setStudentId(-1)));
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

    getAllFullUsers(request.getUser().getDistrictId(), user.getId(), response);
    response.setSuccess(true);
    return response.build();
  }

  /** Checks fields for errors. Sets error messages and returns true if there are any errors. */
  private boolean setFieldErrors(
      UpsertUserRequest request, Builder response, Optional<User> existingUser) {
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

    if (existingUser.isPresent()) {
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
    } else {
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
    }

    Optional<User> emailUser =
        db.getUserRepository().findFullUserByEmailAddress(request.getUser().getEmailAddress());
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
    getAllFullUsers(request.get().getDistrictId(), -1, response);
    response.setSuccess(true);
    return response.build();
  }

  private void getAllFullUsers(int districtId, int nextUserId, Builder response) {
    response.setDistrictId(districtId);
    response.setNextUserId(nextUserId);
    response.addAllUsers(DataAccess.getProtoFullUsersByDistrictId(db, districtId));
  }
}
