package com.reservation.reservation_server.config.Exception;


import com.reservation.reservation_server.config.Error.ErrorCode;

// 커스텀 예외 정의
public class ProductNotFoundException extends BusinessBaseException {

    public ProductNotFoundException(ErrorCode errorCode) {
        super(errorCode,errorCode.getMessage());
    }
    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }

}
