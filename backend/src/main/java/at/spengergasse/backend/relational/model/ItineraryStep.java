package at.spengergasse.backend.relational.model;

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
@Table(name = "itinerary_steps")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItineraryStep extends AbstractPersistable<Long>
{
    @Column(nullable = false)
    private String name;
    private LocalDateTime stepDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Itinerary itinerary;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "itineraryStep")
    private List<RouteStop> routeStops;

    @Builder
    private ItineraryStep(String name, LocalDateTime stepDate, Itinerary itinerary, List<RouteStop> routeStops)
    {
        this.name = name;
        this.stepDate = stepDate;

        setItinerary(itinerary);
        this.routeStops = new ArrayList<>();
        if (routeStops != null && !routeStops.isEmpty()) routeStops.forEach(this::addRouteStop);
    }

    protected void setItinerary(Itinerary itinerary)
    {
        if (itinerary == null) return;
        this.itinerary = itinerary;
        if (!itinerary.containsItineraryStep(this)) itinerary.addItineraryStep(this);
    }

    protected boolean containsRouteStop(RouteStop routeStop)
    {
        return this.routeStops.contains(routeStop);
    }

    protected void addRouteStop(RouteStop routeStop)
    {
        if (routeStop == null) return;
        this.routeStops.add(routeStop);
        if (routeStop.getItineraryStep() != this) routeStop.setItineraryStep(this);
    }
}
