package at.spengergasse.backend.mongodb.model;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "itinerary_steps")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItineraryStep
{
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    private LocalDateTime stepDate;
    private List<RouteStop> routeStops;

    @Builder
    public ItineraryStep(String name, LocalDateTime stepDate, List<RouteStop> routeStops)
    {
        this.name = name;
        this.stepDate = stepDate;
        this.routeStops = routeStops != null ? routeStops : new ArrayList<>();
    }
}
