package at.spengergasse.backend.mongodb.persistenceRef;

import at.spengergasse.backend.mongodb.modelRef.City;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MongoCityRefRepository extends MongoRepository<City, String> {

    List<City> findAll();
    Optional<City> findById(String id);
    City save(City entity);
    void deleteById(String id);
}
