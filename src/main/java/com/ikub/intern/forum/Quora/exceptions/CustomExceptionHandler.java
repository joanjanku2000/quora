package com.ikub.intern.forum.Quora.exceptions;

import com.ikub.intern.forum.Quora.dto.ErrorFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Component
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = {Exception.class,NotFoundException.class
            ,BadRequestException.class,NotAllowedException.class})
    protected ResponseEntity<Object> handleCustomExceptions(RuntimeException ex) {
        ErrorFormat errorBody = new ErrorFormat();
        errorBody.setMessage(ex.getMessage());
        errorBody.setException(ex.getClass().getSimpleName());
        errorBody.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }
}
