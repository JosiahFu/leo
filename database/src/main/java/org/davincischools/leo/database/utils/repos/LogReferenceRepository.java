package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.LogReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogReferenceRepository extends JpaRepository<LogReference, Integer> {}
