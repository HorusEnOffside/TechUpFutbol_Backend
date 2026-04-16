package com.escuela.techcup.core.service;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.enums.PaymentStatus;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public interface PaymentService {
    Payment createPayment(PaymentDTO paymentDTO, MultipartFile file);
    List<Payment> getPayments();
    Payment getPaymentById(String id);
    Payment updatePaymentState(String id, PaymentStatus paymentStatus);
    void deletePayment(String id);
    PaymentEntity getVoucherById(String id);
}
