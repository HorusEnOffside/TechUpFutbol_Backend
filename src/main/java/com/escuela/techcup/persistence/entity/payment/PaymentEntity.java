package com.escuela.techcup.persistence.entity.payment;

import java.util.UUID;


import com.escuela.techcup.core.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class PaymentEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private PaymentStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "voucher", nullable = false, columnDefinition = "bytea")
    private byte[] voucher;

    private String voucherName;
    private String voucherType;
}
