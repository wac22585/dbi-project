package at.spengergasse.backend.mongodb.persistenceRef;

import at.spengergasse.backend.mongodb.modelRef.Itinerary;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoItineraryRefRepository extends MongoRepository<Itinerary, String>
{
    void deleteById(String id);
}