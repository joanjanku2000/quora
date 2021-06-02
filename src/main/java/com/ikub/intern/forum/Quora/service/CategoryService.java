package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.CategoryConverter;
import com.ikub.intern.forum.Quora.dto.category.CategoryCreateRequest;
import com.ikub.intern.forum.Quora.dto.category.CategoryDto;
import com.ikub.intern.forum.Quora.entities.CategoryEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.repository.CategoryRepo;
import com.ikub.intern.forum.Quora.utils.PageParams;
import com.ikub.intern.forum.Quora.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    public void saveCategory(CategoryCreateRequest categoryCreateRequest, UserEntity createdBy) {
        CategoryEntity categoryEntity ;
        if (categoryRepo.findByCategoryName(categoryCreateRequest.getName())!=null){
            throw new BadRequestException("Sorry, the category you are trying to add already exists");
        }
        categoryEntity = CategoryConverter.toEntity(categoryCreateRequest,createdBy);
        categoryRepo.save(categoryEntity);
    }
    public void deleteCategory(Long id){
        CategoryEntity categoryEntity = categoryRepo.findById(id).orElse(null);
        if (categoryEntity==null) throw new BadRequestException("The category you requested does not exist");
        categoryEntity.setActive(false);
        categoryRepo.save(categoryEntity);
    }
    public List<CategoryDto> findAll(){
        return CategoryConverter.entityListToDtoList(categoryRepo.findAll());
    }
    public CategoryEntity findById(Long id){
        CategoryEntity categoryEntity = categoryRepo.findById(id).orElse(null);
        if (categoryEntity==null) throw new BadRequestException("The category with the given id wasn;t found");
        return categoryEntity;
    }
}
