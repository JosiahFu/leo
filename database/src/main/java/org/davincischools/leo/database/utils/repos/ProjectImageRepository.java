package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectImage;
import org.davincischools.leo.database.daos.ProjectImageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectImageRepository extends JpaRepository<ProjectImage, ProjectImageId> {}
