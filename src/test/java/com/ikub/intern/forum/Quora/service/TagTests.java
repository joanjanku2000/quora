package com.ikub.intern.forum.Quora.service;

import com.ikub.intern.forum.Quora.converter.TagConverter;
import com.ikub.intern.forum.Quora.dto.tag.TagDtoForCreate;
import com.ikub.intern.forum.Quora.entities.TagEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import com.ikub.intern.forum.Quora.exceptions.BadRequestException;
import com.ikub.intern.forum.Quora.exceptions.NotAllowedException;
import com.ikub.intern.forum.Quora.repository.TagRepo;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagTests {
    @Mock
    TagRepo tagRepo;

    @InjectMocks
    TagService tagService;

    static TagEntity tagEntity;
    static UserEntity userEntity;
    static TagDtoForCreate tagDtoForCreate;

    @BeforeAll
    static void initialize(){
        userEntity = new UserEntity("test","test","test",
                "m","usernameTest",
                LocalDate.ofYearDay(2000,23),
                LocalDateTime.of(2021, 3, 1,12,1),
                "user",true);
        tagDtoForCreate = new TagDtoForCreate("tag");
        tagEntity = TagConverter.toEntity(tagDtoForCreate,userEntity);
    }

    @Test
    @DisplayName("Saving tag ")
    void saveCategory(){
        when(tagRepo.findByTagName(tagDtoForCreate.getTagName()))
                .thenReturn(Optional.ofNullable(null));
        tagService.saveTag(tagDtoForCreate,userEntity);
        verify(tagRepo,times(1)).save(any(TagEntity.class));
    }
    @Test
    @DisplayName("Saving tag fail")
    void saveTagFail(){
        when(tagRepo.findByTagName(tagDtoForCreate.getTagName()))
                .thenReturn(Optional.ofNullable(tagEntity));
        assertThrows(NotAllowedException.class,() ->{
            tagService.saveTag(tagDtoForCreate,userEntity);
        });
    }
    @Test
    @DisplayName("Get by id success")
    void findById(){
        Long id = 1L;
        Optional<TagEntity> tagEntityOptional = Optional.of(tagEntity);
        when(tagRepo.findById(id)).thenReturn(tagEntityOptional);

        assertDoesNotThrow(() -> {
            tagService.findById(id);
        });

    }
    @Test
    @DisplayName("Get by id failure")
    void findByIdFailure(){
        Long notFoundID = 2L;
        assertThrows(BadRequestException.class,() -> {
            tagService.findById(notFoundID);
        });
    }

    @Test
    @DisplayName("Find all test")
    void findAll(){
        List<TagEntity> tagEntityList = Arrays.asList(
                new TagEntity(1L,"name",userEntity,LocalDateTime.of(2021,2,3,4,5),true),
                new TagEntity(2L,"name2",userEntity,LocalDateTime.of(2021,2,3,4,5),true)
        );

        when(tagRepo.findAll()).thenReturn(tagEntityList);
        assertEquals(tagService.findALl().size(), TagConverter.entityListToDtoList(tagEntityList).size());
    }

    @Test
    @DisplayName("Delete Success ")
    void delete(){
        Long id = 1L;
        Optional<TagEntity> tagEntityOptional = Optional.of(tagEntity);
        when(tagRepo.findById(id)).thenReturn(tagEntityOptional);
        assertDoesNotThrow(() -> {
            tagService.deleteTag(id);
        });
    }
}
