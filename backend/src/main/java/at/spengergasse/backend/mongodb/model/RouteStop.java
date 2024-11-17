package at.spengergasse.backend.mongodb.model;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "route_stops")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteStop
{
    @Id
    private String id;
    @Column(nullable = false)
    private float totalDistance;
    private LocalDateTime arrivalTime;
    private City currentCity;
    private City nextCity;

    @Builder
    public RouteStop(LocalDateTime arrivalTime, float totalDistance, City currentCity, City nextCity)
    {
        this.arrivalTime = arrivalTime;
        this.totalDistance = totalDistance;
        this.currentCity = currentCity;
        this.nextCity = nextCity;
    }
}
