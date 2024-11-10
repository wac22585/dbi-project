package at.spengergasse.backend.dto;

import at.spengergasse.backend.model.Itinerary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record ItineraryDto(
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<ItineraryStepDto> itinerarySteps
)
{
    //This method is used to convert an entity to a dto for the service layer
    public static ItineraryDto fromEntity(Itinerary itinerary) {
        ItineraryDto itineraryDto = new ItineraryDto(
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
