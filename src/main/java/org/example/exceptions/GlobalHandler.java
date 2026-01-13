package org.example.exceptions;

import org.example.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse>handleNotFound(ResourceNotFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse(false,ex.getMessage(),null),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse>handleBadRequest(BadRequestException ex){
        return new ResponseEntity<>(
                new ApiResponse(false,ex.getMessage(),null),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse>handleAllException(Exception ex){
        return new ResponseEntity<>(
                new ApiResponse(false,ex.getMessage(),null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
