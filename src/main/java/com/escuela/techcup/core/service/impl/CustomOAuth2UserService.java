package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        log.info("Intento de autenticación OAuth2 con email: {}", email);

        return userService.getUserByMail(email)
                .map(user -> {
                    log.info("Usuario autenticado exitosamente: {}", email);
                    return oAuth2User;
                })
                .orElseThrow(() -> {
                    log.warn("Usuario no registrado intentó autenticarse: {}", email);

                    OAuth2Error oauth2Error = new OAuth2Error(
                            "user_not_registered",
                            createUserNotRegisteredMessage(email, name),
                            "/api/auth/register"
                    );

                    return new OAuth2AuthenticationException(oauth2Error);
                });
    }

    private String createUserNotRegisteredMessage(String email, String name) {
        return String.format(
                "Acceso denegado: El usuario con correo '%s' no está registrado en el sistema." +
                        "Por favor, contacta al administrador para solicitar acceso." +
                        "Si ya tienes una cuenta, asegúrate de usar el mismo correo registrado.",
                email
        );
    }
}