package com.example.tickets.services;

import com.example.tickets.domain.CreateEventRequest;
import com.example.tickets.domain.entities.Event;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);

}
