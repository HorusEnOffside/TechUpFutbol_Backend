package com.escuela.techcup.persistence.entity.payment;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "normal_payments")
public class NormalPaymentEntity extends PaymentEntity {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "payment_proof")
    private byte[] paymentProof;
}