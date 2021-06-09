package com.ikub.intern.forum.Quora.converter;

import com.ikub.intern.forum.Quora.dto.question.QuestionCreateRequest;
import com.ikub.intern.forum.Quora.dto.question.QuestionDto;
import com.ikub.intern.forum.Quora.entities.*;
import com.ikub.intern.forum.Quora.utils.Utils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class QuestionConverter {
    private static Set<UserEntity> upvoteTableToUserSet(Set<UpvotesQuestion> upvotesQuestions) {
        Set<UserEntity> userEntities = new HashSet<>();
        upvotesQuestions.forEach((upvotesQuestion -> {
            userEntities.add(upvotesQuestion.getUser());
        }));
        return userEntities;
    }
        public static QuestionEntity toEntity(QuestionCreateRequest questionCreateRequest,
                                              UserGroupEntity userGroupEntity, UserEntity userEntity,Set<TagEntity> tags){
            return new QuestionEntity(questionCreateRequest.getQuestion(),userEntity,
            userGroupEntity,LocalDateTime.now(),true,tags);
    }
    public static QuestionDto entityToDto(QuestionEntity questionEntity){

        return new QuestionDto(questionEntity.getId(),questionEntity.getQuestion()
                ,questionEntity.getUser().getUsername(),
                questionEntity.getDate(),questionEntity.getUpdatedAt()
                ,TagConverter.entitySetToDtoSet(questionEntity.getTagList())
                ,UserConverter.entitySetToDtoMiniSet(upvoteTableToUserSet(questionEntity.getUpvotesQuestion())),
               ReplyConverter.entitySetToDto(questionEntity.getReplies())
        );
    }
    public static Set<QuestionDto> entitySetToDtoSet(Set<QuestionEntity> questionEntities){
        Set<QuestionDto> questionDtos = new HashSet<>();
        questionEntities.forEach((questionEntity -> {
            questionDtos.add(entityToDto(questionEntity));
        }));
        return questionDtos;
    }
    public static Page<QuestionDto> entityPageToDtoPage(Page<QuestionEntity> questionEntities){
        Page<QuestionDto> dtoPage = questionEntities.map(
                new Function<QuestionEntity,QuestionDto>(){
                    @Override
                    public QuestionDto apply(QuestionEntity entity){
                        return entityToDto(entity);
                    }
                }
        );
        return dtoPage;
    }
}
