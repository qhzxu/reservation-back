package com.reservation.reservation_server.config.Error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E1", "올바르지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E2", "잘못된 HTTP 메서드를 호출했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E3", "서버 에러가 발생했습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "A1", "존재하지 않은 상품입니다."),
    GENERIC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G1", "알 수 없는 오류가 발생했습니다."); // 기본 예외용

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
