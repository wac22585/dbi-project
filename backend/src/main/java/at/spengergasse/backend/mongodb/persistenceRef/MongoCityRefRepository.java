package at.spengergasse.backend.mongodb.persistenceRef;

import at.spengergasse.backend.mongodb.modelRef.City;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCityRefRepository extends MongoRepository<City, String>
{
    void deleteById(String id);
}
