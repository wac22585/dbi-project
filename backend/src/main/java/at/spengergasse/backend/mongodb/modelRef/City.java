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

@Document(collection = "citiesRef")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class City
{
    @Id
    private String id;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false, unique = true)
    private String country;
    @DBRef
    private RouteStop routeStopCurrent;
    @DBRef
    private RouteStop routeStopNext;

    @Builder
    public City(String city, String country, RouteStop routeStopCurrent, RouteStop routeStopNext)
    {
        this.city = city;
        this.country = country;
        this.routeStopCurrent = routeStopCurrent;
        this.routeStopNext = routeStopNext;
    }
}
