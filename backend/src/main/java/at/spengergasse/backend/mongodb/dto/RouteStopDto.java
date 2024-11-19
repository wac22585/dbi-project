package at.spengergasse.backend.mongodb.dto;

import at.spengergasse.backend.mongodb.model.RouteStop;

import java.time.LocalDateTime;

public record RouteStopDto (
        LocalDateTime arrivalTime,
        float totalDistance,
        CityDto currentCity,
        CityDto nextCity
) {
    public static RouteStopDto fromEntity(RouteStop routeStop)
    {

        return new RouteStopDto(
                routeStop.getArrivalTime(),
                routeStop.getTotalDistance(),
                CityDto.fromEntity(routeStop.getCurrentCity()),
                CityDto.fromEntity(routeStop.getNextCity())
        );
    }

    public static RouteStop toEntity(RouteStopDto routeStopDto)
    {
        return new RouteStop(
                routeStopDto.arrivalTime(),
                routeStopDto.totalDistance(),
                CityDto.toEntity(routeStopDto.currentCity()),
                CityDto.toEntity(routeStopDto.nextCity())
        );
    }
}
