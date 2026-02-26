package com.example.tickets.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID) // Generates a random UUID when id is null
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "end")
    private LocalDateTime end;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "sales_start")
    private LocalDateTime salesStart;

    @Column(name = "sales_end")
    private LocalDateTime salesEnd;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING) // The string representation of the enum is stored in the database instead of an integer
    private EventStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY) // Tells JPA each Event has one User, and a User can organize many events
    @JoinColumn(name = "organizer_id") // Add a foreign key column in the events table
    private User organizer;

    @ManyToMany(mappedBy = "attendingEvents")   // This tells JPA that Event does not own the relationship, instead the Users side owns the relationship.
    // Hence, we need to implement the jointable on the Users side.
    // The owning side is the single source of truth for writing to the join table.
    // This prevents duplicate inserts, conflicting updates, and inconsistent relationships.
    private List<User> attendees = new ArrayList<>(); // Keeps track of list of attendees attending this Event

    @ManyToMany(mappedBy = "staffingEvents")
    private List<User> staff = new ArrayList<>();

    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

}
