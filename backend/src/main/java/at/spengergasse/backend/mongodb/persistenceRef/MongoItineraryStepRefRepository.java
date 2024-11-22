package at.spengergasse.backend.mongodb.persistenceRef;

import at.spengergasse.backend.mongodb.modelRef.ItineraryStep;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoItineraryStepRefRepository extends MongoRepository<ItineraryStep, String>
{
    void deleteById(String id);
}