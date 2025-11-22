package com.reservation.reservation_server.dto;

import com.reservation.reservation_server.common.RoleType;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long storeId;
    private String storeName;
    private String email;
    private RoleType role;
}