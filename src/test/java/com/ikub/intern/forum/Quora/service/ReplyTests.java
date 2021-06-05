package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.GroupConverter;
import com.ikub.intern.forum.Quora.converter.QuestionConverter;
import com.ikub.intern.forum.Quora.converter.ReplyConverter;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.dto.question.QuestionCreateRequest;

import com.ikub.intern.forum.Quora.dto.reply.ReplyRequest;
import com.ikub.intern.forum.Quora.entities.*;

import com.ikub.intern.forum.Quora.exceptions.NotAllowedException;
import com.ikub.intern.forum.Quora.exceptions.NotFoundException;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.ReplyRepo;

import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReplyTests {
    @Mock
    private ReplyRepo replyRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private QuestionsRepo questionsRepo;
    @InjectMocks
    private ReplyService replyService;

    static CategoryEntity categoryEntity;
    static UserEntity userEntity;
    static UserEntity userEntity2;
    static QuestionCreateRequest questionCreateRequest ;
    static Set<QuestionEntity> questionEntitySet;
    static List<Long> tags;
    static QuestionEntity questionEntity;
    static Set<ReplyEntity> replyEntities;
    static Set<UpvotesQuestion> upvotesQuestions;
    static Set<TagEntity> tagEntities;
    static GroupDtoForCreateUpdate groupDtoForCreateUpdate;
    static UserGroupEntity userGroupEntity;
    static TagEntity tagEntity1;
    static TagEntity tagEntity2;
    static ReplyEntity replyEntity;
    static ReplyRequest replyRequest;
    @BeforeAll
    static void  initialize(){
        userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity2 = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity.setId(1L);
        userEntity2.setId(2L);
        replyRequest = new ReplyRequest("reply");
        questionEntitySet = new HashSet<>();
        tagEntity1 = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        tagEntity2 = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        tagEntities = new HashSet<>();
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);

        tags = Arrays.asList(1L, 2L);
        questionCreateRequest = new QuestionCreateRequest("question", 1L, 1L,tags);
        upvotesQuestions = new HashSet<>();
        replyEntities = new HashSet<>();
        groupDtoForCreateUpdate = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        userGroupEntity
                = GroupConverter.toEntity(groupDtoForCreateUpdate, categoryEntity);
        userGroupEntity.setId(1L);
        userGroupEntity.setAdmin(userEntity);
        userGroupEntity.setQuestions(questionEntitySet);
        questionEntity = QuestionConverter.toEntity(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        replyEntity = ReplyConverter.toEntity(replyRequest,userEntity,questionEntity);
    }

    @Test
    @DisplayName("Reply save success")
    void save(){
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(questionsRepo.findById(questionEntity.getId())).thenReturn(Optional.of(questionEntity));
        when(userRepo.findUsersInGroup(questionEntity.getGroup().getId())).thenReturn(Arrays.asList(userEntity,userEntity2));
        replyService.save(userEntity.getId(),questionEntity.getId(),replyRequest);
        verify(replyRepo,times(1)).save(any(ReplyEntity.class));
    }
    @Test
    @DisplayName("Reply save fail:user not found")
    void saveFail1(){
        Assertions.assertThrows(NotFoundException.class,
                ()->replyService.save(userEntity.getId(),questionEntity.getId(),replyRequest));
    }
    @Test
    @DisplayName("Reply save fail:question not found")
    void saveFail2(){
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        Assertions.assertThrows(NotFoundException.class,
                ()->replyService.save(userEntity.getId(),questionEntity.getId(),replyRequest));
    }
    @Test
    @DisplayName("Reply save fail:User not allowed to reply")
    void saveFail3(){
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(questionsRepo.findById(questionEntity.getId())).thenReturn(Optional.of(questionEntity));
        Assertions.assertThrows(NotAllowedException.class,
                ()->replyService.save(userEntity.getId(),questionEntity.getId(),replyRequest));
    }
    @Test
    @DisplayName("Update save Fail:user not allowed")
    void update(){
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity2));
        when(replyRepo.findById(replyEntity.getId())).thenReturn(Optional.ofNullable(replyEntity)) ;

        Assertions.assertThrows(NotAllowedException.class,
                () -> replyService.update(userEntity.getId(),replyEntity.getId(),replyRequest));
    }

    @Test
    @DisplayName("Delete Fail:user not allowed")
    void delete(){
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity2));
        when(replyRepo.findById(replyEntity.getId())).thenReturn(Optional.ofNullable(replyEntity)) ;

        Assertions.assertThrows(NotAllowedException.class,
                () -> replyService.deleteReply(userEntity.getId(),replyEntity.getId()));
    }

}
