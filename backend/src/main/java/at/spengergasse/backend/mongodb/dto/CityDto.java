package at.spengergasse.backend.mongodb.dto;

import at.spengergasse.backend.mongodb.model.City;

import java.util.List;

public record CityDto(
        String city,
        String country,
        List<RouteStopDto> routeStopsCurrent,
        List<RouteStopDto> routeStopsNext
) {
    public static CityDto fromEntity(City city)
    {
        return new CityDto(
                city.getCity(),
                city.getCountry(),
                city.getRouteStopsCurrent().stream()
                        .map(RouteStopDto::fromEntity)
                        .toList(),
                city.getRouteStopsNext().stream()
                        .map(RouteStopDto::fromEntity)
                        .toList()
        );
    }

    public static City toEntity(CityDto cityDto)
    {
        return new City(
                cityDto.city(),
                cityDto.country(),
                cityDto.routeStopsCurrent().stream()
                        .map(RouteStopDto::toEntity)
                        .toList(),
                cityDto.routeStopsNext().stream()
                        .map(RouteStopDto::toEntity)
                        .toList()
        );
    }
}
