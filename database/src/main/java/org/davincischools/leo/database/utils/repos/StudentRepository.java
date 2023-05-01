package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {}
