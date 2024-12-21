package com.matching.band.global.security.config;

import com.matching.band.global.security.service.CustomOAuth2UserService;
import com.matching.band.global.security.handler.OAuth2FailureHandler;
import com.matching.band.global.security.handler.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private static final String[] PERMITTED_ROLES = {"USER", "ADMIN"};
    private final CustomCorsConfigurationSource customCorsConfigurationSource;
    private final CustomOAuth2UserService customOAuthService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(customCorsConfigurationSource))
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // CORS pre-flight 요청 허용
                        .anyRequest().hasAnyRole(PERMITTED_ROLES))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 사용으로 인한 세션 미사용
                // OAuth 로그인 설정
                .oauth2Login(customConfigurer -> customConfigurer
                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(customOAuthService)) // 인증 완료 후 처리
                        .successHandler(successHandler) // OAuth2 로그인 성공
                        .failureHandler(failureHandler) // OAuth2 로그인 실패
                );

        return http.build();
    }
}
