package com.escuela.techcup.persistence.entity.payment;

import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PaymentStatus status;

    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "payment_proof")
    private byte[] paymentProof;


    @Column(name = "description", nullable = false, length = 255)
    private String description;


    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;
}