package at.spengergasse.backend.mongodb.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "cities")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class City
{
    @Id
    private String id;
    private String city;
    private String country;
    private List<RouteStop> routeStopsCurrent;
    private List<RouteStop> routeStopsNext;

    @Builder
    public City(String city, String country, List<RouteStop> routeStopsCurrent, List<RouteStop> routeStopsNext) {
        this.city = city;
        this.country = country;
        this.routeStopsCurrent = routeStopsCurrent != null ? routeStopsCurrent : new ArrayList<>();
        this.routeStopsNext = routeStopsNext != null ? routeStopsNext : new ArrayList<>();
    }
}
