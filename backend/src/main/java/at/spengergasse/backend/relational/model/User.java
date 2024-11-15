package at.spengergasse.backend.relational.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
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

    public void addItinerary(Itinerary itinerary)
    {
        if (itinerary == null) return;
        this.itineraries.add(itinerary);
        if (itinerary.getUser() != this) itinerary.setUser(this);
    }

    public boolean containsItineraryId(Long id)
    {
        return this.itineraries.stream().anyMatch(itinerary -> Objects.equals(itinerary.getId(), id));
    }

    public void removeItinerary(Itinerary itinerary)
    {
        if (itinerary == null) return;
        this.itineraries.remove(itinerary);
        if (itinerary.getUser() == this) itinerary.setUser(null);
    }

    public void setId(Long id)
    {
        super.setId(id);
    }
}
