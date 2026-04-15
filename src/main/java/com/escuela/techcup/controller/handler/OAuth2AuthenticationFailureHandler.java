package com.escuela.techcup.controller.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        log.error("Error en autenticación OAuth2: {}", exception.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String errorMessage;
        String errorCode = "authentication_failed";

        if (exception instanceof OAuth2AuthenticationException oauth2Ex) {
            OAuth2Error oauth2Error = oauth2Ex.getError();
            errorCode = oauth2Error.getErrorCode();

            if ("user_not_registered".equals(errorCode)) {
                errorMessage = oauth2Error.getDescription();
            } else {
                errorMessage = "Error en la autenticación con Google. Por favor, intenta nuevamente.";
            }
        } else {
            errorMessage = "Error inesperado durante la autenticación.";
        }

        Map<String, Object> errorResponse = Map.of(
                "success", false,
                "error", errorCode,
                "message", errorMessage,
                "status", HttpServletResponse.SC_UNAUTHORIZED,
                "timestamp", System.currentTimeMillis()
        );

        response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(errorResponse));
    }
}