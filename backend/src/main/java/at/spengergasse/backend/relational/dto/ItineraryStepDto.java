package at.spengergasse.backend.relational.dto;

import at.spengergasse.backend.relational.model.ItineraryStep;

import java.time.LocalDateTime;
import java.util.List;

public record ItineraryStepDto(
        String name,
        LocalDateTime stepDate,
        List<RouteStopDto> routeStops)
{
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
        return ItineraryStep.builder()
                .name(itineraryStepDto.name())
                .stepDate(itineraryStepDto.stepDate())
                .routeStops(itineraryStepDto.routeStops().stream()
                        .map(RouteStopDto::toEntity)
                        .toList())
                .build();
    }
}
