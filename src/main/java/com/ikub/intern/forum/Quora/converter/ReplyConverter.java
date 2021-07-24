package com.ikub.intern.forum.Quora.converter;

import com.ikub.intern.forum.Quora.dto.reply.ReplyDto;
import com.ikub.intern.forum.Quora.dto.reply.ReplyRequest;
import com.ikub.intern.forum.Quora.entities.QuestionEntity;
import com.ikub.intern.forum.Quora.entities.ReplyEntity;
import com.ikub.intern.forum.Quora.entities.UpvotesReply;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ReplyConverter {
    public static ReplyEntity toEntity(ReplyRequest replyCreateRequest, UserEntity user, QuestionEntity questionEntity) {
        return new ReplyEntity(user, questionEntity, replyCreateRequest.getReply()
                , true, LocalDateTime.now());
    }

    public static Set<UserEntity> upvoteTableToUserSet(Set<UpvotesReply> upvotesReplies) {
        Set<UserEntity> userEntities = new HashSet<>();
        upvotesReplies.forEach((upvotesReply -> userEntities.add(upvotesReply.getUser())));
        return userEntities;
    }

    public static ReplyDto entityToDto(ReplyEntity replyEntity) {

        return new ReplyDto(replyEntity.getId(),
                replyEntity.getReply(), UserConverter.entitySetToDtoMiniSet(replyEntity.getUser()),
                replyEntity.getCreatedAt(), replyEntity.getUpdatedAt()
                , UserConverter.entitySetToDtoMiniSet(upvoteTableToUserSet(replyEntity.getUpvotesReplyList())));
    }

    public static Set<ReplyDto> entitySetToDto(Set<ReplyEntity> replyEntities) {
        Set<ReplyDto> replyDtos = new HashSet<>();
        replyEntities.forEach((reply) ->
                replyDtos.add(entityToDto(reply)));
        return replyDtos;
    }

    public static Page<ReplyDto> entityPageToDtoPage(Page<ReplyEntity> replyEntities) {
        return replyEntities.map(
                new Function<ReplyEntity, ReplyDto>() {
                    @Override
                    public ReplyDto apply(ReplyEntity entity) {
                        return entityToDto(entity);
                    }
                }
        );
    }
}
