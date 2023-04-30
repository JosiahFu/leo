package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {}
