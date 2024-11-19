package at.spengergasse.backend.mongodb.persistence;

import at.spengergasse.backend.mongodb.model.ItineraryStep;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MongoItineraryStepRepository extends MongoRepository<ItineraryStep, String> {

    List<ItineraryStep> findAll();
    Optional<ItineraryStep> findById(String id);
    ItineraryStep save(ItineraryStep entity);
    void deleteById(String id);
}