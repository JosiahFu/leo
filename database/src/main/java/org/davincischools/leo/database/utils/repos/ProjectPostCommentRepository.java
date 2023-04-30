package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ProjectPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostCommentRepository extends JpaRepository<ProjectPostComment, Integer> {}
