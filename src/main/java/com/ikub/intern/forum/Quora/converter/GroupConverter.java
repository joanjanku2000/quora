package com.ikub.intern.forum.Quora.converter;

import com.ikub.intern.forum.Quora.dto.group.GroupDto;
import com.ikub.intern.forum.Quora.dto.group.GroupDtoForCreateUpdate;
import com.ikub.intern.forum.Quora.entities.CategoryEntity;
import com.ikub.intern.forum.Quora.entities.UserGroupEntity;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GroupConverter {

    public static UserGroupEntity toEntity(GroupDtoForCreateUpdate groupDtoForCreate, CategoryEntity categoryEntity) {
        UserGroupEntity userGroupEntity = new UserGroupEntity();
        userGroupEntity.setActive(true);
        userGroupEntity.setCategoryEntity(categoryEntity);
        userGroupEntity.setCreatedAt(LocalDateTime.now());
        userGroupEntity.setGroupName(groupDtoForCreate.getGroupName());
        userGroupEntity.setDescription(groupDtoForCreate.getDescription());
        return userGroupEntity;
    }

    public static GroupDto entityToDto(UserGroupEntity userGroupEntity) {
        return new GroupDto(userGroupEntity.getId(), userGroupEntity.getGroupName(),
                userGroupEntity.getDescription(), userGroupEntity.getCreatedAt(),
                userGroupEntity.getUpdatedAt(), UserConverter.entitySetToDtoMiniSet(userGroupEntity.getAdmin())
                , CategoryConverter.entityToDto(userGroupEntity.getCategoryEntity())
                , QuestionConverter.entitySetToDtoSet(userGroupEntity.getQuestions()));
    }

    public static List<GroupDto> entityListToDtoList(List<UserGroupEntity> userGroupEntities) {
        List<GroupDto> groupDtos = new ArrayList<>();
        userGroupEntities.forEach((group) -> {
            groupDtos.add(entityToDto(group));
        });
        return groupDtos;
    }

    public static Page<GroupDto> entityPageToDtoPage(Page<UserGroupEntity> userEntities) {
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


}
