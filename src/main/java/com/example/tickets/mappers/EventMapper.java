package com.example.tickets.mappers;

import com.example.tickets.domain.CreateEventRequest;
import com.example.tickets.domain.CreateTicketTypeRequest;
import com.example.tickets.domain.dtos.*;
import com.example.tickets.domain.entities.Event;
import com.example.tickets.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE) // ignore anything you can't map
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);  // After creating Event in the Service Layer, map a subset of information in
    // the entity to the Response Dto

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    ListEventResponseDto toListEventResponseDto(Event event);

    GetEventDetailsTicketTypesResponseDto toGetEventDetailsTicketTypesResponseDto(TicketType ticketType);

    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);
}
