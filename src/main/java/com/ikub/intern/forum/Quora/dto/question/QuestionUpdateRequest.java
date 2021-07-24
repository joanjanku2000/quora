package com.ikub.intern.forum.Quora.dto.question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionUpdateRequest {
    @NotNull
    @NotBlank(message = "Question cannot be null")
    private String question;
}
