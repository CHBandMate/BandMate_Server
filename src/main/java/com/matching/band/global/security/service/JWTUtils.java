package com.matching.band.global.security.service;

import com.matching.band.domain.user.entity.UserEntity;
import com.matching.band.global.security.constants.Auth;
import com.matching.band.global.util.exception.BusinessException;
import com.matching.band.global.util.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
public class JWTUtils {
    private static String ACCESS_SECRET_KEY;
    private static String REFRESH_SECRET_KEY;
    private static String EXPIRED_TYPE;
    private static long ACCESS_EXPIRED_TIME;   // AccessToken 만료 시간
    private static long REFRESH_EXPIRED_TIME;  // RefreshToken 만료 시간

    @Value("${jwt.secret.access}")
    private void setAccessSecretKey(String value){
        ACCESS_SECRET_KEY = value;
    }

    @Value("${jwt.secret.refresh}")
    private void setRefreshSecretKey(String value){
        REFRESH_SECRET_KEY = value;
    }

    @Value("${jwt.expired.type}")
    private void setRefreshExpiredType(String type){
        EXPIRED_TYPE = type;
    }

    @Value("${jwt.expired.access}")
    private void setAccessExpiredTime(long value){
        ACCESS_EXPIRED_TIME = value;
    }

    @Value("${jwt.expired.refresh}")
    private void setRefreshExpiredTime(long value){
        REFRESH_EXPIRED_TIME = value;
    }

    /******************************************** JTW 생성 ********************************************/
    /**
     * 사용자 정보를 기반으로 인증 된 JWT Access, Refresh 토큰을 생성하여 반환 해주는 메서드
     * @param userEntity
     * @return String : 토큰
     */
    public static Map<String, String> generateAuthenticatedTokens(UserEntity userEntity) {
        Map<String, String> tokenMap = new HashMap<>();
        String accessToken = createToken(userEntity, Auth.ACCESS_TYPE.getKey(), ACCESS_EXPIRED_TIME);
        String refreshToken = createToken(userEntity, Auth.REFRESH_TYPE.getKey(), REFRESH_EXPIRED_TIME);
        tokenMap.put(Auth.ACCESS_TYPE.getKey(), accessToken);
        tokenMap.put(Auth.REFRESH_TYPE.getKey(), refreshToken);
        return tokenMap;
    }

    /**
     * 사용자 정보를 기반으로 JWT Access, Refresh 토큰을 생성하여 반환 해주는 메서드
     * @param userEntity, tokenType, expiredTime, ACCESS_SECRET_KEY
     * @return String : 토큰
     */
    private static String createToken(UserEntity userEntity, String tokenType, long expiredTime) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setHeader(createHeader())
                .setSubject(String.valueOf(userEntity.getUserNo()))
                .setExpiration(createExpiration(expiredTime))
                .claim("tokenType", tokenType)
                .claim("oauthId", userEntity.getOauthId())
                .claim("oauthType", userEntity.getOauthType())
                .claim("role", userEntity.getRole().getKey())
                .signWith(createSignature(tokenType.equals(Auth.ACCESS_TYPE.getKey()) ? ACCESS_SECRET_KEY : REFRESH_SECRET_KEY), SignatureAlgorithm.HS256);
        return jwtBuilder.compact();
    }

    /**
     * JWT Header 값 생성
     * @return HashMap<String, Object>
     */
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    /**
     * JWT Claim 생성
     * @param userEntity
     * @return Map<String, Object>
     */
    private static Map<String, Object> createClaims(UserEntity userEntity, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenType", tokenType);
        claims.put("oauthId", userEntity.getOauthId());
        claims.put("oauthType", userEntity.getOauthType());
        claims.put("role", userEntity.getRole().getKey());
        return claims;
    }

    /**
     * JWT Signature 생성
     * @return Key
     */
    private static Key createSignature(String key) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * 토큰의 만료기간을 지정
     * @return Calendar
     */
    private static Date createExpiration(long time) {
        Calendar c = Calendar.getInstance();
        switch (JWTUtils.EXPIRED_TYPE) {
            case "SECOND" -> c.add(Calendar.SECOND, (int) time);
            case "MINUTE" -> c.add(Calendar.MINUTE, (int) time);
            case "HOUR" -> c.add(Calendar.HOUR, (int) time);
            case "DATE" -> c.add(Calendar.DATE, (int) time);
        }
        return c.getTime();
    }


    /******************************************** 토큰 검증 ********************************************/

    /**
     * Header 내에 토큰을 추출 (사용 X)
     * @param header 헤더
     * @return String
     */
    public static String getTokenFromHeader(String header) {
        String token = "";
        try {
            token = header.split(" ")[1];
        } catch (NullPointerException e) {
            throw new BusinessException(ErrorCode.TOKEN_NUll.getErrorMessage());
        }
        return token;
    }

    /**
     * 유효한 토큰인지 확인
     * @param token
     * @return boolean
     */
    public static boolean isValidToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null;
    }

    /**
     * 토큰을 기반으로 Authentication 생성
     * @param token
     * @return Authentication
     */
//    public static Authentication getAuthentication(String token) {
//
//        Claims claims = getClaimsFromToken(token);
//        if (claims.get("auth") == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }
//
//        UserEntity user = ConvertUtil.convertValue(claims.get(AUTHORITIES_KEY), UserEntity.class);
//        MemberPrincipal memberPrincipal = new MemberPrincipal(user, Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())));
//
//        return new UsernamePasswordAuthenticationToken(memberPrincipal, "", memberPrincipal.getAuthorities());
//    }
//
//    public static UserEntity getMemberInfoFromToken(String token) {
//        Claims claims = getClaimsFromToken(token);
//        if (claims.get(AUTHORITIES_KEY) == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }
//
//        return ConvertUtil.convertValue(claims.get(AUTHORITIES_KEY), UserEntity.class);
//    }

    /**
     * 토큰 정보를 기반으로 Claims 정보를 반환
     * @param token
     * @return Claims
     */
    private static Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(ACCESS_SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * JWT Payload의 subject 정보 반환
     * @param token
     * @return String
     */
    public static String getSubjectFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(ACCESS_SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    private Key getSigningKey(String secretKey) {
        String encodedKey = encodeToBase64(secretKey);
        return Keys.hmacShaKeyFor(encodedKey.getBytes(StandardCharsets.UTF_8));
    }

    private String encodeToBase64(String secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


}
