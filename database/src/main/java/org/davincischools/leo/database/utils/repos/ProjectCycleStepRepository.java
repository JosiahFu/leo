package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectCycleStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectCycleStepRepository extends JpaRepository<ProjectCycleStep, Integer> {}
