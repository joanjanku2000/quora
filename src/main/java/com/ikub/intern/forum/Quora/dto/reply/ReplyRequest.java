package com.ikub.intern.forum.Quora.dto.reply;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReplyRequest {
    @NotNull
    @NotBlank(message = "Reply cannot be empty")
    private String reply;
}
