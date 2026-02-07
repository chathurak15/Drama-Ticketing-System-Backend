package com.example.NatakaLK.service.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.util.Map;

@Service
public class PaymentVerificationService {
    @Value("${payhere.merchant.id}")
    private String merchantId;

    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

    public boolean verifyPayment(Map<String, String> params) {
        String orderId = params.get("order_id");
        String statusCode = params.get("status_code");
        String md5sig = params.get("md5sig");
        String payHereAmount = params.get("payhere_amount");
        String payHereCurrency = params.get("payhere_currency");

        if (!"2".equals(statusCode)) {
            return false;
        }

        String generatedSig = generateMD5(merchantId + orderId + payHereAmount + payHereCurrency + statusCode + generateMD5(merchantSecret).toUpperCase()).toUpperCase();

        return generatedSig.equals(md5sig);
    }

    private String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating MD5", e);
        }
    }
}
