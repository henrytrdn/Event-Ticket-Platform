package com.example.tickets.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Column(name = "event_start")
    private LocalDateTime start;

    @Column(name = "event_end")
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

    // Relationship with Organizer, Attendee, Staff
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id") // Add a foreign key column in the Events table.
    // Note: We add the FK in the Many side of the relationship. The Many side usually OWNS the relationship
    private User organizer;

    @ManyToMany(mappedBy = "attendingEvents")   // mappedBy tells us that Event does not own the relationship, instead the Users side owns the relationship.
    // Hence, we need to implement the jointable on the Users side.
    // The owning side is the single source of truth for writing to the join table.
    // This prevents duplicate inserts, conflicting updates, and inconsistent relationships.
    private List<User> attendees = new ArrayList<>();

    @ManyToMany(mappedBy = "staffingEvents")
    private List<User> staff = new ArrayList<>();

    // Relationship with TicketType
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)  // Non owning entity. This Event entity is mapped by the field named "event" in the TicketType class
    private List<TicketType> ticketTypes = new ArrayList<>();


    // Audit Fields
    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    // When creating the equals() and hashCode() methods, we want to exclude the fields which reference other entities

    // Why?
    // When Event.equals() compares a field that references an entity such as "organizer" which references the User, Java calls User.equals().
    // Similarly, if User.equals() compares its list of events, this triggers Event.equals() again,
    // creating a circular chain of equality checks that repeats indefinitely and results in a StackOverflowError.
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(start, event.start) && Objects.equals(end, event.end) && Objects.equals(venue, event.venue) && Objects.equals(salesStart, event.salesStart) && Objects.equals(salesEnd, event.salesEnd) && status == event.status && Objects.equals(createdAt, event.createdAt) && Objects.equals(updated, event.updated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, start, end, venue, salesStart, salesEnd, status, createdAt, updated);
    }
}
