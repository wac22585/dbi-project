package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.RouteStop;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RouteStopRepository extends Repository<RouteStop, Long>
{
    RouteStop save(RouteStop routeStop);
    RouteStop findById(Long id);
    void deleteById(Long id);
    void deleteAll();

    List<RouteStop> findByItineraryStepId(Long id);
    List<RouteStop> findAll();
}
