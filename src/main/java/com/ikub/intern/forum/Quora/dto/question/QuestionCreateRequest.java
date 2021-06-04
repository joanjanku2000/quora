package com.ikub.intern.forum.Quora.dto.question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateRequest {
    @NotNull
    @NotBlank(message = "Question cannot be null")
    private String question;
    private Long userId;
    private Long groupId;
    private List<Long> tags;
}
