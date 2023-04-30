package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {

  Iterable<School> findAllByDistrictId(Integer districtId);
}
