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
@Table(name = "ticket_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketType {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "description")
    private String description;

    @Column(name = "totalAvailable", nullable = true)
    private Integer totalAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    // TODO: Tickets
    @OneToMany(mappedBy = "ticketType", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    // Audit Fields
    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketType that = (TicketType) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(description, that.description) && Objects.equals(totalAvailable, that.totalAvailable) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updated, that.updated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, description, totalAvailable, createdAt, updated);
    }
}
