package com.ikub.intern.forum.Quora.dto.group;

import com.ikub.intern.forum.Quora.entities.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDtoForCreateUpdate {
    private String groupName;
    private String description;
    private Long categoryId;
}
