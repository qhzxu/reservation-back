
package com.reservation.reservation_server.dto.auth;


import com.reservation.reservation_server.common.RoleType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String userName;
    private String email;
    private RoleType role;

}
