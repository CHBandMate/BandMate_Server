package com.matching.band.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matching.band.global.util.exception.ErrorCode;
import com.matching.band.global.util.response.ApiResponse;
import com.matching.band.global.util.response.ErrorData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        log.error(accessDeniedException.getMessage());

        ApiResponse<ErrorData> apiResult = ApiResponse.fail(ErrorCode.UNAUTHORIZED.getStatusCode(), new ErrorData(ErrorCode.UNAUTHORIZED.getErrorCode(), ErrorCode.UNAUTHORIZED.getErrorMessage()));
        JSONObject jsonObject = new JSONObject(objectMapper.convertValue(apiResult, Map.class));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(403);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();

    }
}
