package at.spengergasse.backend.mongodb.modelRef;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "itineraryStepsRef")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItineraryStep
{
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    private LocalDateTime stepDate;
    @DBRef
    private Itinerary itinerary;
    private List<RouteStop> routeStops;

    @Builder
    public ItineraryStep(String name, LocalDateTime stepDate, Itinerary itinerary, List<RouteStop> routeStops)
    {
        this.name = name;
        this.stepDate = stepDate;
        this.itinerary = itinerary;
        this.routeStops = routeStops != null ? routeStops : new ArrayList<>();
    }
}
