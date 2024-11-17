package at.spengergasse.backend.mongodb.dto;

import at.spengergasse.backend.mongodb.model.Itinerary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ItineraryDto(
        UUID uuid,
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<ItineraryStepDto> itinerarySteps
) {
    public static ItineraryDto fromEntity(Itinerary itinerary)
    {
        ItineraryDto itineraryDto = new ItineraryDto(
                itinerary.getUuid(),
                itinerary.getName(),
                itinerary.getStartDate(),
                itinerary.getEndDate(),
                new ArrayList<>()
        );

        if (itinerary.getItinerarySteps() != null && !itinerary.getItinerarySteps().isEmpty())
        {
            itineraryDto.itinerarySteps().addAll(
                    itinerary.getItinerarySteps().stream()
                            .map(ItineraryStepDto::fromEntity)
                            .toList()
            );
        }

        return itineraryDto;
    }

    public static Itinerary toEntity(ItineraryDto itineraryDto)
    {
        Itinerary itinerary = Itinerary.builder()
                .uuid(itineraryDto.uuid())
                .name(itineraryDto.name())
                .startDate(itineraryDto.startDate())
                .endDate(itineraryDto.endDate())
                .build();

        if (itineraryDto.itinerarySteps() != null && !itineraryDto.itinerarySteps().isEmpty())
        {
            itinerary.getItinerarySteps().addAll(
                    itineraryDto.itinerarySteps().stream()
                            .map(ItineraryStepDto::toEntity)
                            .toList()
            );
        }

        return itinerary;
    }
}
