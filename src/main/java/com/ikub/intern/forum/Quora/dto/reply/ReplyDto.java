package com.ikub.intern.forum.Quora.dto.reply;

import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.dto.user.UserDtoMini;
import com.ikub.intern.forum.Quora.entities.QuestionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
@AllArgsConstructor
@Getter
public class ReplyDto {
    private Long id;
    private String reply;
    private UserDtoMini user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<UserDtoMini> upvotedBy;
}
