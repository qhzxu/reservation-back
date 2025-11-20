package com.reservation.reservation_server.config.Error;

import com.reservation.reservation_server.config.Exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j // 자동 로그 생성
@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 잡아서 처리한다.
public class GlobalExceptionHandler {

    // HTTP Method 예외
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException", e);
        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED);
    }


    // 기본 예외
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }


    // ProductNotFound 예외
    @ExceptionHandler(ProductNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException e) {
        log.error("ProductNotFoundException", e);
        return createErrorResponseEntity(ErrorCode.PRODUCT_NOT_FOUND);
    }

    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(
                ErrorResponse.of(errorCode),
                errorCode.getHttpStatus()
        );
    }
}
