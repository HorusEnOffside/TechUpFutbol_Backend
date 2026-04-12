package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.PaymentRespondDTO;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.core.service.PaymentService;
import com.escuela.techcup.persistence.mapper.payment.PaymentMapper;
import lombok.extern.slf4j.Slf4j;
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

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
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

}
