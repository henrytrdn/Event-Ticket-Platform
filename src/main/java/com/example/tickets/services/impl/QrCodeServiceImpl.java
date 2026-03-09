package com.example.tickets.services.impl;

import com.example.tickets.domain.entities.QrCode;
import com.example.tickets.domain.entities.QrCodeStatusEnum;
import com.example.tickets.domain.entities.Ticket;
import com.example.tickets.exceptions.QrCodeGenerationException;
import com.example.tickets.repositories.QrCodeRepository;
import com.example.tickets.services.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

    private static final int QR_HEIGHT = 300;
    private static final int QR_WIDTH = 300;

    private final QRCodeWriter qrCodeWriter;
    private QrCodeRepository qrCodeRepository;

    @Override
    public QrCode generateQrCode(Ticket ticket) {
        try {
            UUID uniqueId = UUID.randomUUID(); // Unique ID baked into the QR code
            String qrCodeImage = generateQrCodeImage(uniqueId);

            QrCode qrCode = new QrCode();
            qrCode.setId(uniqueId);
            qrCode.setStatus(QrCodeStatusEnum.ACTIVE);
            qrCode.setValue(qrCodeImage);
            qrCode.setTicket(ticket);

            return qrCodeRepository.saveAndFlush(qrCode);

        } catch(IOException | WriterException ex) {
            throw new QrCodeGenerationException("Failed to generate QR Code", ex);
        }
    }

    // Returns a Base 64 encoded QR code image
    private String generateQrCodeImage(UUID uniqueId) throws WriterException, IOException {
        BitMatrix bitMatrix = qrCodeWriter.encode(
                uniqueId.toString(),
                BarcodeFormat.QR_CODE,
                QR_WIDTH,
                QR_HEIGHT
        );  // Encodes unique UUID into a QR Code from ZXing library. Stored in the Matrix, BitMatrix of 0's and 1's
        // 1 = Black pixel, 0 = White pixel

        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        // A BufferedImage is a real image made of pixels.
        // Each pixel stores color information, not just 0 or 1.
        // We store RGB values instead, e.g. (0,0,0) or (255, 255, 255)

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(qrCodeImage, "PNG", baos);    // Export the Image into a PNG
            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);

        }
    }
}
