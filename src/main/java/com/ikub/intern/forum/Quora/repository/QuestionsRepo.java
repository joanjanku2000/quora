package com.ikub.intern.forum.Quora.repository;

import com.ikub.intern.forum.Quora.entities.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionsRepo extends JpaRepository<QuestionEntity,Long>,QuestionRepoCustom {
     Page<QuestionEntity> findAllByGroupId(Long id, Pageable pageable);

}
