package org.davincischools.leo.server.controllers;

import java.util.Optional;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.database.utils.Database.StudentRepository.StudentAssignment;
import org.davincischools.leo.protos.class_management.GetStudentAssignmentsRequest;
import org.davincischools.leo.protos.class_management.GetStudentAssignmentsResponse;
import org.davincischools.leo.server.utils.DataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClassManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/ClassManagementService/GetStudentAssignments")
  @ResponseBody
  public GetStudentAssignmentsResponse getStudentAssignments(
      @RequestBody Optional<GetStudentAssignmentsRequest> optionalRequest) {
    var request = optionalRequest.orElse(GetStudentAssignmentsRequest.getDefaultInstance());
    var response = GetStudentAssignmentsResponse.newBuilder();

    for (StudentAssignment assignment :
        db.getStudentRepository().findAllAssignmentsByStudentUserId(request.getUserId())) {
      response.addAssignments(
          DataAccess.convertAssignmentToProto(assignment.classField(), assignment.assignment()));
    }

    return response.build();
  }
}
