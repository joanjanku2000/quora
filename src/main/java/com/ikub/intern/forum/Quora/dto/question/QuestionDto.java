package com.ikub.intern.forum.Quora.dto.question;


import com.ikub.intern.forum.Quora.dto.group.GroupDto;
import com.ikub.intern.forum.Quora.dto.reply.ReplyDto;
import com.ikub.intern.forum.Quora.dto.tag.TagDto;
import com.ikub.intern.forum.Quora.dto.user.UserDto;
import com.ikub.intern.forum.Quora.dto.user.UserDtoMini;
import com.ikub.intern.forum.Quora.entities.UpvotesQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
    private Long id;
    private String question;
    private String username;
    private LocalDateTime date;
    private LocalDateTime updatedAt;
    private Set<TagDto> tagList;
    private Set<UserDtoMini> upvotedBy;
    private Set<ReplyDto> replies;

    public boolean hasUpvoted(String username){
        for(UserDtoMini user : upvotedBy){
            if (user.getUsername().compareTo(username)==0) {
                return true;
            }
        }
        return false;
    }
}
