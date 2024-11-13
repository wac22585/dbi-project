package at.spengergasse.backend.model.mongodb;

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
public class RouteStop {

    @Id
    private String id;
    private LocalDateTime arrivalTime;
    private float totalDistance;
    private City currentCity;
    private City nextCity;

    @Builder
    public RouteStop(LocalDateTime arrivalTime, float totalDistance, City currentCity, City nextCity) {
        this.arrivalTime = arrivalTime;
        this.totalDistance = totalDistance;
        this.currentCity = currentCity;
        this.nextCity = nextCity;
    }
}
