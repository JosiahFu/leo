package org.davincischools.leo.server.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.utils.Database;

public class DataAccess {

  public static org.davincischools.leo.protos.user_management.User convertFullUserToProto(
      User user) {
    var userProto =
        org.davincischools.leo.protos.user_management.User.newBuilder()
            .setId(user.getId())
            .setDistrictId(user.getDistrict().getId())
            .setFirstName(user.getFirstName())
            .setLastName(user.getLastName())
            .setEmailAddress(user.getEmailAddress())
            .setIsAdmin(user.getAdmin() != null)
            .setIsTeacher(user.getTeacher() != null)
            .setIsStudent(user.getStudent() != null);
    return userProto.build();
  }

  public static List<org.davincischools.leo.protos.user_management.User>
      getProtoFullUsersByDistrictId(Database db, int districtId) {
    return StreamSupport.stream(
            db.getUserRepository().findAllFullUsersByDistrictId(districtId).spliterator(), false)
        .map(DataAccess::convertFullUserToProto)
        .collect(Collectors.toList());
  }

  public static org.davincischools.leo.protos.school_management.School convertSchoolToProto(
      School school) {
    return org.davincischools.leo.protos.school_management.School.newBuilder()
        .setId(school.getId())
        .setDistrictId(school.getDistrict().getId())
        .setName(school.getName())
        .setCity(school.getCity())
        .build();
  }

  public static List<org.davincischools.leo.protos.school_management.School>
      getProtoSchoolsByDistrictId(Database db, int districtId) {
    return StreamSupport.stream(
            db.getSchoolRepository().findAllByDistrictId(districtId).spliterator(), false)
        .map(DataAccess::convertSchoolToProto)
        .collect(Collectors.toList());
  }
}
