package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.dto.user.UserCreateRequest;
import com.ikub.intern.forum.Quora.dto.user.UserUpdateRequest;
import com.ikub.intern.forum.Quora.entities.*;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.repository.QuestionsRepo;
import com.ikub.intern.forum.Quora.repository.ReplyRepo;
import com.ikub.intern.forum.Quora.repository.UpvoteQuestionRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTests {
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("Save user -> Success")
    void save(){
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("firstname","lastName",
                        "test","username","male","2000-02-04");
        UserEntity userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity.setId(1L);
        userService.saveUser(userCreateRequest);
        verify(userRepo,times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Save user -> fail -> email exists")
    void saveFail1(){
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("firstname","lastName",
                        "test","username","male","2000-02-04");
        UserEntity userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity.setId(1L);
        Mockito.when(userRepo.findByEmail(userCreateRequest.getEmail())).thenReturn(userEntity);
        Assertions.assertThrows(BadRequestException.class,()->userService.saveUser(userCreateRequest));
    }
    @Test
    @DisplayName("Save user -> fail -> username exists")
    void saveFail2(){
        UserCreateRequest userCreateRequest =
                new UserCreateRequest("firstname","lastName",
                        "test","username","male","2000-02-04");
        UserEntity userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        Mockito.when(userRepo.findByUsername(userCreateRequest.getUsername())).thenReturn(userEntity);
        Assertions.assertThrows(BadRequestException.class,()->userService.saveUser(userCreateRequest));
    }

    @Test
    @DisplayName("Update User -> success ")
    void update(){
        UserEntity userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity.setId(1L);
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("firstname","lastName",
                "testEmailDifferent","username","male","2000-02-04");
        when(userRepo.findById(userEntity.getId())).thenReturn(java.util.Optional.ofNullable(userEntity));
        userService.update(userEntity.getId(),userUpdateRequest);
        verify(userRepo,times(1)).save(userEntity);
    }
    @Test
    @DisplayName("Update User -> fail -> user does not exist ")
    void updateFail1(){
        UserEntity userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity.setId(1L);
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("firstname","lastName",
                "testEmailDifferent","username","male","2000-02-04");
       Assertions.assertThrows(BadRequestException.class,()->userService.update(userEntity.getId(),userUpdateRequest));
    }
    @Test
    @DisplayName("Update User -> fail -> username exists ")
    void updateFail2() {
        UserEntity userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity.setId(1L);
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("firstname","lastName",
                "testEmailDifferent","username","male","2000-02-04");
        when(userRepo.findById(userEntity.getId())).thenReturn(java.util.Optional.ofNullable(userEntity));
        when(userRepo.findByUsername(userUpdateRequest.getUsername())).thenReturn(userEntity);
        Assertions.assertThrows(BadRequestException.class,()->userService.update(userEntity.getId(),userUpdateRequest));
    }
    @Test
    @DisplayName("Update User -> fail -> email exists ")
    void updateFail3() {
        UserEntity userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        userEntity.setId(1L);
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("firstname","lastName",
                "testEmailDifferent","username","male","2000-02-04");
        UserEntity userEntity2  = new UserEntity("test","test","different",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);

        userEntity2.setId(2L);
        when(userRepo.findById(userEntity.getId())).thenReturn(java.util.Optional.ofNullable(userEntity2));
        when(userRepo.findByEmail(userUpdateRequest.getEmail())).thenReturn(userEntity);
        Assertions.assertThrows(BadRequestException.class,
                ()->userService.update(userEntity.getId(),userUpdateRequest));
    }
}
