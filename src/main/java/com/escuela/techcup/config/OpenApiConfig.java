package com.escuela.techcup.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI techUpFutbolOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("TechUp Futbol API")
                .description("Documentacion de la API de TechUp Futbol")
                .version("v1.0"))
            .addTagsItem(new Tag().name("Autenticacion").description("Inicio de sesion y emision de token"))
            .addTagsItem(new Tag().name("Gestion de usuarios").description("Administracion y consulta de usuarios"))
            .addTagsItem(new Tag().name("Gestion de jugadores").description("Creacion y consulta de perfiles deportivos"))
            .addTagsItem(new Tag().name("Gestion de equipos").description("Operaciones de equipos"))
            .addTagsItem(new Tag().name("Gestion de torneos").description("Operaciones de torneos"))
            .addTagsItem(new Tag().name("Gestion de pagos").description("Operaciones de pagos"))
            .addTagsItem(new Tag().name("Gestion de partidos").description("Operaciones de partidos"));
    }
}
