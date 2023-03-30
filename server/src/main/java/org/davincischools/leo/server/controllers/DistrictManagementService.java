package org.davincischools.leo.server.controllers;

import java.util.Optional;
import org.davincischools.leo.database.daos.District;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.district_management.AddDistrictRequest;
import org.davincischools.leo.protos.district_management.DistrictInformationResponse;
import org.davincischools.leo.protos.district_management.GetDistrictsRequest;
import org.davincischools.leo.protos.district_management.RemoveDistrictRequest;
import org.davincischools.leo.protos.district_management.UpdateDistrictRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DistrictManagementService {

  @Autowired Database db;

  @PostMapping(value = "/api/protos/DistrictManagementService/GetDistricts")
  @ResponseBody
  public DistrictInformationResponse getDistricts(
      @RequestBody Optional<GetDistrictsRequest> request) {
    request = Optional.of(request.orElse(GetDistrictsRequest.getDefaultInstance()));
    return getAllDistricts(null);
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/AddDistrict")
  @ResponseBody
  public DistrictInformationResponse addDistrict(
      @RequestBody Optional<AddDistrictRequest> request) {
    request = Optional.of(request.orElse(AddDistrictRequest.getDefaultInstance()));

    if (request.get().hasDistrict()) {
      District district = new District().setName(request.get().getDistrict());
      db.getDistrictRepository().save(district);
      return getAllDistricts(district.getId());
    }

    return getAllDistricts(null);
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/UpdateDistrict")
  @ResponseBody
  public DistrictInformationResponse updateDistrict(
      @RequestBody Optional<UpdateDistrictRequest> request) {
    request = Optional.of(request.orElse(UpdateDistrictRequest.getDefaultInstance()));

    if (request.get().hasDistrictId()) {
      db.getDistrictRepository().save(new District().setId(request.get().getDistrictId()).setName(request.get().getDistrict()));
      return getAllDistricts(request.get().getDistrictId());
    }

    return getAllDistricts(null);
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/RemoveDistrict")
  @ResponseBody
  public DistrictInformationResponse removeDistrict(
      @RequestBody Optional<RemoveDistrictRequest> request) {
    request = Optional.of(request.orElse(RemoveDistrictRequest.getDefaultInstance()));

    if (request.get().hasDistrictId()) {
      db.getDistrictRepository().deleteById(request.get().getDistrictId());
    }

    return getAllDistricts(null);
  }

  private DistrictInformationResponse getAllDistricts(Integer modifiedDistrictId) {
    DistrictInformationResponse.Builder response = DistrictInformationResponse.newBuilder();
    response.setModifiedDistrictId(modifiedDistrictId != null ? modifiedDistrictId : -1);

    for (District district : db.getDistrictRepository().findAll()) {
      response.putDistricts(district.getId(), district.getName());
    }

    return response.build();
  }
}
