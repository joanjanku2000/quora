package com.ikub.intern.forum.Quora.dto.category;

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
public class CategoryCreateRequest {
   @NotNull(message = "Field cannot be null")
   @NotBlank(message = "Field cannot be blank")
   private String name;
}
