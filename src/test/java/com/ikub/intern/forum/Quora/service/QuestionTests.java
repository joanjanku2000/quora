package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.dto.question.QuestionCreateRequest;
import com.ikub.intern.forum.Quora.dto.question.QuestionUpdateRequest;
import com.ikub.intern.forum.Quora.entities.QuestionEntity;
import com.ikub.intern.forum.Quora.entities.TagEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotAllowedException;
import com.ikub.intern.forum.Quora.exceptions.NotFoundException;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.TagRepo;
import com.ikub.intern.forum.Quora.repository.UserReportsRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import com.ikub.intern.forum.Quora.utils.PageParams;
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
    TagRepo tagRepo;
    @Mock
    private UserReportsRepo groupRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private QuestionsRepo questionsRepo;
    @InjectMocks
    private QuestionService questionService;


    @Test
    @DisplayName("Saving new question success")
    void save() {
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate
                = new GroupDtoForCreateUpdate("test", "dedsc", 1L);
        List<Long> tags = Arrays.asList(1l, 2l);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L, tags);
        Set<QuestionEntity> questionEntitySet = new HashSet<>();
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity, groupDtoForCreateUpdate,
                instantiators.getCategory(), questionEntitySet);
        UserEntity userEntity2 = new UserEntity("test", "test", "test",
                "m", "usernameTest",
                LocalDate.ofYearDay(2000, 23),
                LocalDateTime.of(2021, 3, 1, 12, 1),
                "user", true);
        userEntity2.setId(2L);
        TagEntity tagEntity1
                = new TagEntity(1L, "tag1", userEntity,
                LocalDateTime.of(2021, 2, 3, 5, 4), true);
        TagEntity tagEntity2
                = new TagEntity(2L, "tag2", userEntity,
                LocalDateTime.of(2021, 2, 3, 5, 4), true);
        when(tagRepo.findById(1L)).thenReturn(Optional.of(tagEntity1));
        when(tagRepo.findById(1L)).thenReturn(Optional.of(tagEntity2));
        when(userRepo.findById(questionCreateRequest.getUserId())).thenReturn(Optional.of(userEntity));
        when(groupRepo.findById(questionCreateRequest.getGroupId())).thenReturn(Optional.of(userGroupEntity));
        when(userRepo.findUsersInGroup(userGroupEntity.getId())).thenReturn(Arrays.asList(userEntity, userEntity2));

        questionService.newQuestion(questionCreateRequest);
        verify(questionsRepo, times(1)).save(any(QuestionEntity.class));
    }

    @Test
    @DisplayName("Saving new question fail:user not exist")
    void saveFailUserDoesNotExist() {
        List<Long> tags = Arrays.asList(1l, 2l);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L, tags);
        assertThrows(BadRequestException.class, () -> questionService.newQuestion(questionCreateRequest));
    }

    @Test
    @DisplayName("Saving new question fail:group not exist")
    void saveFailGroupDoesNotExist() {
        Instantiators instantiators = new Instantiators();
        List<Long> tags = Arrays.asList(1l, 2l);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L, tags);
        UserEntity userEntity = instantiators.getUser();
        when(userRepo.findById(questionCreateRequest.getUserId())).thenReturn(Optional.of(userEntity));
        assertThrows(BadRequestException.class, () -> questionService.newQuestion(questionCreateRequest));

    }

    @Test
    @DisplayName("Saving new question fail:user isn't part of the group")
    void saveFailUserIsNotPartOfTheGroup() {
        Instantiators instantiators = new Instantiators();
        List<Long> tags = Arrays.asList(1l, 2l);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L, tags);
        UserEntity userEntity = instantiators.getUser();
        GroupDtoForCreateUpdate groupDtoForCreateUpdate
                = new GroupDtoForCreateUpdate("test", "dedsc", 1L);
        Set<QuestionEntity> questionEntitySet = new HashSet<>();
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity, groupDtoForCreateUpdate,
                instantiators.getCategory(), questionEntitySet);

        when(userRepo.findById(questionCreateRequest.getUserId())).thenReturn(Optional.of(userEntity));
        when(groupRepo.findById(questionCreateRequest.getGroupId())).thenReturn(Optional.of(userGroupEntity));

        assertThrows(NotAllowedException.class, () -> questionService.newQuestion(questionCreateRequest));
    }

    @Test
    @DisplayName("Find questions in group test fail:group not found")
    void findAllInGroup() {
        PageParams params = new PageParams();
        Long id = 1L;
        assertThrows(NotFoundException.class, () -> questionService.findAllInAGroup(params, id));
    }

    @Test
    @DisplayName("Delete question fail: question not found")
    void deleteQuestion() {
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        Long id = 1L;
        Long uid = 1L;
        when(userRepo.findById(uid)).thenReturn(Optional.of(userEntity));
        assertThrows(NotFoundException.class, () -> questionService.deleteQuestion(id, uid));
    }

    @Test
    @DisplayName("Update question fail: user not allowed to update it")
    void update() {
        UserEntity userEntity2 = new UserEntity("test", "test", "test",
                "m", "usernameTest",
                LocalDate.ofYearDay(2000, 23),
                LocalDateTime.of(2021, 3, 1, 12, 1),
                "user", true);
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        TagEntity tagEntity1
                = new TagEntity(1L, "tag1", userEntity, LocalDateTime.of(2021, 2, 3, 5, 4), true);
        TagEntity tagEntity2
                = new TagEntity(2L, "tag2", userEntity, LocalDateTime.of(2021, 2, 3, 5, 4), true);
        Set<TagEntity> tagEntities = new HashSet<>();
        List<Long> tags = Arrays.asList(1l, 2l);
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        QuestionCreateRequest questionCreateRequest
                = new QuestionCreateRequest("question", 1L, 1L, tags);
        GroupDtoForCreateUpdate groupDtoForCreateUpdate
                = new GroupDtoForCreateUpdate("test", "dedsc", 1L);
        Set<QuestionEntity> questionEntitySet = new HashSet<>();
        UserGroupEntity userGroupEntity = instantiators.getGroup(userEntity, groupDtoForCreateUpdate,
                instantiators.getCategory(), questionEntitySet);
        QuestionEntity questionEntity
                = instantiators.getQuestion(questionCreateRequest, userGroupEntity, userEntity, tagEntities);
        userEntity2.setId(2L);
        Long id = 1L;
        Long uid = 1L;
        when(userRepo.findById(uid)).thenReturn(Optional.of(userEntity2));
        when(questionsRepo.findById(id)).thenReturn(Optional.of(questionEntity));
        assertThrows(NotAllowedException.class, () -> questionService.updateQuestion(id, uid, new QuestionUpdateRequest()));
    }
}
