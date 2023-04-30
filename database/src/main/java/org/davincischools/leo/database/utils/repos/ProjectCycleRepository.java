package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectCycleRepository extends JpaRepository<ProjectCycle, Integer> {}
