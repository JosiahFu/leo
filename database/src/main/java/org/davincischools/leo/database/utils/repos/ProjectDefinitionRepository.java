package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDefinitionRepository extends JpaRepository<ProjectDefinition, Integer> {}
