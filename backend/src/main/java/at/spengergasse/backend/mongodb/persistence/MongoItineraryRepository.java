package at.spengergasse.backend.mongodb.persistence;

import at.spengergasse.backend.mongodb.model.Itinerary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MongoItineraryRepository extends MongoRepository<Itinerary, String> {

    List<Itinerary> findAll();
    Optional<Itinerary> findById(String id);
    Itinerary save(Itinerary entity);
    void deleteById(String id);
}