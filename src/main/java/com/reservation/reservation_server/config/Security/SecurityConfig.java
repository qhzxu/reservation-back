package com.reservation.reservation_server.config.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 스프링 시큐리티 설정을 담당한다.
     * 보안정책, 필터체인, 예외처리 등을 구성함
     */
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          JwtUtil jwtUtil,
                          CustomAccessDeniedHandler accessDeniedHandler,
                          AuthenticationEntryPoint authenticationEntryPoint) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    private static final String[] AUTH_WHITELIST = {
            "/auth/user/login",
            "/auth/user/signup",
            "/auth/store/login",
            "/auth/store/signup",
            "/swagger-ui/**",
            "/api-docs",
            "/swagger-ui-custom.html",
            "/ws/**",
            "/product/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF, CORS 설정 비활성화 및 기본 설정 사용
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());

        // 폼 로그인과 HTTP 기본 인증 비활성화
        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 전에 추가
        http.addFilterBefore(
                new JwtAuthFilter(customUserDetailsService, jwtUtil),
                UsernamePasswordAuthenticationFilter.class);

        // 인증 예외 처리 핸들러 설정
        http.exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));

        // 세션 정책을 Stateless로 설정
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 인가 설정 - 인증이 필요한 경로 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(AUTH_WHITELIST).permitAll()    // 인증 제외 경로 허용
                .requestMatchers("/store/**").hasRole("STORE")  // /store/**는 STORE 권한만 허용
                .requestMatchers("/user/**").hasAnyRole("USER","STORE")
                .requestMatchers("/chat/**").hasAnyRole("USER","STORE")// /user/**는 USER 권한만 허용
                .anyRequest().authenticated()                   // 그 외 모든 요청은 인증 필요
        );


        return http.build();
    }
}
