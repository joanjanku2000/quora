package com.ikub.intern.forum.Quora.repository;

import com.ikub.intern.forum.Quora.entities.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepo extends JpaRepository<ReplyEntity,Long> {
    //@Query("Select reply from ReplyEntity reply where reply.question.id = :id order by reply.createdAt")
    Page<ReplyEntity> findAllByQuestionIdAndActiveTrueOrderByCreatedAtDesc(Long id, Pageable pageable);
}
