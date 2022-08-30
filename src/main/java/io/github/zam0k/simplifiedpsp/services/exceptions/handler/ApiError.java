package io.github.zam0k.simplifiedpsp.services.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiError {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String path;
}
