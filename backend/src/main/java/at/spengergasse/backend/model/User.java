package at.spengergasse.backend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractPersistable<Long>
{
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    private List<Itinerary> itineraries;

    @Builder
    private User(String username, String email, String password, List<Itinerary> itineraries)
    {
        this.username = username;
        this.email = email;
        this.password = password;

        this.itineraries = new ArrayList<>();
        if (itineraries != null && !itineraries.isEmpty()) itineraries.forEach(this::addItinerary);
    }

    protected boolean containsItinerary(Itinerary itinerary)
    {
        return this.itineraries.contains(itinerary);
    }

    protected void addItinerary(Itinerary itinerary)
    {
        if (itinerary == null) return;
        this.itineraries.add(itinerary);
        if (itinerary.getUser() != this) itinerary.setUser(this);
    }
}
