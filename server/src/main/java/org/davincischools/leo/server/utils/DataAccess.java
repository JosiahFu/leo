package org.davincischools.leo.server.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.davincischools.leo.database.daos.Assignment;
import org.davincischools.leo.database.daos.ClassX;
import org.davincischools.leo.database.daos.Project;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;

public class DataAccess {

  public static org.davincischools.leo.protos.user_management.User convertFullUserXToProto(
      UserX user) {
    var userProto =
        org.davincischools.leo.protos.user_management.User.newBuilder()
            .setId(user.getId())
            .setDistrictId(user.getDistrict().getId())
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
            db.getUserXRepository().findAllFullUserXsByDistrictId(districtId).spliterator(), false)
        .map(DataAccess::convertFullUserXToProto)
        .collect(Collectors.toList());
  }

  public static org.davincischools.leo.protos.school_management.School convertSchoolToProto(
      School school) {
    return org.davincischools.leo.protos.school_management.School.newBuilder()
        .setId(school.getId())
        .setDistrictId(school.getDistrict().getId())
        .setName(school.getName())
        .setAddress(school.getAddress())
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
        .setId(classX.getId())
        .setName(classX.getName())
        .setShortDescr(classX.getShortDescr())
        .setLongDescr(classX.getLongDescr())
        .build();
  }

  public static org.davincischools.leo.protos.class_management.Assignment convertAssignmentToProto(
      ClassX class_, Assignment assignment) {
    return org.davincischools.leo.protos.class_management.Assignment.newBuilder()
        .setId(assignment.getId())
        .setName(assignment.getName())
        .setShortDescr(assignment.getShortDescr())
        .setLongDescr(assignment.getLongDescr())
        .setClassX(convertClassToProto(class_))
        .build();
  }

  public static org.davincischools.leo.protos.class_management.Project convertProjectToProto(
      Project project) {
    return org.davincischools.leo.protos.class_management.Project.newBuilder()
        .setId(project.getId())
        .setName(project.getName())
        .setShortDescr(project.getShortDescr())
        .setLongDescr(project.getLongDescr())
        .build();
  }

  public static List<org.davincischools.leo.protos.class_management.Project>
      getProtoProjectsByUserXId(Database db, int userXId) {
    return StreamSupport.stream(
            db.getProjectRepository().findAllByUserXId(userXId).spliterator(), false)
        .map(DataAccess::convertProjectToProto)
        .collect(Collectors.toList());
  }
}
