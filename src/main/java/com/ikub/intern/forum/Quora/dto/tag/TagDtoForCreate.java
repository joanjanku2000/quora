package com.ikub.intern.forum.Quora.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagDtoForCreate {
    @NotNull
    @NotBlank(message = "TagName cannot be empty")
    private String tagName;
}
