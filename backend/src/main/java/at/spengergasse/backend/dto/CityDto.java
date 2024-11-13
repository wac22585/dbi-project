package at.spengergasse.backend.dto;

import at.spengergasse.backend.model.relational.City;

public record CityDto(
        String city,
        String country)
{
    public static CityDto fromEntity(City city)
    {
        return new CityDto(city.getCity(), city.getCountry());
    }

    public static City toEntity(CityDto city)
    {
        return City.builder()
                .city(city.city)
                .country(city.country)
                .build();
    }
}
