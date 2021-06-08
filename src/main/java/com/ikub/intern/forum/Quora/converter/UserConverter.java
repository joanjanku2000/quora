package com.ikub.intern.forum.Quora.converter;

import com.ikub.intern.forum.Quora.dto.user.UserCreateRequest;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.dto.user.UserDtoMini;
import com.ikub.intern.forum.Quora.dto.user.UserUpdateRequest;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.utils.Roles;
import com.ikub.intern.forum.Quora.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserConverter {

    public static UserEntity toEntity(UserCreateRequest userCreateRequest){
      if(userCreateRequest.getEmail().isEmpty() || userCreateRequest.getFirstName().isEmpty()
              || userCreateRequest.getUsername().isEmpty()  || userCreateRequest.getGender().isEmpty()
                || userCreateRequest.getLastName().isEmpty()){
            throw new BadRequestException("ALl fields must be completed");
        }

        UserEntity userEntity = new UserEntity(userCreateRequest.getFirstName(),userCreateRequest.getLastName()
                ,userCreateRequest.getEmail(),userCreateRequest.getGender(),userCreateRequest.getUsername()
                ,LocalDate.parse(userCreateRequest.getBirthday()),LocalDateTime.now(),Roles.USER.value, true);
        return userEntity;
    }

    public static void updateDtoToEntity(UserEntity userEntity, UserUpdateRequest userUpdateRequest){
        userEntity.setUsername(userUpdateRequest.getUsername());
        userEntity.setBirthday(LocalDate.parse(userUpdateRequest.getBirthday()));
        userEntity.setLastName(userUpdateRequest.getLastName());
        userEntity.setFirstName(userUpdateRequest.getFirstName());
        userEntity.setUpdatedAt(LocalDateTime.now());
    }
    public static UserDto entityToDto(UserEntity userEntity){
        return new UserDto(userEntity.getId(),userEntity.getFirstName(),userEntity.getLastName(),userEntity.getEmail(),
        userEntity.getUsername(),userEntity.getGender(),userEntity.getBirthday(),userEntity.getUserRole());
    }
    public static UserDtoMini entitySetToDtoMiniSet(UserEntity userEntity){
        return new UserDtoMini(userEntity.getFirstName(),
                userEntity.getLastName(),userEntity.getEmail(),userEntity.getUsername()
            );
    }
    public static Set<UserDtoMini> entitySetToDtoMiniSet(Set<UserEntity> userEntities){
        Set<UserDtoMini> set = new HashSet<>();
        userEntities.forEach((user)->{
            set.add(entitySetToDtoMiniSet(user));
        });
        return set;
    }
    public static Set<UserDto> entitySetToDtoSet(Set<UserEntity> userEntities){
        Set<UserDto> set = new HashSet<>();
        userEntities.forEach((user)->{
            set.add(entityToDto(user));
        });
        return set;
    }
    public static List<UserDto> entityListToDtoList(List<UserEntity> userEntities){
        List<UserDto> set = new ArrayList<>();
        userEntities.forEach((user)->{
            set.add(entityToDto(user));
        });
        return set;
    }
    public static Page<UserDto> entityPageToDtoPage(Page<UserEntity> userEntities){
        Set<UserEntity> set = Utils.listToSet(userEntities.getContent());
        List<UserDto> list = Utils.setToList(entitySetToDtoSet(set));
        return new PageImpl<>(list, userEntities.getPageable(),list.size());
    }
}
