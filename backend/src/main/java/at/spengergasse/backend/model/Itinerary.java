package at.spengergasse.backend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "itineraries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Itinerary extends AbstractPersistable<Long>
{
    @Column(nullable = false)
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "itinerary")
    private List<ItineraryStep> itinerarySteps;

    @Builder
    private Itinerary(String name, LocalDateTime startDate, LocalDateTime endDate, User user, List<ItineraryStep> itinerarySteps)
    {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;

        setUser(user);
        this.itinerarySteps = new ArrayList<>();
        if (itinerarySteps != null && !itinerarySteps.isEmpty()) itinerarySteps.forEach(this::addItineraryStep);
    }

    public void setUser(User user)
    {
        if (user == null) return;
        this.user = user;
        if (!user.containsItinerary(this)) user.addItinerary(this);
    }

    protected boolean containsItineraryStep(ItineraryStep itineraryStep)
    {
        return this.itinerarySteps.contains(itineraryStep);
    }

    protected void addItineraryStep(ItineraryStep itineraryStep)
    {
        if (itineraryStep == null) return;
        this.itinerarySteps.add(itineraryStep);
        if (itineraryStep.getItinerary() != this) itineraryStep.setItinerary(this);
    }
}
