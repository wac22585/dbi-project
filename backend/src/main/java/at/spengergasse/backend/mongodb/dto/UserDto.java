package at.spengergasse.backend.mongodb.dto;


import java.util.List;

public record UserDto(
        String id,
        String username,
        String email,
        String password,
        List<ItineraryDto> itineraries
) { }
