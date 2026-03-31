package com.escuela.techcup.core.handler;

import com.escuela.techcup.controller.handler.ErrorResponse;
import com.escuela.techcup.controller.handler.GlobalExceptionHandler;
import com.escuela.techcup.core.exception.TechcupException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }


    @Test
    void testHandleBusinessErrors_statusCode() {
        TechcupException ex = new TechcupException("Equipo no encontrado", HttpStatus.NOT_FOUND);
        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testHandleBusinessErrors_mensaje() {
        TechcupException ex = new TechcupException("Equipo no encontrado", HttpStatus.NOT_FOUND);
        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);
        assertEquals("Equipo no encontrado", response.getBody().getMessage());
    }

    @Test
    void testHandleBusinessErrors_statusEnBody() {
        TechcupException ex = new TechcupException("Equipo no encontrado", HttpStatus.NOT_FOUND);
        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
    }

    @Test
    void testHandleBusinessErrors_bodyNoEsNulo() {
        TechcupException ex = new TechcupException("Error", HttpStatus.BAD_REQUEST);
        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);
        assertNotNull(response.getBody());
    }

    @Test
    void testHandleBusinessErrors_timestampNoEsNulo() {
        TechcupException ex = new TechcupException("Error", HttpStatus.BAD_REQUEST);
        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void testHandleBusinessErrors_conStatusConflict() {
        TechcupException ex = new TechcupException("Ya existe", HttpStatus.CONFLICT);
        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Ya existe", response.getBody().getMessage());
    }

    @Test
    void testHandleBusinessErrors_conStatusForbidden() {
        TechcupException ex = new TechcupException("No autorizado", HttpStatus.FORBIDDEN);
        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testHandleBusinessErrors_conStatusInternalServerError() {
        TechcupException ex = new TechcupException("Fallo interno", HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<ErrorResponse> response = handler.handleBusinessErrors(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
    }


    @Test
    void testHandleGeneralErrors_statusCode() {
        Exception ex = new Exception("Error inesperado");
        ResponseEntity<ErrorResponse> response = handler.handleGeneralErrors(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testHandleGeneralErrors_message() {
        Exception ex = new Exception("Error inesperado");
        ResponseEntity<ErrorResponse> response = handler.handleGeneralErrors(ex);
        assertEquals("Internal server error", response.getBody().getMessage());
    }

    @Test
    void testHandleGeneralErrors_statusEnBody() {
        Exception ex = new Exception("cualquier error");
        ResponseEntity<ErrorResponse> response = handler.handleGeneralErrors(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
    }

    @Test
    void testHandleGeneralErrors_bodyNoEsNulo() {
        Exception ex = new Exception("error");
        ResponseEntity<ErrorResponse> response = handler.handleGeneralErrors(ex);
        assertNotNull(response.getBody());
    }

    @Test
    void testHandleGeneralErrors_timestampNoEsNulo() {
        Exception ex = new Exception("error");
        ResponseEntity<ErrorResponse> response = handler.handleGeneralErrors(ex);
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void testHandleGeneralErrors_conRuntimeException() {
        RuntimeException ex = new RuntimeException("runtime error");
        ResponseEntity<ErrorResponse> response = handler.handleGeneralErrors(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void testHandleValidationErrors_statusCode() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objeto", "nombre", "no debe estar vacío");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testHandleValidationErrors_mensajeContienecampo() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objeto", "nombre", "no debe estar vacío");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex);
        assertTrue(response.getBody().getMessage().contains("nombre"));
    }

    @Test
    void testHandleValidationErrors_mensajeContieneDescripcion() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objeto", "email", "formato inválido");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex);
        assertTrue(response.getBody().getMessage().contains("formato inválido"));
    }

    @Test
    void testHandleValidationErrors_statusEnBody() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objeto", "campo", "requerido");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
    }

    @Test
    void testHandleValidationErrors_sinErroresUsaFallback() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        ResponseEntity<ErrorResponse> response = handler.handleValidationErrors(ex);
        assertEquals("Validation error", response.getBody().getMessage());
    }
}
