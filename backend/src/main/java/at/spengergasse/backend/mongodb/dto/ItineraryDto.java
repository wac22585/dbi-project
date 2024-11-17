package at.spengergasse.backend.mongodb.dto;

import at.spengergasse.backend.mongodb.model.Itinerary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ItineraryDto(
        UUID uuid,
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        UserDto user,
        List<ItineraryStepDto> itinerarySteps
) {
    public static ItineraryDto fromEntity(Itinerary itinerary)
    {
        return new ItineraryDto(
                itinerary.getUuid(),
                itinerary.getName(),
                itinerary.getStartDate(),
                itinerary.getEndDate(),
                UserDto.fromEntity(itinerary.getUser()),
                itinerary.getItinerarySteps().stream()
                        .map(ItineraryStepDto::fromEntity)
                        .toList()
        );
    }

    public static Itinerary toEntity(ItineraryDto itineraryDto)
    {
        return new Itinerary(
                itineraryDto.uuid(),
                itineraryDto.name(),
                itineraryDto.startDate(),
                itineraryDto.endDate(),
                UserDto.toEntity(itineraryDto.user()),
                itineraryDto.itinerarySteps().stream()
                        .map(ItineraryStepDto::toEntity)
                        .toList()
        );
    }
}
