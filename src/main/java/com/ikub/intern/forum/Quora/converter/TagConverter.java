package com.ikub.intern.forum.Quora.converter;

import com.ikub.intern.forum.Quora.dto.tag.TagDto;
import com.ikub.intern.forum.Quora.dto.tag.TagDtoForCreate;
import com.ikub.intern.forum.Quora.entities.TagEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagConverter {

    public static TagEntity toEntity(TagDtoForCreate tagDtoForCreate, UserEntity userEntity) {
        return new TagEntity(tagDtoForCreate.getTagName(), userEntity, LocalDateTime.now(), true);
    }

    public static TagDto entityToDto(TagEntity tagEntity) {
        return new TagDto(tagEntity.getId(), tagEntity.getTagName());
    }

    public static Set<TagDto> entitySetToDtoSet(Set<TagEntity> tagEntitySet) {
        Set<TagDto> tagDtos = new HashSet<>();
        tagEntitySet.forEach((tagEntity -> {
            tagDtos.add(entityToDto(tagEntity));
        }));
        return tagDtos;
    }

    public static List<TagDto> entityListToDtoList(List<TagEntity> tagEntitySet) {
        List<TagDto> tagDtos = new ArrayList<>();
        tagEntitySet.forEach((tagEntity -> {
            tagDtos.add(entityToDto(tagEntity));
        }));
        return tagDtos;
    }

}
