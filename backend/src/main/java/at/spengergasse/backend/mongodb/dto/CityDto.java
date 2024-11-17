package at.spengergasse.backend.mongodb.dto;

import java.util.List;

public record CityDto(
        String id,
        String city,
        String country,
        List<RouteStopDto> routeStopsCurrent,
        List<RouteStopDto> routeStopsNext
) { }
