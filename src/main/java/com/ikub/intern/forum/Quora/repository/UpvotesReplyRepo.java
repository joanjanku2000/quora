package com.ikub.intern.forum.Quora.repository;

import com.ikub.intern.forum.Quora.entities.UpvotesQuestion;
import com.ikub.intern.forum.Quora.entities.UpvotesReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UpvotesReplyRepo extends JpaRepository<UpvotesReply,Long> {
    public List<UpvotesReply> findAllByReplyId(Long id);
    public Optional<UpvotesReply> findByReplyIdAndUserId(Long replyId, Long userId);
}
