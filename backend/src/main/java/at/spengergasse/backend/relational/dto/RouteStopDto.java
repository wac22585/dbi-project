package at.spengergasse.backend.relational.dto;

import at.spengergasse.backend.relational.model.RouteStop;

import java.time.LocalDateTime;

public record RouteStopDto(
        LocalDateTime arrivalTime,
        float totalDistance,
        CityDto currentCity,
        CityDto nextCity)
{
    public static RouteStopDto fromEntity(RouteStop routeStop)
    {
        return new RouteStopDto(
                routeStop.getArrivalTime(),
                routeStop.getTotalDistance(),
                CityDto.fromEntity(routeStop.getCurrentCity()),
                CityDto.fromEntity(routeStop.getNextCity())
        );
    }

    public static RouteStop toEntity(RouteStopDto routeStop)
    {
        return RouteStop.builder()
                .arrivalTime(routeStop.arrivalTime)
                .totalDistance(routeStop.totalDistance)
                .currentCity(CityDto.toEntity(routeStop.currentCity))
                .nextCity(CityDto.toEntity(routeStop.nextCity))
                .build();
    }
}
