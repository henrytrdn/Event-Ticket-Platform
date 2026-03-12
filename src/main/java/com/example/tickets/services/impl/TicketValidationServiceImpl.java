package com.example.tickets.services.impl;

import com.example.tickets.domain.entities.*;
import com.example.tickets.exceptions.QrCodeNotFoundException;
import com.example.tickets.exceptions.TicketNotFoundException;
import com.example.tickets.repositories.QrCodeRepository;
import com.example.tickets.repositories.TicketRepository;
import com.example.tickets.repositories.TicketValidationRepository;
import com.example.tickets.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {

    private final QrCodeRepository qrCodeRepository;
    private final TicketValidationRepository ticketValidationRepository;
    private final TicketRepository ticketRepository;

    @Override
    // First successful scan → creates a TicketValidation with status VALID
    // Every later scan of the same ticket → creates another TicketValidation with status INVALID
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
                .orElseThrow(() -> new QrCodeNotFoundException(
                        String.format(
                                "Qr Code with ID %s was not found", qrCodeId
                        )
                ));

        Ticket ticket = qrCode.getTicket();

        return validateTicket(ticket, TicketValidationMethodEnum.QR_SCAN);

    }

    // Validate Ticket by creating and referencing a TicketValidation object
    private TicketValidation validateTicket(Ticket ticket,
            TicketValidationMethodEnum ticketValidationMethodEnum) {
        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(ticketValidationMethodEnum);

        TicketValidationStatusEnum ticketValidationStatus = ticket.getValidations().stream()
                .filter(validations -> TicketValidationStatusEnum.VALID.equals(validations.getStatus()))
                .findFirst()
                .map(validation -> TicketValidationStatusEnum.INVALID)
                .orElse(TicketValidationStatusEnum.VALID);

        ticketValidation.setStatus(ticketValidationStatus);

        return ticketValidationRepository.save(ticketValidation);
    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException());

        return validateTicket(ticket, TicketValidationMethodEnum.MANUAL);
    }
}
