package at.spengergasse.backend.mongodb.dto;

import java.time.LocalDateTime;

public record RouteStopDto (
        String id,
        LocalDateTime arrivalTime,
        float totalDistance,
        CityDto currentCity,
        CityDto nextCity
) { }
