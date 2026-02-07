package com.example.NatakaLK.service.payment;

import com.example.NatakaLK.dto.responseDTO.PaymentInitResponseDTO;
import com.example.NatakaLK.model.Booking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.text.DecimalFormat;

@Service
public class PayHereService {

    @Value("${payhere.merchant.id}")
    private String merchantId;

    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

    public PaymentInitResponseDTO generatePaymentData(Booking booking) {
        String currency = "LKR";
        double amount = booking.getTotalAmount();

        String hash = generatePayHereHash(merchantId, String.valueOf(booking.getId()), amount, currency, merchantSecret);

        return new PaymentInitResponseDTO(
                booking.getId(),
                booking.getTicketId(),
                merchantId,
                hash,
                amount,
                currency
        );
    }

    // PayHere Hash Generation Logic (MD5)
    private String generatePayHereHash(String merchantId, String orderId, double amount, String currency, String merchantSecret) {
        DecimalFormat df = new DecimalFormat("0.00");
        String amountFormatted = df.format(amount);

        String hashString = merchantId + orderId + amountFormatted + currency + generateMD5(merchantSecret).toUpperCase();
        return generateMD5(hashString).toUpperCase();
    }

    // MD5 Helper Method
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
