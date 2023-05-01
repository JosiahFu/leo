package org.davincischools.leo.server.utils;

import com.google.common.base.Strings;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;

public class DataAccess {

  @SafeVarargs
  public static <T> T firstNonNull(Callable<T>... values) throws NullPointerException {
    return Stream.of(values)
        .map(
            value -> {
              try {
                return value.call();
              } catch (Exception e) {
                return null;
              }
            })
        .filter(Objects::nonNull)
        .findFirst()
        .orElseThrow(NullPointerException::new);
  }

  public static String getShortDescr(Object daoWithNameAndShortDescAndLongDescr) {
    return firstNonNull(
        () ->
            (String)
                daoWithNameAndShortDescAndLongDescr
                    .getClass()
                    .getMethod("getShortDescr")
                    .invoke(daoWithNameAndShortDescAndLongDescr),
        () ->
            (String)
                daoWithNameAndShortDescAndLongDescr
                    .getClass()
                    .getMethod("getName")
                    .invoke(daoWithNameAndShortDescAndLongDescr));
  }

  public static String getLongDescr(Object daoWithNameAndShortDescAndLongDescr) {
    return firstNonNull(
        () ->
            (String)
                daoWithNameAndShortDescAndLongDescr
                    .getClass()
                    .getMethod("getLongDescr")
                    .invoke(daoWithNameAndShortDescAndLongDescr),
        () ->
            (String)
                daoWithNameAndShortDescAndLongDescr
                    .getClass()
                    .getMethod("getShortDescr")
                    .invoke(daoWithNameAndShortDescAndLongDescr),
        () ->
            (String)
                daoWithNameAndShortDescAndLongDescr
                    .getClass()
                    .getMethod("getName")
                    .invoke(daoWithNameAndShortDescAndLongDescr));
  }

  public static org.davincischools.leo.protos.user_management.User convertFullUserXToProto(
      UserX user) {
    var userProto =
        org.davincischools.leo.protos.user_management.User.newBuilder()
            .setId(firstNonNull(user::getId, () -> -1))
            .setDistrictId(firstNonNull(user.getDistrict()::getId, () -> -1))
            .setFirstName(user.getFirstName())
            .setLastName(user.getLastName())
            .setEmailAddress(user.getEmailAddress())
            .setIsAdmin(user.getAdminX() != null)
            .setIsTeacher(user.getTeacher() != null)
            .setIsStudent(user.getStudent() != null);
    return userProto.build();
  }

  public static List<org.davincischools.leo.protos.user_management.User>
      getProtoFullUserXsByDistrictId(Database db, int districtId) {
    return StreamSupport.stream(
            db.getUserXRepository().findAllByDistrictId(districtId).spliterator(), false)
        .map(DataAccess::convertFullUserXToProto)
        .collect(Collectors.toList());
  }

  public static org.davincischools.leo.protos.school_management.School convertSchoolToProto(
      School school) {
    return org.davincischools.leo.protos.school_management.School.newBuilder()
        .setId(firstNonNull(school::getId, () -> -1))
        .setDistrictId(firstNonNull(school.getDistrict()::getId, () -> -1))
        .setName(school.getName())
        .setAddress(Strings.nullToEmpty(school.getAddress()))
        .build();
  }

  public static List<org.davincischools.leo.protos.school_management.School>
      getProtoSchoolsByDistrictId(Database db, int districtId) {
    return StreamSupport.stream(
            db.getSchoolRepository().findAllByDistrictId(districtId).spliterator(), false)
        .map(DataAccess::convertSchoolToProto)
        .collect(Collectors.toList());
  }

  public static org.davincischools.leo.protos.class_management.ClassX convertClassToProto(
      ClassX classX) {
    return org.davincischools.leo.protos.class_management.ClassX.newBuilder()
        .setId(firstNonNull(classX::getId, () -> -1))
        .setName(classX.getName())
        .setShortDescr(getShortDescr(classX))
        .setLongDescr(getLongDescr(classX))
        .build();
  }

  public static org.davincischools.leo.protos.class_management.Assignment convertAssignmentToProto(
      ClassX class_, Assignment assignment) {
    return org.davincischools.leo.protos.class_management.Assignment.newBuilder()
        .setId(firstNonNull(assignment::getId, () -> -1))
        .setName(assignment.getName())
        .setShortDescr(getShortDescr(assignment))
        .setLongDescr(getLongDescr(assignment))
        .setClassX(convertClassToProto(class_))
        .build();
  }

  public static org.davincischools.leo.protos.class_management.Project convertProjectToProto(
      Project project) {
    return org.davincischools.leo.protos.class_management.Project.newBuilder()
        .setId(firstNonNull(project::getId, () -> -1))
        .setName(project.getName())
        .setShortDescr(getShortDescr(project))
        .setLongDescr(getLongDescr(project))
        .build();
  }

  public static List<org.davincischools.leo.protos.class_management.Project>
      getProtoProjectsByUserXId(Database db, UserX userX) {
    return StreamSupport.stream(
            db.getProjectRepository()
                .findAllByStudentId(userX.getStudent().getStudentId())
                .spliterator(),
            false)
        .map(DataAccess::convertProjectToProto)
        .collect(Collectors.toList());
  }
}
