package com.escuela.techcup.persistence.entity.payment;


import jakarta.persistence.Entity;

import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "normal_payments")
public class NormalPaymentEntity extends PaymentEntity {

}