package at.spengergasse.backend.mongodb.dto;

import at.spengergasse.backend.mongodb.model.ItineraryStep;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record ItineraryStepDto(
        String name,
        LocalDateTime stepDate,
        List<RouteStopDto> routeStops
) {
    public static ItineraryStepDto fromEntity(ItineraryStep itineraryStep)
    {
        ItineraryStepDto itineraryStepDto = new ItineraryStepDto(
                itineraryStep.getName(),
                itineraryStep.getStepDate(),
                new ArrayList<>()
        );

        if (itineraryStep.getRouteStops() != null && !itineraryStep.getRouteStops().isEmpty())
        {
            itineraryStepDto.routeStops().addAll(
                   itineraryStep.getRouteStops().stream()
                           .map(RouteStopDto::fromEntity)
                           .toList()
           );
        }

        return itineraryStepDto;
    }

    public static ItineraryStep toEntity(ItineraryStepDto itineraryStepDto)
    {
        ItineraryStep itineraryStep = ItineraryStep.builder()
                .name(itineraryStepDto.name())
                .stepDate(itineraryStepDto.stepDate())
                .build();

        if (itineraryStepDto.routeStops() != null && !itineraryStepDto.routeStops().isEmpty())
        {
            itineraryStep.getRouteStops().addAll(
                    itineraryStepDto.routeStops().stream()
                            .map(RouteStopDto::toEntity)
                            .toList()
            );
        }

        return itineraryStep;
    }
}
