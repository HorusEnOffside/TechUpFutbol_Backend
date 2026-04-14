package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.controller.dto.PaymentRespondDTO;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.core.service.PaymentService;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.mapper.payment.PaymentMapper;
import com.escuela.techcup.persistence.repository.payment.PaymentRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Gestion de pagos", description = "Operaciones de pagos")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }



    @PreAuthorize("hasRole('ORGANIZER')")
    @GetMapping("/{id}/status")
    public ResponseEntity<PaymentRespondDTO> managePayment( @PathVariable String id,
                                                  @RequestParam PaymentStatus status){

        log.info("Petición para actualizar estado de pago - ID: {}, Estado solicitado: {}", id, status);


        Payment updatedModel = paymentService.updatePaymentState(id, status);
        PaymentRespondDTO response = PaymentMapper.toRespondDTO(updatedModel);

        log.info("Estado actualizado correctamente - ID: {}, Nuevo estado: {}", id, status);

        return ResponseEntity.ok(response);

    }


    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<PaymentRespondDTO>> getAllPayments() {
        log.info("Obtener todos los pagos");

        List<Payment> payments = paymentService.getPayments();

        List<PaymentRespondDTO> response = payments.stream()
                .map(PaymentMapper::toRespondDTO)
                .collect(Collectors.toList());

        log.info("Se encontraron {} pagos", response.size());

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PaymentRespondDTO> createPayment(
            @ModelAttribute @Valid PaymentDTO dto) {

        log.info("Petición para crear pago");

        Payment payment = paymentService.createPayment(dto, dto.getComprobante());
        PaymentRespondDTO response = PaymentMapper.toRespondDTO(payment);

        log.info("Pago creado correctamente - ID: {}", payment.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{id}/voucher")
    public ResponseEntity<byte[]> getVoucher(@PathVariable String id) {
        log.info("Petición para obtener voucher - ID: {}", id);

        Payment payment = paymentService.getPaymentById(id);
        PaymentEntity entity = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        PaymentMapper.VoucherMetadata metadata = PaymentMapper.getVoucherMetadata(entity);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metadata.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + metadata.getName() + "\"")
                .body(metadata.getBytes());
    }



}
