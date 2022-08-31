package io.github.zam0k.simplifiedpsp.services.exceptions.handler;

import io.github.zam0k.simplifiedpsp.services.exceptions.BadRequestException;
import io.github.zam0k.simplifiedpsp.services.exceptions.BadGatewayException;
import io.github.zam0k.simplifiedpsp.services.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> internalServerErrorException(Exception ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> notFoundException(NotFoundException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> badRequestException(BadRequestException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BadGatewayException.class)
    public ResponseEntity<ApiError> badGatewayException(BadGatewayException ex, HttpServletRequest request) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                BAD_GATEWAY.value(),
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(BAD_GATEWAY).body(error);
    }
}
