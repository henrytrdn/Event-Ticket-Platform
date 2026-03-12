package com.example.tickets.domain.dtos;

import com.example.tickets.domain.entities.TicketValidationMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketValidationRequestDto {
    private UUID id;    // ticketId or qrCodeId
    private TicketValidationMethodEnum method;
}
