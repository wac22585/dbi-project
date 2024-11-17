package at.spengergasse.backend.mongodb.persistence;

import at.spengergasse.backend.relational.model.RouteStop;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MongoRouteStopRepository extends MongoRepository<RouteStop, String> {

    List<RouteStop> findAll();
    Optional<RouteStop> findById(String id);
    RouteStop save(RouteStop entity);
    void deleteById(String id);
}
