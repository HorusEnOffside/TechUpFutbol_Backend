package com.escuela.techcup.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpsEnforcementFilter extends OncePerRequestFilter {

    private final boolean sslEnabled;
    private final int httpsPort;

    public HttpsEnforcementFilter(
        @Value("${server.ssl.enabled:true}") boolean sslEnabled,
        @Value("${server.port:8443}") int httpsPort
    ) {
        this.sslEnabled = sslEnabled;
        this.httpsPort = httpsPort;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        if (!sslEnabled || request.isSecure() || isAlreadyHttps(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestUri = request.getRequestURI();
        String query = request.getQueryString();
        String redirectUrl = "https://" + request.getServerName() + ":" + httpsPort + requestUri
            + (query != null ? "?" + query : "");

        response.sendRedirect(redirectUrl);
    }

    private boolean isAlreadyHttps(HttpServletRequest request) {
        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        return "https".equalsIgnoreCase(forwardedProto);
    }
}
