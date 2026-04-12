package com.escuela.techcup.persistence.entity.payment;


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
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "formation", length = 50)
    private PaymentStatus status;


    @Column(name = "descripcion")
    private String description;

    @Column(name = "fecha_pago")
    private LocalDateTime paymentDate;

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] voucher;

    @Column(name = "voucher_type", length = 100)
    private String voucherType;

    @Column(name = "voucher_name", length = 255)
    private String voucherName;

    @Column(name = "voucher_size")
    private Long voucherSize;
}
