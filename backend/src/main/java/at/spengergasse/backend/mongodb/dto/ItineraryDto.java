package at.spengergasse.backend.mongodb.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ItineraryDto(
        String id,
        UUID uuid,
        String name,
        LocalDateTime startDate,
        LocalDateTime endDate,
        UserDto user,
        List<ItineraryStepDto> itinerarySteps
) { }
