package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;

import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.mapper.payment.PaymentMapper;
import com.escuela.techcup.persistence.repository.payment.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private PaymentMapper paymentMapper;

    @InjectMocks private PaymentServiceImpl paymentService;

    private PaymentDTO mockDTO;

    @BeforeEach
    void setUp() {
        mockDTO = new PaymentDTO();
        mockDTO.setDescription("Pago inscripcion");
        mockDTO.setPaymentDate(LocalDateTime.of(2025, 3, 1, 10, 0));
    }

    // ── CreatePayment ────────────────────────────────────────────────────

    @Nested
    class CreatePayment {

        @Test
        void whenValidImageFile_thenCreatesAndSavesPayment() throws IOException {
            MultipartFile file = mock(MultipartFile.class);
            when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
            when(file.getContentType()).thenReturn("image/jpeg");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertNotNull(result);
            assertEquals(PaymentStatus.PENDING, result.getStatus());
            verify(paymentRepository).save(any(PaymentEntity.class));
        }

        @Test
        void whenValidPdfFile_thenCreatesAndSavesPayment() throws IOException {
            MultipartFile file = mock(MultipartFile.class);
            when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
            when(file.getContentType()).thenReturn("application/pdf");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertNotNull(result);
            assertEquals(PaymentStatus.PENDING, result.getStatus());
            verify(paymentRepository).save(any(PaymentEntity.class));
        }

        @Test
        void whenValidPngFile_thenCreatesAndSavesPayment() throws IOException {
            MultipartFile file = mock(MultipartFile.class);
            when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
            when(file.getContentType()).thenReturn("image/png");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertNotNull(result);
            verify(paymentRepository).save(any(PaymentEntity.class));
        }



        @Test
        void whenCreated_thenStatusIsPending() throws IOException {
            MultipartFile file = mock(MultipartFile.class);
            when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
            when(file.getContentType()).thenReturn("image/jpeg");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertEquals(PaymentStatus.PENDING, result.getStatus());
        }

        @Test
        void whenCreated_thenDescriptionMatchesDTO() throws IOException {
            MultipartFile file = mock(MultipartFile.class);
            when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
            when(file.getContentType()).thenReturn("image/jpeg");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertEquals("Pago inscripcion", result.getDescription());
        }

        @Test
        void whenCreated_thenPaymentDateMatchesDTO() throws IOException {
            MultipartFile file = mock(MultipartFile.class);
            when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
            when(file.getContentType()).thenReturn("image/jpeg");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertEquals(mockDTO.getPaymentDate(), result.getPaymentDate());
        }

        @Test
        void whenCreated_thenIdIsGenerated() throws IOException {
            MultipartFile file = mock(MultipartFile.class);
            when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
            when(file.getContentType()).thenReturn("image/jpeg");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertNotNull(result.getId());
            assertFalse(result.getId().isBlank());
        }
    }
}
