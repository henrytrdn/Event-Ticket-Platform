package com.example.tickets.services.impl;

import com.example.tickets.domain.entities.Ticket;
import com.example.tickets.domain.entities.TicketStatusEnum;
import com.example.tickets.domain.entities.TicketType;
import com.example.tickets.domain.entities.User;
import com.example.tickets.exceptions.TicketTypeNotFoundException;
import com.example.tickets.exceptions.TicketsSoldOutException;
import com.example.tickets.exceptions.UserNotFoundException;
import com.example.tickets.repositories.TicketRepository;
import com.example.tickets.repositories.TicketTypeRepository;
import com.example.tickets.repositories.UserRepository;
import com.example.tickets.services.QrCodeService;
import com.example.tickets.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;

    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with ID %s was not found", userId)
        ));

        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId)
                .orElseThrow(() -> new TicketTypeNotFoundException(
                String.format("Ticket type with ID %s was not found", ticketTypeId)
        ));

        int purchasedTickets = ticketRepository.countByTicketTypeId(ticketType.getId());
        // Counts all Tickets which have a TicketType ID of the one the user wants to purchase

        int totalAvailable = ticketType.getTotalAvailable();
        // Check how many of those TicketType's were originally allocated

        if(purchasedTickets + 1 > totalAvailable) {
            throw new TicketsSoldOutException();
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);

        Ticket savedTicket = ticketRepository.save(ticket);
        qrCodeService.generateQrCode(savedTicket);

        return ticketRepository.save(savedTicket);
    }
}
