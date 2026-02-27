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
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    // TODO: Organized events
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL) // Any changes made to Event that User references is saved
    private List<Event> organizedEvents = new ArrayList<>();

    // TODO: Attending events
    @ManyToMany
    @JoinTable(     // Creating the Jointable on the Owning side of the relationship
            name = "user_attending_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> attendingEvents;

    // TODO: Staffing events
    @ManyToMany
    @JoinTable(
            name = "user_staffing_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> staffingEvents;

    // TODO: Ticket

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
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(createdAt, user.createdAt) && Objects.equals(updated, user.updated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, createdAt, updated);
    }
}
