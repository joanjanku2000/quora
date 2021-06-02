package com.ikub.intern.forum.Quora.dto.question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateRequest {
    private String question;
    private Long userId;
    private Long groupId;
    private List<Long> tags;
}
