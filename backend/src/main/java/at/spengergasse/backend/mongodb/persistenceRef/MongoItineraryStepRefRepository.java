package at.spengergasse.backend.mongodb.persistenceRef;

import at.spengergasse.backend.mongodb.modelRef.ItineraryStep;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MongoItineraryStepRefRepository extends MongoRepository<ItineraryStep, String> {

    List<ItineraryStep> findAll();
    Optional<ItineraryStep> findById(String id);
    ItineraryStep save(ItineraryStep entity);
    void deleteById(String id);
}