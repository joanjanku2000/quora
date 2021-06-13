package com.ikub.intern.forum.Quora.dto.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
@Getter
@Setter
@AllArgsConstructor
public class GroupWithMostAskedQuestion {
    private String groupName;
    private BigInteger totalQuestions;
}
