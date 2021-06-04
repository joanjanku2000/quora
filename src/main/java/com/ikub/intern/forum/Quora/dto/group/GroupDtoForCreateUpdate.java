package com.ikub.intern.forum.Quora.dto.group;

import com.ikub.intern.forum.Quora.entities.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDtoForCreateUpdate {
    @NotNull(message = "Name of the group cannot be null")
    @NotBlank(message = "Name of the group cannot be blank")
    private String groupName;
    @NotNull(message = "Description of the group cannot be null")
    @NotBlank(message = "Description of the group cannot be blank")
    private String description;
    @NotNull(message = "Category of the group cannot be null")
    @NotBlank(message = "Category of the group cannot be blank")
    private Long categoryId;
}
