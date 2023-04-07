package org.davincischools.leo.server.controllers;

import java.util.Optional;
import org.davincischools.leo.database.daos.School;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.school_management.GetSchoolsRequest;
import org.davincischools.leo.protos.school_management.RemoveSchoolRequest;
import org.davincischools.leo.protos.school_management.SchoolInformationResponse;
import org.davincischools.leo.protos.school_management.UpsertSchoolRequest;
import org.davincischools.leo.server.utils.DataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SchoolManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/SchoolManagementService/GetSchools")
  @ResponseBody
  public SchoolInformationResponse getSchools(@RequestBody Optional<GetSchoolsRequest> request) {
    request = Optional.of(request.orElse(GetSchoolsRequest.getDefaultInstance()));
    return getAllSchools(request.get().getDistrictId(), -1);
  }

  @PostMapping(value = "/api/protos/SchoolManagementService/UpsertSchool")
  @ResponseBody
  public SchoolInformationResponse upsertSchool(
      @RequestBody Optional<UpsertSchoolRequest> request) {
    request = Optional.of(request.orElse(UpsertSchoolRequest.getDefaultInstance()));

    School school =
        new School()
            .setDistrict(
                db.getDistrictRepository()
                    .findById(request.get().getSchool().getDistrictId())
                    .orElseThrow())
            .setId(request.get().getSchool().hasId() ? request.get().getSchool().getId() : null)
            .setName(request.get().getSchool().getName())
            .setCity(request.get().getSchool().getCity());
    db.getSchoolRepository().save(school);

    return getAllSchools(school.getDistrict().getId(), school.getId());
  }

  @PostMapping(value = "/api/protos/SchoolManagementService/RemoveSchool")
  @ResponseBody
  public SchoolInformationResponse removeSchool(
      @RequestBody Optional<RemoveSchoolRequest> request) {
    request = Optional.of(request.orElse(RemoveSchoolRequest.getDefaultInstance()));

    db.getSchoolRepository().deleteById(request.get().getSchoolId());

    return getAllSchools(request.get().getDistrictId(), -1);
  }

  private SchoolInformationResponse getAllSchools(int districtId, int nextSchoolId) {
    SchoolInformationResponse.Builder response = SchoolInformationResponse.newBuilder();
    response.setDistrictId(districtId);
    response.setNextSchoolId(nextSchoolId);
    response.addAllSchools(DataAccess.getProtoSchoolsByDistrictId(db, districtId));
    return response.build();
  }
}
