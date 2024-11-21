package at.spengergasse.backend.mongodb.persistenceRef;

import at.spengergasse.backend.mongodb.modelRef.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserRefRepository extends MongoRepository<User, String> {

}
