package com.ikub.intern.forum.Quora.converter;

import com.ikub.intern.forum.Quora.dto.group.GroupDto;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoMini;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.dto.user.UserDtoMini;
import com.ikub.intern.forum.Quora.entities.CategoryEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import com.ikub.intern.forum.Quora.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class GroupConverter {

    public static UserGroupEntity toEntity(GroupDtoForCreateUpdate groupDtoForCreate, CategoryEntity categoryEntity){
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setActive(true);
        userGroupEntity.setCategoryEntity(categoryEntity);
        userGroupEntity.setCreatedAt(LocalDateTime.now());
        userGroupEntity.setGroupName(groupDtoForCreate.getGroupName());
        userGroupEntity.setDescription(groupDtoForCreate.getDescription());
       return userGroupEntity;
    }
    public static GroupDto entityToDto(UserGroupEntity userGroupEntity){
        return new GroupDto(userGroupEntity.getId(), userGroupEntity.getGroupName(),
                userGroupEntity.getDescription(),userGroupEntity.getCreatedAt(),
                userGroupEntity.getUpdatedAt(),UserConverter.entitySetToDtoMiniSet(userGroupEntity.getAdmin())
                ,CategoryConverter.entityToDto(userGroupEntity.getCategoryEntity())
                ,QuestionConverter.entitySetToDtoSet(userGroupEntity.getQuestions()));
    }
    public static GroupDtoMini entityToDtoMini(UserGroupEntity userGroupEntity){
        return new GroupDtoMini(userGroupEntity.getId(),userGroupEntity.getGroupName(),userGroupEntity.getDescription()
                ,UserConverter.entitySetToDtoMiniSet(userGroupEntity.getAdmin()),CategoryConverter.entityToDto(userGroupEntity.getCategoryEntity()));
    }
    public static Set<GroupDto> entitySetToDtoSet(Set<UserGroupEntity> userGroupEntities){
        Set<GroupDto> groupDtos = new HashSet<>();
        userGroupEntities.forEach((group)->{
            //System.out.println(group);
            groupDtos.add(entityToDto(group));
        });
        return groupDtos;
    }
    public static List<GroupDto> entityListToDtoList(List<UserGroupEntity> userGroupEntities){
        List<GroupDto> groupDtos = new ArrayList<>();
        userGroupEntities.forEach((group)->{
            groupDtos.add(entityToDto(group));
        });
        return groupDtos;
    }
    public static Page<GroupDto> entityPageToDtoPage(Page<UserGroupEntity> userEntities){
       Page<GroupDto> page = userEntities.map(
               new Function<UserGroupEntity, GroupDto>() {
                   @Override
                   public GroupDto apply(UserGroupEntity userGroupEntity) {
                       return entityToDto(userGroupEntity);
                   }
               }
       );

        return page;
    }

    public static Set<GroupDtoMini> entitySetToDtoSetMini(Set<UserGroupEntity> userGroupEntities){
        Set<GroupDtoMini> groupDtos = new HashSet<>();
        userGroupEntities.forEach((group)->{
            groupDtos.add(entityToDtoMini(group));
        });
        return groupDtos;
    }
    public static Page<GroupDtoMini> entityPageToDtoPageMini(Page<UserGroupEntity> categoryEntities){
        Set<UserGroupEntity> arr = Utils.listToSet(categoryEntities.getContent());
        Set<GroupDtoMini> newList = entitySetToDtoSetMini(arr);
        List<GroupDtoMini> list = Utils.setToList(newList);
        return new PageImpl<>(list,categoryEntities.getPageable(),list.size());
    }

}
