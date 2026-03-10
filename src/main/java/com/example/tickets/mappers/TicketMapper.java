package com.example.tickets.mappers;

import com.example.tickets.domain.dtos.ListTicketResponseDto;
import com.example.tickets.domain.dtos.ListTicketTicketTypeResponseDto;
import com.example.tickets.domain.entities.Ticket;
import com.example.tickets.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    ListTicketTicketTypeResponseDto toListTicketTicketTypeResponseDto(TicketType ticketType);

    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);
}
