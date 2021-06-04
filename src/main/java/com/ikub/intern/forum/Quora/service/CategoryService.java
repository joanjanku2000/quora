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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    public void saveCategory(CategoryCreateRequest categoryCreateRequest, UserEntity createdBy) {
        logger.info("Saving category {}",categoryCreateRequest);
        CategoryEntity categoryEntity ;
        if (categoryRepo.findByCategoryName(categoryCreateRequest.getName())!=null){
            throw new BadRequestException("Sorry, the category you are trying to add already exists");
        }
        categoryEntity = CategoryConverter.toEntity(categoryCreateRequest,createdBy);
         categoryRepo.save(categoryEntity);
        logger.info("Saved category {}",categoryEntity);
    }
    public void deleteCategory(Long id){
        logger.info("Deleting category {}",id);
        Optional<CategoryEntity> categoryEntity = categoryRepo.findById(id);
        if (!categoryEntity.isPresent()) throw new BadRequestException("The category you requested does not exist");
        categoryEntity.get().setActive(false);
        categoryRepo.save(categoryEntity.get());
        logger.info("Deleted category {}",categoryEntity);
    }
    public List<CategoryDto> findAll(){
        return CategoryConverter.entityListToDtoList(categoryRepo.findAll());
    }
    public CategoryDto findById(Long id){
        logger.info("Getting category {}",id);
        Optional<CategoryEntity> categoryEntity = categoryRepo.findById(id);
        if (!categoryEntity.isPresent())
            throw new BadRequestException("The category with the given id wasn;t found");
        logger.info("Got category");
        return CategoryConverter.entityToDto(categoryEntity.get());

    }
}
