package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.CategoryConverter;
import com.ikub.intern.forum.Quora.converter.GroupConverter;
import com.ikub.intern.forum.Quora.converter.QuestionConverter;
import com.ikub.intern.forum.Quora.converter.ReplyConverter;
import com.ikub.intern.forum.Quora.dto.category.CategoryCreateRequest;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.dto.question.QuestionCreateRequest;
import com.ikub.intern.forum.Quora.dto.reply.ReplyRequest;
import com.ikub.intern.forum.Quora.entities.*;
import org.apache.catalina.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class Instantiators {
    public UserEntity getUser(){
        UserEntity userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity.setId(1L);
        return userEntity;
    }
    public QuestionEntity getQuestion(QuestionCreateRequest questionCreateRequest, UserGroupEntity userGroupEntity,
                                      UserEntity userEntity, Set<TagEntity> tagEntities){
        return QuestionConverter.toEntity(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
    }
    public UpvotesQuestion getUpvotesQuestion(UserEntity userEntity,QuestionEntity questionEntity){
      return new UpvotesQuestion(userEntity,true,
                LocalDateTime.of(2021,4,5,5,5),questionEntity);
    }
    public UserGroupEntity getGroup(UserEntity userEntity, GroupDtoForCreateUpdate groupDtoForCreateUpdate
            ,CategoryEntity categoryEntity,Set<QuestionEntity> questionEntitySet){
        UserGroupEntity userGroupEntity
                = GroupConverter.toEntity(groupDtoForCreateUpdate, categoryEntity);
        userGroupEntity.setId(1L);
        userGroupEntity.setAdmin(userEntity);
        userGroupEntity.setQuestions(questionEntitySet);
        return userGroupEntity;
    }
    public CategoryEntity getCategory(){
        UserEntity userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        CategoryCreateRequest categoryCreateRequest = new CategoryCreateRequest("name");
        CategoryEntity categoryEntity =  CategoryConverter.toEntity(categoryCreateRequest,userEntity);;
        categoryEntity.setId(1L);
        return categoryEntity;
    }
    public ReplyEntity getReply(ReplyRequest replyRequest, UserEntity userEntity,QuestionEntity questionEntity){
       return ReplyConverter.toEntity(replyRequest,userEntity,questionEntity);
    }

}
