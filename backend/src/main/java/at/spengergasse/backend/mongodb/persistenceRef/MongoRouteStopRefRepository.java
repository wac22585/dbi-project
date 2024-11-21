package at.spengergasse.backend.mongodb.persistenceRef;

import at.spengergasse.backend.mongodb.modelRef.RouteStop;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoRouteStopRefRepository extends MongoRepository<RouteStop, String>
{
    void deleteById(String id);
}
