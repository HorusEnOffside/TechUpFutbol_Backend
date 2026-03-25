package com.escuela.techcup.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "normal_payments")
public class NormalPaymentEntity extends PaymentEntity {
}