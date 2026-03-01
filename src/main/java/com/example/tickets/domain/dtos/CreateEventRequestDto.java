package com.example.tickets.domain.dtos;

import com.example.tickets.domain.entities.EventStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequestDto {
    // DTO's with 'Dto' suffix are used in the Presentation Layer
    // Uses Spring Boot Starter Validation to validate incoming data before it reaches the service and persistence layer
    // This is one reason why we have 2 sets of DTO's that seem identical. This one exists in the presentation layer to
    // stop junk input early on.

    // Recall, Spring maps JSON data to the DTO object. Spring will flag the error msg if the field is blank
    @NotBlank(message = "Event name is required")
    private String name;


    private LocalDateTime start;
    private LocalDateTime end;

    @NotBlank(message = "Venue information is required")
    private String venue;


    private LocalDateTime salesStart;
    private LocalDateTime salesEnd;

    @NotNull(message = "Event status must be provided")
    private EventStatusEnum status;

    @NotEmpty(message = "At least one ticket type is required")
    @Valid // activates the validation annotations nested inside CreateTicketTypeRequestDto
    private List<CreateTicketTypeRequestDto> ticketTypes;
}
