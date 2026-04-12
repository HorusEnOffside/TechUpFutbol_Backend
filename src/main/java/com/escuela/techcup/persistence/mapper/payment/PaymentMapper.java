package com.escuela.techcup.persistence.mapper.payment;


import com.escuela.techcup.core.model.Payment;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PaymentMapper {
    private PaymentMapper() {}


    public static NormalPaymentEntity toEntity(NormalPayment model) {
        if (model == null) return null;
        NormalPaymentEntity entity = new NormalPaymentEntity();
        entity.setId(model.getId());
        entity.setStatus(model.getStatus());
        entity.setDescription(model.getDescription());
        entity.setPaymentDate(model.getPaymentDate());
        entity.setPaymentProof(toPngBytes(model.getVoucher()));
        return entity;
    }

    public static NormalPaymentEntity toEntity(Payment model) {
        if (model == null) return null;
        NormalPaymentEntity entity = new NormalPaymentEntity();
        entity.setId(model.getId());
        entity.setStatus(model.getStatus());
        entity.setDescription(model.getDescription());
        entity.setPaymentDate(model.getPaymentDate());
        entity.setPaymentProof(toPngBytes(model.getVoucher()));
        return entity;
    }

    public static NormalPayment toModel(NormalPaymentEntity entity) {
        if (entity == null) return null;
        NormalPayment model = new NormalPayment();
        model.setId(entity.getId());
        model.setStatus(entity.getStatus());
        model.setDescription(entity.getDescription());
        model.setPaymentDate(entity.getPaymentDate());
        model.setVoucher(toBufferedImage(entity.getPaymentProof()));
        return model;
    }

    public static NormalPayment toModel(PaymentEntity entity) {
        if (entity == null) return null;
        NormalPayment model = new NormalPayment();
        model.setId(entity.getId());
        model.setStatus(entity.getStatus());
        model.setDescription(entity.getDescription());
        model.setPaymentDate(entity.getPaymentDate());
        model.setVoucher(toBufferedImage(entity.getPaymentProof()));
        return model;
    }

    private static byte[] toPngBytes(BufferedImage image) {
        if (image == null) {
            return new byte[0];
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private static BufferedImage toBufferedImage(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            return null;
        }
    }
}
