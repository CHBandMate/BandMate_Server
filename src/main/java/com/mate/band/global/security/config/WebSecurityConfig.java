package com.mate.band.global.security.config;

import com.mate.band.global.security.filter.ExceptionHandlerFilter;
import com.mate.band.global.security.filter.JWTAuthorizationFilter;
import com.mate.band.global.security.handler.CustomAccessDeniedHandler;
import com.mate.band.global.security.handler.OAuth2FailureHandler;
import com.mate.band.global.security.handler.OAuth2SuccessHandler;
import com.mate.band.global.security.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    public static final String[] IGNORING_URI = {"/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/favicon.ico", "/default-ui.css"};
    public static final String[] PERMITTED_URI = {"/login", "/auth/token", "/auth/token/reissue", "/profile/metadata", "/profile/metadata/district", "/band/profile", "/user/profile"};
    private static final String[] PERMITTED_ROLES = {"USER", "ADMIN", "LEADER"};
    private static final String[] ALL_ROLES = {"USER", "ADMIN", "LEADER", "NOT_REGISTERED"};
    private final CustomCorsConfigurationSource customCorsConfigurationSource;
    private final CustomOAuth2UserService customOAuthService;
    private final OAuth2SuccessHandler successHandler;
    private final OAuth2FailureHandler failureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())   // 정적 자원 Spring Security 적용 X
                    .requestMatchers(IGNORING_URI);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(customCorsConfigurationSource))
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // CORS pre-flight 요청 허용
                        .requestMatchers(PERMITTED_URI).permitAll()

                        // 회원 프로필 등록 관련
                        .requestMatchers(HttpMethod.POST,"/user/profile").hasRole("NOT_REGISTERED")
                        .requestMatchers("/profile/check-nickname").hasAnyRole(ALL_ROLES)

                        .anyRequest().hasAnyRole(PERMITTED_ROLES))
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 사용으로 인한 세션 미사용
                .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(), JWTAuthorizationFilter.class)
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.accessDeniedHandler(customAccessDeniedHandler))
                // OAuth 로그인 설정
                .oauth2Login(customConfigurer -> customConfigurer
                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(customOAuthService)) // 인증 완료 후 처리
                        .successHandler(successHandler) // OAuth2 로그인 성공
                        .failureHandler(failureHandler) // OAuth2 로그인 실패
                );

        return http.build();
    }
}
