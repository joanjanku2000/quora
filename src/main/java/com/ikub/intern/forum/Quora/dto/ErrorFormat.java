package com.ikub.intern.forum.Quora.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorFormat {
    private String message;
    private LocalDateTime timestamp;
    private String exception;
}