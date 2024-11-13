package at.spengergasse.backend.dto;

import at.spengergasse.backend.model.relational.User;

import java.util.List;

public record UserDto(
        String username,
        String email,
        String password,
        List<ItineraryDto> itineraries)
{
    //This method is used to convert an entity to a dto for the service layer
    public static UserDto fromEntity(User user) {
        return new UserDto(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getItineraries().stream().map((ItineraryDto::fromEntity)).toList()
        );
    }

    //This method is used to convert a dto to an entity for the service layer
    public static User toEntity(UserDto userDto) {
        return User.builder()
                .username(userDto.username)
                .email(userDto.email)
                .password(userDto.password)
                .itineraries(userDto.itineraries().stream().map(ItineraryDto::toEntity).toList())
                .build();
    }
}
