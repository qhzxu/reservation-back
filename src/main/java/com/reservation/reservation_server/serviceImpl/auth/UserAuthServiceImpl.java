package com.reservation.reservation_server.serviceImpl.auth;

import com.reservation.reservation_server.common.RoleType;
import com.reservation.reservation_server.config.RedisService;
import com.reservation.reservation_server.config.Security.JwtUtil;
import com.reservation.reservation_server.config.TokenService;
import com.reservation.reservation_server.dto.CustomUserInfoDto;
import com.reservation.reservation_server.dto.LoginRequestDto;
import com.reservation.reservation_server.dto.UserSignupRequestDto;
import com.reservation.reservation_server.dto.auth.UserLoginResponseDto;
import com.reservation.reservation_server.entity.User;
import com.reservation.reservation_server.repository.UserRepository;
import com.reservation.reservation_server.service.auth.UserAuthService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserAuthServiceImpl implements UserAuthService {


    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserAuthServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper, JwtUtil jwtUtil, TokenService tokenService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }


    @Transactional
    @Override
    public User signup(UserSignupRequestDto request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        /**
         * 비밀번호 해시 처리
         * */

        User user = User.builder()
                        .email(request.getEmail())
                        .password(bCryptPasswordEncoder.encode(request.getPassword()))
                        .name(request.getName())
                        .phoneNumber(request.getPhoneNumber())
                        .role(RoleType.USER)
                        .isActive(Boolean.TRUE)
                        .build();

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public UserLoginResponseDto login(LoginRequestDto request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        CustomUserInfoDto info = modelMapper.map(user, CustomUserInfoDto.class);
        String token = jwtUtil.createAccessToken(info);
        String refreshToken = jwtUtil.createRefreshToken(info);

        //redis
        tokenService.storeRefreshToken(user.getUserId(), refreshToken);

        return UserLoginResponseDto.builder()
                .accessToken(token)
                .userId(user.getUserId())
                .userName(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .refreshToken(refreshToken)
                .build();
    }


}
