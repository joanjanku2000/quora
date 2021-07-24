package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.GroupConverter;
import com.ikub.intern.forum.Quora.converter.QuestionConverter;
import com.ikub.intern.forum.Quora.converter.ReplyConverter;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.dto.question.QuestionCreateRequest;
import com.ikub.intern.forum.Quora.dto.reply.ReplyDto;
import com.ikub.intern.forum.Quora.dto.reply.ReplyRequest;
import com.ikub.intern.forum.Quora.entities.*;

import com.ikub.intern.forum.Quora.exceptions.NotAllowedException;
import com.ikub.intern.forum.Quora.exceptions.NotFoundException;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.ReplyRepo;

import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import org.apache.catalina.User;
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

    @Test
    @DisplayName("Reply save success")
    void save(){
        Instantiators instantiators = new Instantiators();

        UserEntity userEntity = instantiators.getUser();
        List<Long> tags = Arrays.asList(1l,2l);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L,tags);
        Set<QuestionEntity>  questionEntitySet = new HashSet<>();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate
                = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity,groupDtoForCreateUpdate,
                instantiators.getCategory(),questionEntitySet);
        TagEntity tagEntity1
                = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        TagEntity tagEntity2
                = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        Set<TagEntity> tagEntities = new HashSet<>();
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        QuestionEntity questionEntity
                = instantiators.getQuestion(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        UserEntity userEntity2 = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity2.setId(2L);
        ReplyRequest replyRequest = new ReplyRequest("reply");
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(questionsRepo.findById(questionEntity.getId())).thenReturn(Optional.of(questionEntity));
        when(userRepo.findUsersInGroup(questionEntity.getGroup().getId())).thenReturn(Arrays.asList(userEntity,userEntity2));
        replyService.save(userEntity.getId(),questionEntity.getId(),replyRequest);
        verify(replyRepo,times(1)).save(any(ReplyEntity.class));
    }
    @Test
    @DisplayName("Reply save fail:user not found")
    void saveFailUserNotFound(){
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        Set<QuestionEntity>  questionEntitySet = new HashSet<>();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate
                = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity,groupDtoForCreateUpdate,
                instantiators.getCategory(),questionEntitySet);
        TagEntity tagEntity1
                = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        TagEntity tagEntity2
                = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        Set<TagEntity> tagEntities = new HashSet<>();
        List<Long> tags = Arrays.asList(1l,2l);
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L,tags);
        QuestionEntity questionEntity
                = instantiators.getQuestion(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        ReplyRequest replyRequest = new ReplyRequest("reply");
        Assertions.assertThrows(NotFoundException.class,
                ()->replyService.save(userEntity.getId(),questionEntity.getId(),replyRequest));
    }
    @Test
    @DisplayName("Reply save fail:question not found")
    void saveFail2(){
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        Set<QuestionEntity>  questionEntitySet = new HashSet<>();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate
                = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity,groupDtoForCreateUpdate,
                instantiators.getCategory(),questionEntitySet);
        TagEntity tagEntity1
                = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        TagEntity tagEntity2
                = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        Set<TagEntity> tagEntities = new HashSet<>();
        List<Long> tags = Arrays.asList(1l,2l);
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L,tags);
        QuestionEntity questionEntity
                = instantiators.getQuestion(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        ReplyRequest replyRequest = new ReplyRequest("reply");
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        Assertions.assertThrows(NotFoundException.class,
                ()->replyService.save(userEntity.getId(),questionEntity.getId(),replyRequest));
    }
    @Test
    @DisplayName("Reply save fail:User not allowed to reply")
    void saveFail3(){
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        Set<QuestionEntity>  questionEntitySet = new HashSet<>();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate
                = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity,groupDtoForCreateUpdate,
                instantiators.getCategory(),questionEntitySet);
        TagEntity tagEntity1
                = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        TagEntity tagEntity2
                = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        Set<TagEntity> tagEntities = new HashSet<>();
        List<Long> tags = Arrays.asList(1l,2l);
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L,tags);
        QuestionEntity questionEntity
                = instantiators.getQuestion(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        ReplyRequest replyRequest = new ReplyRequest("reply");
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(questionsRepo.findById(questionEntity.getId())).thenReturn(Optional.of(questionEntity));
        Assertions.assertThrows(NotAllowedException.class,
                ()->replyService.save(userEntity.getId(),questionEntity.getId(),replyRequest));
    }
    @Test
    @DisplayName("Update save Fail:user not allowed")
    void update(){
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        Set<QuestionEntity>  questionEntitySet = new HashSet<>();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate
                = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity,groupDtoForCreateUpdate,
                instantiators.getCategory(),questionEntitySet);
        TagEntity tagEntity1
                = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        TagEntity tagEntity2
                = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        Set<TagEntity> tagEntities = new HashSet<>();
        List<Long> tags = Arrays.asList(1l,2l);
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L,tags);
        QuestionEntity questionEntity
                = instantiators.getQuestion(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        ReplyRequest replyRequest = new ReplyRequest("reply");
        UserEntity userEntity2 = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity2.setId(2L);
        ReplyEntity replyEntity = instantiators.getReply(replyRequest,userEntity,questionEntity);

        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity2));
        when(replyRepo.findById(replyEntity.getId())).thenReturn(Optional.ofNullable(replyEntity)) ;
        Assertions.assertThrows(NotAllowedException.class,
                () -> replyService.update(userEntity.getId(),replyEntity.getId(),replyRequest));
    }

    @Test
    @DisplayName("Delete Fail:user not allowed")
    void delete(){
        UserEntity userEntity2 = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity2.setId(2L);
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        Set<QuestionEntity>  questionEntitySet = new HashSet<>();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate
                = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity,groupDtoForCreateUpdate,
                instantiators.getCategory(),questionEntitySet);
        TagEntity tagEntity1
                = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        TagEntity tagEntity2
                = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        Set<TagEntity> tagEntities = new HashSet<>();
        List<Long> tags = Arrays.asList(1l,2l);
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L,tags);
        QuestionEntity questionEntity
                = instantiators.getQuestion(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        ReplyRequest replyRequest = new ReplyRequest("reply");
        ReplyEntity replyEntity = instantiators.getReply(replyRequest,userEntity,questionEntity);
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity2));
        when(replyRepo.findById(replyEntity.getId())).thenReturn(Optional.ofNullable(replyEntity)) ;

        Assertions.assertThrows(NotAllowedException.class,
                () -> replyService.deleteReply(userEntity.getId(),replyEntity.getId()));
    }

}
