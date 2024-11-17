package at.spengergasse.backend.mongodb.dto;


import at.spengergasse.backend.mongodb.model.User;

import java.util.List;

public record UserDto(
        String username,
        String email,
        String password,
        List<ItineraryDto> itineraries
) {
    public static UserDto fromEntity(User user)
    {
        return new UserDto(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getItineraries().stream()
                        .map(ItineraryDto::fromEntity)
                        .toList()
        );
    }

    public static User toEntity(UserDto userDto)
    {
        return new User(
                userDto.username(),
                userDto.email(),
                userDto.password(),
                userDto.itineraries().stream()
                        .map(ItineraryDto::toEntity)
                        .toList()
        );
    }
}
