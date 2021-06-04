package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.CategoryConverter;
import com.ikub.intern.forum.Quora.dto.category.CategoryCreateRequest;
import com.ikub.intern.forum.Quora.dto.category.CategoryDto;
import com.ikub.intern.forum.Quora.entities.CategoryEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.repository.CategoryRepo;
import com.ikub.intern.forum.Quora.repository.users.UserRepo;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryTests {
    @Mock
    CategoryRepo categoryRepo;

    @InjectMocks
    CategoryService categoryService;
    static CategoryEntity categoryEntity;
    static UserEntity userEntity;
    static CategoryCreateRequest categoryCreateRequest ;
    @BeforeAll
    static void initialize(){
        userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        categoryCreateRequest = new CategoryCreateRequest("name");
        categoryEntity = CategoryConverter.toEntity(categoryCreateRequest,userEntity);
    }


    @Test
    @DisplayName("Saving category ")
    void saveCategory(){
        when(categoryRepo.findByCategoryName(categoryCreateRequest.getName()))
                .thenReturn(null);
        categoryService.saveCategory(categoryCreateRequest,userEntity);
        verify(categoryRepo,times(1)).save(any(CategoryEntity.class));
    }
    @Test
    @DisplayName("Saving category fail")
    void saveCategoryFail(){

        when(categoryRepo.findByCategoryName(categoryCreateRequest.getName()))
                .thenReturn(categoryEntity);
       assertThrows(BadRequestException.class,() ->{
           categoryService.saveCategory(categoryCreateRequest,userEntity);
       });
    }
    @Test
    @DisplayName("Get by id success")
    void findById(){
        Long id = 1L;
        Optional<CategoryEntity> categoryEntityOptional = Optional.of(categoryEntity);
        when(categoryRepo.findById(id)).thenReturn(categoryEntityOptional);

        assertDoesNotThrow(() -> {
            categoryService.findById(id);
        });

    }
    @Test
    @DisplayName("Get by id failure")
    void findByIdFailure(){
        Long notFoundID = 2L;
        assertThrows(BadRequestException.class,() -> {
            categoryService.findById(notFoundID);
        });
    }

    @Test
    @DisplayName("Find all test")
    void findAll(){
        List<CategoryEntity> categoryEntityList = Arrays.asList(
                new CategoryEntity(1L,"name",userEntity,LocalDateTime.of(2021,2,3,4,5),true),
                new CategoryEntity(2L,"name2",userEntity,LocalDateTime.of(2021,2,3,4,5),true)
        );

        when(categoryRepo.findAll()).thenReturn(categoryEntityList);
        assertEquals(categoryService.findAll().size(),CategoryConverter.entityListToDtoList(categoryEntityList).size());
    }

    @Test
    @DisplayName("Delete Success ")
    void delete(){
        Long id = 1L;
        Optional<CategoryEntity> categoryEntityOptional = Optional.of(categoryEntity);
        when(categoryRepo.findById(id)).thenReturn(categoryEntityOptional);
        assertDoesNotThrow(() -> {
            categoryService.deleteCategory(id);
        });
    }
}
