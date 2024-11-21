package at.spengergasse.backend.mongodb.persistenceRef;

import at.spengergasse.backend.mongodb.modelRef.RouteStop;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MongoRouteStopRefRepository extends MongoRepository<RouteStop, String> {

    List<RouteStop> findAll();
    Optional<RouteStop> findById(String id);
    RouteStop save(RouteStop entity);
    void deleteById(String id);
}
