package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {}
