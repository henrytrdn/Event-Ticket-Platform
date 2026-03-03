package com.example.tickets.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketTypeRequest {  // DTO which maps TicketType entity. Used in the Service Layer
    private UUID id;
    private String name;
    private Double price;
    private String description;
    private Integer totalAvailable;
}
