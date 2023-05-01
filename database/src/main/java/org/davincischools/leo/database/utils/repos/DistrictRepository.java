package org.davincischools.leo.database.utils.repos;

import java.util.Optional;
import org.davincischools.leo.database.daos.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {

  Optional<District> findByName(String name);
}
