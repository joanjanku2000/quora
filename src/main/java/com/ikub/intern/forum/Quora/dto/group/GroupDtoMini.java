package com.ikub.intern.forum.Quora.dto.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ikub.intern.forum.Quora.dto.category.CategoryDto;
import com.ikub.intern.forum.Quora.dto.user.UserDtoMini;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDtoMini {
    private Long id;
    private String groupName;
    private String description;
    private UserDtoMini admin;
    private CategoryDto categoryDto;
}
