package com.example.tickets.controllers;

import com.example.tickets.domain.CreateEventRequest;
import com.example.tickets.domain.UpdateEventRequest;
import com.example.tickets.domain.dtos.*;
import com.example.tickets.domain.entities.Event;
import com.example.tickets.mappers.EventMapper;
import com.example.tickets.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/events") // Every endpoint inside this controller starts with /api/v1/events
@RequiredArgsConstructor
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateEventRequestDto createEventRequestDto) {

        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
        UUID userId = parseUserId(jwt);

        Event createdEvent = eventService.createEvent(userId, createEventRequest);
        CreateEventResponseDto createEventResponseDto = eventMapper.toDto(createdEvent);
        return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{eventId}")
    public ResponseEntity<UpdateEventResponseDto> updateEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId,
            @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto) {

        UpdateEventRequest updateEventRequest = eventMapper.fromDto(updateEventRequestDto);
        UUID userId = parseUserId(jwt);

        Event updatedEvent = eventService.updateEventForOrganizer(
                userId, eventId, updateEventRequest
        );

        UpdateEventResponseDto updateEventResponseDto = eventMapper.toUpdateEventResponseDto(updatedEvent);

        return ResponseEntity.ok(updateEventResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEvents(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable   // The GET request from the client can contain query params in the URL to build the Pageable object
    ) {
        UUID userId = parseUserId(jwt);
        Page<Event> events = eventService.listEventsForOrganizer(userId, pageable);
        return ResponseEntity.ok(events.map(event -> eventMapper.toListEventResponseDto(event)));
        // Map method means convert each element inside the page to another object
        // This preserves the Page metadata such as total pages, total elements, page number
        // Runs the lambda method inside to map each Event to ListEventResponseDto,
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetEventDetailsResponseDto> getEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId
    ) {
        UUID userId = parseUserId(jwt);
        return eventService.getEventForOrganizer(userId, eventId)
                .map(eventObj -> eventMapper.toGetEventDetailsResponseDto(eventObj))
                .map(dto -> ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build()); // 404 Error
    }
    // .map takes the Event object in Optional<Event> and applies the lambda function. I.e. mapping it to the DTO
    // then it ResponseEntity.ok(dto) will wrap the dto with the ResponseEntity.Ok message

    private UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }


}
