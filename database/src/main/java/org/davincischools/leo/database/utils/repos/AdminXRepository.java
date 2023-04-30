package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.AdminX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminXRepository extends JpaRepository<AdminX, Integer> {}
