package at.spengergasse.backend.mongodb.dto;

import at.spengergasse.backend.mongodb.model.User;

import java.util.ArrayList;
import java.util.List;

public record UserDto(
        String username,
        String email,
        String password,
        List<ItineraryDto> itineraries
) {
    public static UserDto fromEntity(User user)
    {
        UserDto userDto = new UserDto(
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );

        if (user.getItineraries() != null && !user.getItineraries().isEmpty())
        {
            userDto.itineraries().addAll(user.getItineraries().stream()
                    .map(ItineraryDto::fromEntity)
                    .toList());
        }

        return userDto;
    }

    public static User toEntity(UserDto userDto)
    {
        User user = User.builder()
                .username(userDto.username())
                .email(userDto.email())
                .password(userDto.password())
                .build();

        if (userDto.itineraries() != null && !userDto.itineraries().isEmpty())
        {
            user.getItineraries().addAll(userDto.itineraries().stream()
                    .map(ItineraryDto::toEntity)
                    .toList());
        }

        return user;
    }
}
