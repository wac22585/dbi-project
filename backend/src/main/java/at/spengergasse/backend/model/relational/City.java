package at.spengergasse.backend.model.relational;

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
@Table(name = "cities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class City extends AbstractPersistable<Long>
{
    @Column(nullable = false)
    private String city;
    @Column(nullable = false, unique = true)
    private String country;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "currentCity")
    private List<RouteStop> routeStopsCurrent;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "nextCity")
    private List<RouteStop> routeStopsNext;

    @Builder
    private City(String city, String country, List<RouteStop> routeStopsCurrent, List<RouteStop> routeStopsNext)
    {
        this.city = city;
        this.country = country;

        this.routeStopsCurrent = new ArrayList<>();
        if (routeStopsCurrent != null && !routeStopsCurrent.isEmpty()) routeStopsCurrent.forEach(this::addRouteStopCurrent);
        this.routeStopsNext = new ArrayList<>();
        if (routeStopsNext != null && !routeStopsNext.isEmpty()) routeStopsNext.forEach(this::addRouteStopNext);
    }

    protected boolean containsCurrentRouteStop(RouteStop routeStop)
    {
        return this.routeStopsCurrent.contains(routeStop);
    }

    protected boolean containsNextRouteStop(RouteStop routeStop)
    {
        return this.routeStopsNext.contains(routeStop);
    }

    protected void addRouteStopCurrent(RouteStop routeStop)
    {
        if (routeStop == null) return;
        this.routeStopsCurrent.add(routeStop);
        if (routeStop.getCurrentCity() != this) routeStop.setCurrentCity(this);
    }

    protected void addRouteStopNext(RouteStop routeStop)
    {
        if (routeStop == null) return;
        this.routeStopsNext.add(routeStop);
        if (routeStop.getNextCity() != this) routeStop.setNextCity(this);
    }
}
