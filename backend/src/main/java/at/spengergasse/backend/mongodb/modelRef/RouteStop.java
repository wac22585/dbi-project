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

@Document(collection = "routeStopsRef")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteStop
{
    @Id
    private String id;
    @Column(nullable = false)
    private float totalDistance;
    private LocalDateTime arrivalTime;
    @DBRef
    private ItineraryStep itineraryStep;
    @DBRef
    private City currentCity;
    @DBRef
    private City nextCity;

    @Builder
    public RouteStop(LocalDateTime arrivalTime, float totalDistance, ItineraryStep itineraryStep, City currentCity, City nextCity)
    {
        this.arrivalTime = arrivalTime;
        this.totalDistance = totalDistance;
        this.itineraryStep = itineraryStep;
        this.currentCity = currentCity;
        this.nextCity = nextCity;
    }
}
