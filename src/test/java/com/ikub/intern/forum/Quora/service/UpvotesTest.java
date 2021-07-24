package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.dto.question.QuestionCreateRequest;
import com.ikub.intern.forum.Quora.entities.*;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotAllowedException;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.UpvoteQuestionRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpvotesTest {
    @Mock
    private UpvoteQuestionRepo upvoteQuestionRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private QuestionsRepo questionsRepo;
    @InjectMocks
    private UpvotesService upvotesService;


    @Test
    @DisplayName("Save success")
    void save(){
        Instantiators instantiators =  new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        List<Long> tags = Arrays.asList(1l,2l);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L,tags);
        Set<QuestionEntity>  questionEntitySet = new HashSet<>();
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity,groupDtoForCreateUpdate,
                instantiators.getCategory(),questionEntitySet);
        TagEntity tagEntity1 = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        TagEntity tagEntity2 = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        Set<TagEntity> tagEntities = new HashSet<>();
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        QuestionEntity questionEntity
                = instantiators.getQuestion(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        UpvotesQuestion upvotesQuestion =  new UpvotesQuestion(userEntity,true,
                LocalDateTime.of(2021,4,5,5,5),questionEntity);
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.ofNullable(userEntity));
        when(questionsRepo.findById(questionEntity.getId())).thenReturn(Optional.ofNullable(questionEntity));
        when(userRepo.findGroupsOfUser(userEntity.getId())).thenReturn(Arrays.asList(userGroupEntity));
        when(upvoteQuestionRepo.findByQuestionIdAndUserId(questionEntity.getId()
                ,userEntity.getId())).thenReturn(Optional.ofNullable(upvotesQuestion));
        upvotesService.upvoteQuestion(userEntity.getId(),questionEntity.getId());
        verify(upvoteQuestionRepo,times(1)).save(upvotesQuestion);
    }

    @Test
    @DisplayName("Save fail: user not found")
    void saveFailUserNotFound(){
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        List<Long> tags = Arrays.asList(1l,2l);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L,tags);
        Set<QuestionEntity>  questionEntitySet = new HashSet<>();
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity,groupDtoForCreateUpdate,
                instantiators.getCategory(),questionEntitySet);
        TagEntity tagEntity1 = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        TagEntity tagEntity2 = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        Set<TagEntity> tagEntities = new HashSet<>();
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        QuestionEntity questionEntity
                = instantiators.getQuestion(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        Assertions.assertThrows(BadRequestException.class,
                () -> upvotesService.upvoteQuestion(userEntity.getId(),questionEntity.getId()));
    }

    @Test
    @DisplayName("Save fail: question not found")
    void saveFailQuestionNotFound(){
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        List<Long> tags = Arrays.asList(1l,2l);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L,tags);
        Set<QuestionEntity>  questionEntitySet = new HashSet<>();
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity,groupDtoForCreateUpdate,
                instantiators.getCategory(),questionEntitySet);
        TagEntity tagEntity1 = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        TagEntity tagEntity2 = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        Set<TagEntity> tagEntities = new HashSet<>();
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        QuestionEntity questionEntity
                = instantiators.getQuestion(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.ofNullable(userEntity));
        Assertions.assertThrows(BadRequestException.class,
                () -> upvotesService.upvoteQuestion(userEntity.getId(),questionEntity.getId()));
    }

    @Test
    @DisplayName("Save fail: user not a member")
    void saveFailUserNotMember(){
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        List<Long> tags = Arrays.asList(1l,2l);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L,tags);
        Set<QuestionEntity>  questionEntitySet = new HashSet<>();
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity,groupDtoForCreateUpdate,
                instantiators.getCategory(),questionEntitySet);
        TagEntity tagEntity1 = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        TagEntity tagEntity2 = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        Set<TagEntity> tagEntities = new HashSet<>();
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        QuestionEntity questionEntity
                = instantiators.getQuestion(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.ofNullable(userEntity));
        when(questionsRepo.findById(questionEntity.getId())).thenReturn(Optional.ofNullable(questionEntity));
        when(userRepo.findGroupsOfUser(userEntity.getId())).thenReturn(Arrays.asList(new UserGroupEntity()));
        Assertions.assertThrows(NotAllowedException.class,
                () -> upvotesService.upvoteQuestion(userEntity.getId(),questionEntity.getId()));
    }
}
