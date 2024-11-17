package at.spengergasse.backend.relational.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "route_stops")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteStop extends AbstractPersistable<Long>
{
    private LocalDateTime arrivalTime;
    @Column(nullable = false)
    private float totalDistance;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private ItineraryStep itineraryStep;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private City currentCity;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private City nextCity;

    @Builder
    private RouteStop(LocalDateTime arrivalTime, float totalDistance, ItineraryStep itineraryStep, City currentCity, City nextCity)
    {
        this.arrivalTime = arrivalTime;
        this.totalDistance = totalDistance;

        setItineraryStep(itineraryStep);
        setCurrentCity(currentCity);
        setNextCity(nextCity);
    }

    protected void setItineraryStep(ItineraryStep itineraryStep)
    {
        if (itineraryStep == null) return;
        this.itineraryStep = itineraryStep;
        if (!itineraryStep.containsRouteStop(this)) itineraryStep.addRouteStop(this);
    }

    protected void setCurrentCity(City currentCity)
    {
        if (currentCity == null) return;
        this.currentCity = currentCity;
        if (!currentCity.containsCurrentRouteStop(this)) currentCity.addRouteStopCurrent(this);
    }

    protected void setNextCity(City nextCity)
    {
        if (nextCity == null) return;
        this.nextCity = nextCity;
        if (!nextCity.containsNextRouteStop(this)) nextCity.addRouteStopNext(this);
    }
}
