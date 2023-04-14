package org.davincischools.leo.server.controllers;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
    return getAllDistricts(-1);
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/AddDistrict")
  @ResponseBody
  public DistrictInformationResponse addDistrict(
      @RequestBody Optional<AddDistrictRequest> request) {
    request = Optional.of(request.orElse(AddDistrictRequest.getDefaultInstance()));

    if (request.get().hasDistrict()) {
      District district =
          new District()
              .setCreationTime(Instant.now())
              .setName(request.get().getDistrict().getName());
      db.getDistrictRepository().save(district);
      return getAllDistricts(district.getId());
    }

    return getAllDistricts(-1);
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/UpdateDistrict")
  @ResponseBody
  public DistrictInformationResponse updateDistrict(
      @RequestBody Optional<UpdateDistrictRequest> request) {
    request = Optional.of(request.orElse(UpdateDistrictRequest.getDefaultInstance()));

    if (request.get().getDistrict().hasId()) {
      db.getDistrictRepository()
          .save(
              new District()
                  .setCreationTime(Instant.now())
                  .setId(request.get().getDistrict().getId())
                  .setName(request.get().getDistrict().getName()));
      return getAllDistricts(request.get().getDistrict().getId());
    }

    return getAllDistricts(-1);
  }

  @PostMapping(value = "/api/protos/DistrictManagementService/RemoveDistrict")
  @ResponseBody
  public DistrictInformationResponse removeDistrict(
      @RequestBody Optional<RemoveDistrictRequest> request) {
    request = Optional.of(request.orElse(RemoveDistrictRequest.getDefaultInstance()));

    if (request.get().hasDistrictId()) {
      db.getDistrictRepository().deleteById(request.get().getDistrictId());
    }

    return getAllDistricts(-1);
  }

  private DistrictInformationResponse getAllDistricts(int modifiedDistrictId) {
    DistrictInformationResponse.Builder response = DistrictInformationResponse.newBuilder();

    response.setModifiedDistrictId(modifiedDistrictId);
    response.addAllDistricts(
        StreamSupport.stream(db.getDistrictRepository().findAll().spliterator(), false)
            .map(
                district ->
                    org.davincischools.leo.protos.district_management.District.newBuilder()
                        .setId(district.getId())
                        .setName(district.getName())
                        .build())
            .collect(Collectors.toList()));

    return response.build();
  }
}
