package at.spengergasse.backend.mongodb.persistence;

import at.spengergasse.backend.mongodb.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoCityRepository extends MongoRepository<City, String> { }
