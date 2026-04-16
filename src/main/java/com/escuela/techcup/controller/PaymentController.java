package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.controller.dto.PaymentRespondDTO;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.core.service.PaymentService;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.mapper.payment.PaymentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Endpoints para gestión de pagos")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasAnyRole('CAPTAIN', 'ORGANIZER', 'ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear un nuevo pago", description = "Crea un pago con comprobante (imagen)")
    public ResponseEntity<PaymentRespondDTO> createPayment(
            @Valid
            @Parameter(description = "Datos del pago", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PaymentDTO.class)))
            @RequestPart("payment") PaymentDTO paymentDTO,

            @Parameter(description = "Archivo del comprobante", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, schema = @Schema(type = "string", format = "binary")))
            @RequestPart("file") MultipartFile file) {

        Payment created = paymentService.createPayment(paymentDTO, file);
        PaymentRespondDTO response = PaymentMapper.toRespondDTO(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole( 'ORGANIZER', 'ADMIN')")
    @GetMapping
    @Operation(summary = "Listar todos los pagos")
    public ResponseEntity<List<PaymentRespondDTO>> getAllPayments() {
        List<Payment> payments = paymentService.getPayments();

        List<PaymentRespondDTO> response = payments.stream()
                .map(PaymentMapper::toRespondDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole( 'ORGANIZER', 'ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID")
    public ResponseEntity<PaymentRespondDTO> getPaymentById(
            @PathVariable @Parameter(description = "ID del pago") String id) {

        Payment payment = paymentService.getPaymentById(id);
        PaymentRespondDTO response = PaymentMapper.toRespondDTO(payment);

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyRole('CAPTAIN', 'ORGANIZER', 'ADMIN')")
    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado del pago")
    public ResponseEntity<PaymentRespondDTO> updatePaymentStatus(
            @PathVariable String id,
            @RequestParam PaymentStatus status) {

        Payment updated = paymentService.updatePaymentState(id, status);
        PaymentRespondDTO response = PaymentMapper  .toRespondDTO(updated);

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyRole( 'ORGANIZER', 'ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pago")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletePayment(@PathVariable String id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyRole( 'ORGANIZER', 'ADMIN')")
    @GetMapping("/{id}/voucher")
    @Operation(summary = "Obtener comprobante de pago")
    public ResponseEntity<byte[]> getVoucher(@PathVariable String id) {
        PaymentEntity entity = paymentService.getVoucherById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(entity.getVoucherType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + entity.getVoucherName() + "\"")
                .body(entity.getVoucher());
    }


    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @GetMapping("/filter-by-status")
    @Operation(summary = "Filtrar pagos por estado", description = "Obtiene todos los pagos filtrados por su estado")
    public ResponseEntity<List<PaymentRespondDTO>> getPaymentsByStatus(
            @RequestParam PaymentStatus status) {

        List<Payment> payments = paymentService.getPaymentsByStatus(status);

        List<PaymentRespondDTO> response = payments.stream()
                .map(PaymentMapper::toRespondDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
