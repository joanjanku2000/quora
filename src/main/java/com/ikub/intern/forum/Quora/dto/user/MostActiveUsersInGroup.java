package com.ikub.intern.forum.Quora.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
@Getter
@Setter
@AllArgsConstructor
public class MostActiveUsersInGroup {
    private String username;
    private BigInteger totalQuestions;
}
