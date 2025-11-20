package com.reservation.reservation_server.config.Error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String code;
    private String message;

    // 생성자 추가
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // 정적 팩토리 메서드 추가
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getCode(),      // ErrorCode의 code
                errorCode.getMessage()    // ErrorCode의 message
        );
    }
}

