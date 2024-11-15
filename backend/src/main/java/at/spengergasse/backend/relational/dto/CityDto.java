package at.spengergasse.backend.relational.dto;

import at.spengergasse.backend.relational.model.City;

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
