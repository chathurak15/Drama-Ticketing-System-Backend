package com.example.NatakaLK.service;

import com.example.NatakaLK.dto.responseDTO.BookingResponseDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QRCodeService {

    private static final Logger logger = LoggerFactory.getLogger(QRCodeService.class);

    public byte[] generateQRCodeBytes(String data, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("Error generating QR code: {}", e.getMessage(), e);
            return null;
        }
    }

//    public String generateTicketQRCode(String ticketId) {
//        try {
//            String qrData = String.format(ticketId);
//            return generateQRCodeBase64(qrData, 300, 300);
//
//        } catch (Exception e) {
//            logger.error("Failed to generate QR code for ticket ID {}: {}",ticketId, e.getMessage(), e);
//            return null;
//        }
//    }
//
//    private String generateChecksum(BookingResponseDTO responseDTO) {
//        String data = responseDTO.getTicketId() + responseDTO.getTotalAmount();
//        return Integer.toHexString(data.hashCode());
//    }
}
