package com.ikub.intern.forum.Quora.service;


import com.ikub.intern.forum.Quora.converter.GroupConverter;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.entities.*;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotFoundException;
import com.ikub.intern.forum.Quora.repository.CategoryRepo;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.UserGroupRepo;
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
import java.util.HashSet;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupTests {
    @Mock
    private UserGroupRepo groupRepo;
    @Mock
    private CategoryRepo categoryRepo;
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private GroupService groupService;

    static GroupDtoForCreateUpdate groupDtoForCreateUpdate ;
    static Set<QuestionEntity> questionEntitySet;

    @BeforeAll
    static void  initialize(){
        groupDtoForCreateUpdate = new GroupDtoForCreateUpdate("test","dedsc",1l);
        questionEntitySet = new HashSet<>();
    }

    @Test
    @DisplayName("Saving group success")
    void saveGroup(){
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        CategoryEntity categoryEntity = instantiators.getCategory();
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(categoryRepo.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        when(groupRepo.findByGroupName(groupDtoForCreateUpdate.getGroupName())).thenReturn(Optional.ofNullable(null));

        UserGroupEntity userGroupEntity
                = GroupConverter.toEntity(groupDtoForCreateUpdate,categoryEntity);
        userGroupEntity.setId(2l);

        when(groupRepo.save(any(UserGroupEntity.class))).thenReturn(userGroupEntity);

        groupService.createGroup(userEntity.getId(),groupDtoForCreateUpdate);
        verify(groupRepo,times(1)).save(any(UserGroupEntity.class));
        verify(userRepo,times(1)).addUserToGroup(userEntity.getId(),userGroupEntity.getId());
        verify(userRepo,times(1)).activateUserMembership(userEntity.getId(),userGroupEntity.getId());
    }

    @Test
    @DisplayName("Saving group fail user not exist")
    void saveGroupFail1(){
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        CategoryEntity categoryEntity = instantiators.getCategory();

        when(categoryRepo.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        Assertions.assertThrows(NotFoundException.class, () -> {
            groupService.createGroup(userEntity.getId(),groupDtoForCreateUpdate);
        });
    }
    @Test
    @DisplayName("Saving group fail Category doesn't not exist")
    void saveGroupFail2(){
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();

        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        Assertions.assertThrows(BadRequestException.class, () -> {
            groupService.createGroup(userEntity.getId(),groupDtoForCreateUpdate);
        });

    }
    @Test
    @DisplayName("Saving group fail, duplicate group name")
    void saveGroupFail3(){
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        CategoryEntity categoryEntity = instantiators.getCategory();

        when(categoryRepo.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        UserGroupEntity userGroupEntity = GroupConverter.toEntity(groupDtoForCreateUpdate,categoryEntity);

        when(groupRepo.findByGroupName(groupDtoForCreateUpdate.getGroupName())).thenReturn(Optional.of(userGroupEntity));
        Assertions.assertThrows(BadRequestException.class, () -> {
            groupService.createGroup(userEntity.getId(),groupDtoForCreateUpdate);
        });
    }

    @Test
    @DisplayName("Update passes")
    void updateGroup() {
        Instantiators instantiators = new Instantiators();
        UserEntity userEntity = instantiators.getUser();
        CategoryEntity categoryEntity = instantiators.getCategory();

        when(categoryRepo.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        when(groupRepo.findByGroupName(groupDtoForCreateUpdate.getGroupName())).thenReturn(Optional.ofNullable(null));
        UserGroupEntity userGroupEntity
                = GroupConverter.toEntity(groupDtoForCreateUpdate, categoryEntity);
        userGroupEntity.setId(2l);
        userGroupEntity.setAdmin(userEntity);
        userGroupEntity.setQuestions(questionEntitySet);
        when(groupRepo.findById(userGroupEntity.getId())).thenReturn(Optional.of(userGroupEntity));
        when(groupRepo.save(any(UserGroupEntity.class))).thenReturn(userGroupEntity);

        Assertions.assertEquals(groupService.updateGroup(userGroupEntity.getId(), groupDtoForCreateUpdate).getGroupName()
                , userGroupEntity.getGroupName());

    }

}
