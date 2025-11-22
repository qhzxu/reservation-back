package com.reservation.reservation_server.controller.auth;

import com.reservation.reservation_server.dto.LoginRequestDto;
import com.reservation.reservation_server.dto.UserSignupRequestDto;
import com.reservation.reservation_server.dto.auth.UserLoginResponseDto;
import com.reservation.reservation_server.entity.User;
import com.reservation.reservation_server.service.auth.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/user")
public class UserAuthController {

    private final UserAuthService userAuthService;

    @Autowired
    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    /**
     * 사용자 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequestDto request) {
        try{
            User user = userAuthService.signup(request);
            return ResponseEntity.ok("회원가입 성공 : " + user.getEmail());
        }catch(IllegalArgumentException e){
            System.out.println("회원가입 실패");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("회원가입 실패");
        }
    }

    /**
     * 사용자 로그인 (JWT 토큰 발급)
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto request) {
        // 로그인 시 토큰 발급 및 사용자 정보 조회
        UserLoginResponseDto loginResponse = userAuthService.login(request);

        Map<String, Object> body = new HashMap<>();
        body.put("accessToken", loginResponse.getAccessToken());
        body.put("userName", loginResponse.getUserName());
        body.put("email", loginResponse.getEmail());
        body.put("userId", loginResponse.getUserId());
        body.put("role", loginResponse.getRole());
        body.put("refreshToken", loginResponse.getRefreshToken());

        return ResponseEntity.ok(body);
    }

    /**
     * 사용자 로그아웃
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/logout")
    public String logout(@RequestParam String username, @RequestParam String password) {
        return "test";
    }

}
