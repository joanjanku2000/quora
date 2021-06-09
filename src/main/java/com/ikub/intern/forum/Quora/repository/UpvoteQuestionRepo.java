package com.ikub.intern.forum.Quora.repository;

import com.ikub.intern.forum.Quora.entities.UpvotesQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UpvoteQuestionRepo extends JpaRepository<UpvotesQuestion,Long> {
    List<UpvotesQuestion> findAllByQuestionId(Long id);
    Optional<UpvotesQuestion> findByQuestionIdAndUserId(Long questionId, Long userId);
}
