package at.spengergasse.backend.mongodb.persistence;

import at.spengergasse.backend.mongodb.model.Itinerary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface MongoItineraryRepository extends MongoRepository<Itinerary, String>
{
    Itinerary findByUuid(UUID uuid);;
    void deleteById(String id);
    void deleteByUuid(UUID uuid);
}