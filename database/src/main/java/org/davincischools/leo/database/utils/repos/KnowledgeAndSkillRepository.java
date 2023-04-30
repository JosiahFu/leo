package org.davincischools.leo.database.utils.repos;

import org.davincischools.leo.database.daos.KnowledgeAndSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeAndSkillRepository extends JpaRepository<KnowledgeAndSkill, Integer> {}
