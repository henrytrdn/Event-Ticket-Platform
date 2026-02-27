package com.example.tickets.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketTypeRequest {  // DTO which maps TicketType entity
    private String name;
    private Double price;
    private String description;
    private Integer totalAvailable;
}
