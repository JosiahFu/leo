package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.ClassX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassXRepository extends JpaRepository<ClassX, Integer> {}
