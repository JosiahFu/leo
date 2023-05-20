package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.Motivation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivationRepository extends JpaRepository<Motivation, Integer> {}
