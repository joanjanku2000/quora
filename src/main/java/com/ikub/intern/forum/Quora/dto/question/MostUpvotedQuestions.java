package com.ikub.intern.forum.Quora.dto.question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MostUpvotedQuestions {
    private String question;
    private BigInteger upvotes;
}
