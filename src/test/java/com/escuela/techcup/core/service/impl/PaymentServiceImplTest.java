package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.core.exception.FileStorageException;
import com.escuela.techcup.core.exception.InvalidFileException;
import com.escuela.techcup.core.exception.PaymentNotFoundException;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock private PaymentRepository paymentRepository;

    @InjectMocks private PaymentServiceImpl paymentService;

    private PaymentDTO mockDTO;

    @BeforeEach
    void setUp() {
        mockDTO = new PaymentDTO();
        mockDTO.setDescription("Pago inscripcion");
        mockDTO.setPaymentDate(LocalDateTime.of(2025, 3, 1, 10, 0));
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private MultipartFile mockFile(String contentType) throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn(contentType);
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(file.getOriginalFilename()).thenReturn("comprobante.jpg");
        when(file.getSize()).thenReturn(1024L);
        return file;
    }

    private PaymentEntity buildPaymentEntity(String id) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(id);
        entity.setDescription("Pago inscripcion");
        entity.setPaymentDate(LocalDateTime.of(2025, 3, 1, 10, 0));
        entity.setStatus(PaymentStatus.PENDING);
        return entity;
    }

    // ── CreatePayment ────────────────────────────────────────────────────

    @Nested
    class CreatePayment {

        @Test
        void whenValidJpegFile_thenCreatesAndSavesPayment() throws IOException {
            MultipartFile file = mockFile("image/jpeg");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertNotNull(result);
            assertEquals(PaymentStatus.PENDING, result.getStatus());
            verify(paymentRepository).save(any(PaymentEntity.class));
        }

        @Test
        void whenValidPngFile_thenCreatesAndSavesPayment() throws IOException {
            MultipartFile file = mockFile("image/png");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertNotNull(result);
            assertEquals(PaymentStatus.PENDING, result.getStatus());
            verify(paymentRepository).save(any(PaymentEntity.class));
        }

        @Test
        void whenValidPdfFile_thenCreatesAndSavesPayment() throws IOException {
            MultipartFile file = mockFile("application/pdf");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertNotNull(result);
            assertEquals(PaymentStatus.PENDING, result.getStatus());
            verify(paymentRepository).save(any(PaymentEntity.class));
        }

        @Test
        void whenFileIsNull_thenThrowsInvalidFileException() {
            assertThrows(InvalidFileException.class,
                    () -> paymentService.createPayment(mockDTO, null));
            verifyNoInteractions(paymentRepository);
        }

        @Test
        void whenFileIsEmpty_thenThrowsInvalidFileException() {
            MultipartFile file = mock(MultipartFile.class);
            when(file.isEmpty()).thenReturn(true);

            assertThrows(InvalidFileException.class,
                    () -> paymentService.createPayment(mockDTO, file));
            verifyNoInteractions(paymentRepository);
        }

        @Test
        void whenFileTypeIsInvalid_thenThrowsInvalidFileException() {
            MultipartFile file = mock(MultipartFile.class);
            when(file.isEmpty()).thenReturn(false);
            when(file.getContentType()).thenReturn("video/mp4");

            assertThrows(InvalidFileException.class,
                    () -> paymentService.createPayment(mockDTO, file));
            verifyNoInteractions(paymentRepository);
        }

        @Test
        void whenFileTypeIsNull_thenThrowsInvalidFileException() {
            MultipartFile file = mock(MultipartFile.class);
            when(file.isEmpty()).thenReturn(false);
            when(file.getContentType()).thenReturn(null);

            assertThrows(InvalidFileException.class,
                    () -> paymentService.createPayment(mockDTO, file));
            verifyNoInteractions(paymentRepository);
        }

        @Test
        void whenFileReadFails_thenThrowsFileStorageException() throws IOException {
            MultipartFile file = mock(MultipartFile.class);
            when(file.isEmpty()).thenReturn(false);
            when(file.getContentType()).thenReturn("image/jpeg");
            doThrow(new IOException("disk error")).when(file).getBytes();

            assertThrows(FileStorageException.class,
                    () -> paymentService.createPayment(mockDTO, file));
            verify(paymentRepository, never()).save(any());
        }

        @Test
        void whenPaymentDateIsNull_thenSetsCurrentDate() throws IOException {
            mockDTO.setPaymentDate(null);
            MultipartFile file = mockFile("image/jpeg");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertNotNull(result.getPaymentDate());
        }

        @Test
        void whenCreated_thenIdIsGenerated() throws IOException {
            MultipartFile file = mockFile("image/jpeg");
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.createPayment(mockDTO, file);

            assertNotNull(result.getId());
            assertFalse(result.getId().isBlank());
        }

        @Test
        void whenCreated_thenVoucherUrlContainsPaymentId() throws IOException {
            MultipartFile file = mockFile("image/jpeg");
            PaymentEntity savedEntity = buildPaymentEntity("pay-123");
            when(paymentRepository.save(any())).thenReturn(savedEntity);

            Payment result = paymentService.createPayment(mockDTO, file);

            assertTrue(result.getUrlComprobante().contains("pay-123"));
            assertTrue(result.getUrlComprobante().contains("/voucher"));
        }
    }

    // ── GetPayments ──────────────────────────────────────────────────────

    @Nested
    class GetPayments {

        @Test
        void whenPaymentsExist_thenReturnsAll() {
            when(paymentRepository.findAll()).thenReturn(List.of(
                    buildPaymentEntity("pay-1"),
                    buildPaymentEntity("pay-2")
            ));

            List<Payment> result = paymentService.getPayments();

            assertEquals(2, result.size());
            verify(paymentRepository).findAll();
        }

        @Test
        void whenNoPaymentsExist_thenReturnsEmptyList() {
            when(paymentRepository.findAll()).thenReturn(List.of());

            List<Payment> result = paymentService.getPayments();

            assertTrue(result.isEmpty());
        }

        @Test
        void whenPaymentsExist_thenEachHasVoucherUrl() {
            when(paymentRepository.findAll()).thenReturn(List.of(
                    buildPaymentEntity("pay-1")
            ));

            List<Payment> result = paymentService.getPayments();

            assertNotNull(result.get(0).getUrlComprobante());
            assertTrue(result.get(0).getUrlComprobante().contains("pay-1"));
        }
    }

    // ── GetPaymentById ───────────────────────────────────────────────────

    @Nested
    class GetPaymentById {

        @Test
        void whenIdExists_thenReturnsPayment() {
            when(paymentRepository.findById("pay-1"))
                    .thenReturn(Optional.of(buildPaymentEntity("pay-1")));

            Payment result = paymentService.getPaymentById("pay-1");

            assertNotNull(result);
            assertEquals("pay-1", result.getId());
        }

        @Test
        void whenIdNotFound_thenThrowsPaymentNotFoundException() {
            when(paymentRepository.findById("unknown")).thenReturn(Optional.empty());

            assertThrows(PaymentNotFoundException.class,
                    () -> paymentService.getPaymentById("unknown"));
        }

        @Test
        void whenIdExists_thenVoucherUrlIsCorrect() {
            when(paymentRepository.findById("pay-1"))
                    .thenReturn(Optional.of(buildPaymentEntity("pay-1")));

            Payment result = paymentService.getPaymentById("pay-1");

            assertTrue(result.getUrlComprobante().contains("pay-1"));
            assertTrue(result.getUrlComprobante().contains("/voucher"));
        }
    }

    // ── UpdatePaymentState ───────────────────────────────────────────────

    @Nested
    class UpdatePaymentState {

        @Test
        void whenIdExists_thenUpdatesStatus() {
            PaymentEntity entity = buildPaymentEntity("pay-1");
            when(paymentRepository.findById("pay-1")).thenReturn(Optional.of(entity));
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.updatePaymentState("pay-1", PaymentStatus.APPROVED);

            assertEquals(PaymentStatus.APPROVED, result.getStatus());
            verify(paymentRepository).save(entity);
        }

        @Test
        void whenIdNotFound_thenThrowsPaymentNotFoundException() {
            when(paymentRepository.findById("unknown")).thenReturn(Optional.empty());

            assertThrows(PaymentNotFoundException.class,
                    () -> paymentService.updatePaymentState("unknown", PaymentStatus.APPROVED));
            verify(paymentRepository, never()).save(any());
        }

        @Test
        void whenStatusIsRejected_thenUpdatesCorrectly() {
            PaymentEntity entity = buildPaymentEntity("pay-1");
            when(paymentRepository.findById("pay-1")).thenReturn(Optional.of(entity));
            when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Payment result = paymentService.updatePaymentState("pay-1", PaymentStatus.REJECTED);

            assertEquals(PaymentStatus.REJECTED, result.getStatus());
        }
    }

    // ── DeletePayment ────────────────────────────────────────────────────

    @Nested
    class DeletePayment {

        @Test
        void whenIdExists_thenDeletesPayment() {
            PaymentEntity entity = buildPaymentEntity("pay-1");
            when(paymentRepository.findById("pay-1")).thenReturn(Optional.of(entity));

            paymentService.deletePayment("pay-1");

            verify(paymentRepository).delete(entity);
        }

        @Test
        void whenIdNotFound_thenThrowsPaymentNotFoundException() {
            when(paymentRepository.findById("unknown")).thenReturn(Optional.empty());

            assertThrows(PaymentNotFoundException.class,
                    () -> paymentService.deletePayment("unknown"));
            verify(paymentRepository, never()).delete(any());
        }
    }
}
