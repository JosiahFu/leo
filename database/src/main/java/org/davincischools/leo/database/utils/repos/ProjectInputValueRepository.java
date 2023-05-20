package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectInputValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectInputValueRepository extends JpaRepository<ProjectInputValue, Integer> {}
