package com.ikub.intern.forum.Quora.converter;

import com.ikub.intern.forum.Quora.dto.category.CategoryCreateRequest;
import com.ikub.intern.forum.Quora.dto.category.CategoryDto;
import com.ikub.intern.forum.Quora.entities.CategoryEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryConverter {
    public static CategoryEntity toEntity(CategoryCreateRequest categoryCreateRequest, UserEntity createdBY){
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setActive(true);
        categoryEntity.setCategoryName(categoryCreateRequest.getName());
        categoryEntity.setCreatedAt(LocalDateTime.now());
        categoryEntity.setCreatedBy(createdBY);
        return categoryEntity;
    }
    public static CategoryDto entityToDto(CategoryEntity categoryEntity){
       return new CategoryDto(categoryEntity.getId(),categoryEntity.getCategoryName());
    }
    public static List<CategoryDto> entityListToDtoList(List<CategoryEntity> categoryEntities){
        List<CategoryDto> list = new ArrayList<>();
        categoryEntities.forEach((categoryEntity -> {
            list.add(entityToDto(categoryEntity));
        }));
        return list;
    }
    public static Page<CategoryDto> entityPageToDtoPage(Page<CategoryEntity> categoryEntities){
        List<CategoryDto> newList = entityListToDtoList(categoryEntities.getContent());
        return new PageImpl<>(newList,categoryEntities.getPageable(),newList.size());
    }
}
