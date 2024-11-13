package at.spengergasse.backend.dto;

import at.spengergasse.backend.model.relational.Itinerary;

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
)
{
    //This method is used to convert an entity to a dto for the service layer
    public static ItineraryDto fromEntity(Itinerary itinerary) {
        ItineraryDto itineraryDto = new ItineraryDto(
                itinerary.getUuid(),
                itinerary.getName(),
                itinerary.getStartDate(),
                itinerary.getEndDate(),
                new ArrayList<>()
        );
        if (itinerary.getItinerarySteps() != null && !itinerary.getItinerarySteps().isEmpty()) {
            itinerary.getItinerarySteps()
                    .forEach(itineraryStep ->
                            itineraryDto.itinerarySteps
                                    .add(ItineraryStepDto.fromEntity(itineraryStep)));
        }
        return itineraryDto;
    }

    public static Itinerary toEntity(ItineraryDto itineraryDto) {
        Itinerary itinerary =  Itinerary.builder()
                .uuid(itineraryDto.uuid)
                .name(itineraryDto.name)
                .startDate(itineraryDto.startDate)
                .endDate(itineraryDto.endDate)
                .build();

        if (itineraryDto.itinerarySteps != null && !itineraryDto.itinerarySteps.isEmpty()) {
            itineraryDto.itinerarySteps
                    .forEach(itineraryStepDto ->
                            itinerary.getItinerarySteps()
                                    .add(ItineraryStepDto.toEntity(itineraryStepDto)));
        }
        return itinerary;
    }
}
