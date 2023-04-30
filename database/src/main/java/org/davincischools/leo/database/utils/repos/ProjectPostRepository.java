package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Integer> {}
