package at.spengergasse.backend.mongodb.dto;

import at.spengergasse.backend.mongodb.model.ItineraryStep;

import java.time.LocalDateTime;
import java.util.List;

public record ItineraryStepDto(
        String name,
        LocalDateTime stepDate,
        List<RouteStopDto> routeStops
) {
    public static ItineraryStepDto fromEntity(ItineraryStep itineraryStep)
    {
        return new ItineraryStepDto(
                itineraryStep.getName(),
                itineraryStep.getStepDate(),
                itineraryStep.getRouteStops().stream()
                        .map(RouteStopDto::fromEntity)
                        .toList()
        );
    }

    public static ItineraryStep toEntity(ItineraryStepDto itineraryStepDto)
    {
        return new ItineraryStep(
                itineraryStepDto.name(),
                itineraryStepDto.stepDate(),
                itineraryStepDto.routeStops().stream()
                        .map(RouteStopDto::toEntity)
                        .toList()
        );
    }
}
