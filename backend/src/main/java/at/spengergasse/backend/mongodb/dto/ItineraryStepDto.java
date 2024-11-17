package at.spengergasse.backend.mongodb.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ItineraryStepDto(
        String id,
        String name,
        LocalDateTime stepDate,
        List<RouteStopDto> routeStops
) { }
