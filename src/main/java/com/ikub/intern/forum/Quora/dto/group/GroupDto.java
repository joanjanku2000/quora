package com.ikub.intern.forum.Quora.dto.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ikub.intern.forum.Quora.dto.category.CategoryDto;
import com.ikub.intern.forum.Quora.dto.question.QuestionDto;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.dto.user.UserDtoMini;
import com.ikub.intern.forum.Quora.entities.CategoryEntity;
import com.ikub.intern.forum.Quora.entities.QuestionEntity;
import com.ikub.intern.forum.Quora.entities.UserEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@ToString
public class GroupDto {
    private Long id;
    private String groupName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDtoMini admin;
    private CategoryDto categoryDto;
    private List<UserDto> users;
    private Set<QuestionDto> questions;

    public GroupDto(Long id, String groupName, String description, LocalDateTime createdAt, LocalDateTime updatedAt, UserDtoMini admin, CategoryDto categoryDto, Set<QuestionDto> questions) {
        this.id = id;
        this.groupName = groupName;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.admin = admin;
        this.categoryDto = categoryDto;
        this.questions = questions;
    }

    public boolean userIsPartOfTheGroup(String username){
        for(UserDto user : users){
            if (user.getUsername().compareTo(username)==0){
                return true;
            }
        }
        return false;
    }
    public boolean userIsAdmin(String username){
        return admin.getUsername().compareTo(username)==0;
    }
}
