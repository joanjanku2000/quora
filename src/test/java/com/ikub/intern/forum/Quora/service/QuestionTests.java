package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.GroupConverter;
import com.ikub.intern.forum.Quora.converter.QuestionConverter;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.dto.question.QuestionCreateRequest;
import com.ikub.intern.forum.Quora.dto.question.QuestionUpdateRequest;
import com.ikub.intern.forum.Quora.entities.*;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotAllowedException;
import com.ikub.intern.forum.Quora.exceptions.NotFoundException;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.TagRepo;
import com.ikub.intern.forum.Quora.repository.UserGroupRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import com.ikub.intern.forum.Quora.utils.PageParams;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionTests {
    @Mock
    private UserGroupRepo groupRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private TagRepo tagRepo;
    @Mock
    private QuestionsRepo questionsRepo;
    @InjectMocks
    private QuestionService questionService;

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
        tagEntity1 = new TagEntity(1L,"tag1",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        tagEntity2 = new TagEntity(2L,"tag2",userEntity,LocalDateTime.of(2021,2,3,5,4),true);
        tags = Arrays.asList(1L, 2L);
        questionCreateRequest = new QuestionCreateRequest("question", 1L, 1L,tags);
        categoryEntity =  new CategoryEntity(1L,"name",userEntity
                ,LocalDateTime.of(2021,2,3,4,5),true);
        questionEntitySet = new HashSet<>();
        tagEntities = new HashSet<>();
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        upvotesQuestions = new HashSet<>();
        replyEntities = new HashSet<>();
        groupDtoForCreateUpdate = new GroupDtoForCreateUpdate("test","dedsc", 1L);
        userGroupEntity
                = GroupConverter.toEntity(groupDtoForCreateUpdate, categoryEntity);
        userGroupEntity.setId(1L);
        userGroupEntity.setAdmin(userEntity);
        userGroupEntity.setQuestions(questionEntitySet);
        questionEntity = QuestionConverter.toEntity(questionCreateRequest,userGroupEntity,userEntity,tagEntities);
    }

    @Test
    @DisplayName("Saving new question success")
    void save(){
        when(userRepo.findById(questionCreateRequest.getUserId())).thenReturn(Optional.of(userEntity));
        when(groupRepo.findById(questionCreateRequest.getGroupId())).thenReturn(Optional.of(userGroupEntity));
        when(userRepo.findUsersInGroup(userGroupEntity.getId())).thenReturn(Arrays.asList(userEntity,userEntity2));

        questionService.newQuestion(questionCreateRequest);
        verify(questionsRepo,times(1)).save(any(QuestionEntity.class));
    }

    @Test
    @DisplayName("Saving new question fail:user not exist")
    void saveFail1(){
        assertThrows(BadRequestException.class,()->questionService.newQuestion(questionCreateRequest));
    }

    @Test
    @DisplayName("Saving new question fail:group not exist")
    void saveFail2(){
        when(userRepo.findById(questionCreateRequest.getUserId())).thenReturn(Optional.of(userEntity));

        assertThrows(BadRequestException.class,()->questionService.newQuestion(questionCreateRequest));

    }
    @Test
    @DisplayName("Saving new question fail:user isn't part of the group")
    void saveFail3(){
        when(userRepo.findById(questionCreateRequest.getUserId())).thenReturn(Optional.of(userEntity));
        when(groupRepo.findById(questionCreateRequest.getGroupId())).thenReturn(Optional.of(userGroupEntity));

        assertThrows(NotAllowedException.class,()->questionService.newQuestion(questionCreateRequest));
    }

    @Test
    @DisplayName("Find questions in group test fail:group not found")
    void findAllInGroup(){
        PageParams params = new PageParams();
        Long id = 1L;
        assertThrows(NotFoundException.class,() -> questionService.findAllInAGroup(params,id));
    }
    @Test
    @DisplayName("Delete question fail: question not found")
    void deleteQuestion(){
        Long id = 1L;
        assertThrows(NotFoundException.class,() -> questionService.deleteQuestion(id));
    }

    @Test
    @DisplayName("Update question fail: question not found")
    void update(){
        Long id = 1L;
        assertThrows(NotFoundException.class,() -> questionService.updateQuestion(id,new QuestionUpdateRequest()));
    }
}
