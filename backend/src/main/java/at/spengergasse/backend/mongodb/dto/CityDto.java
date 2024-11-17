package at.spengergasse.backend.mongodb.dto;

import at.spengergasse.backend.mongodb.model.City;

import java.util.List;

public record CityDto(
        String city,
        String country
) {
    public static CityDto fromEntity(City city)
    {
        return new CityDto(
                city.getCity(),
                city.getCountry()
        );
    }

    public static City toEntity(CityDto cityDto)
    {
        return new City(
                cityDto.city(),
                cityDto.country()
        );
    }
}
