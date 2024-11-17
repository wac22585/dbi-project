package at.spengergasse.backend.mongodb.persistence;

import at.spengergasse.backend.mongodb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoUserRepository extends MongoRepository<User, String> {

    List<User> findAll();
    User findByEmail(String email);
    User findByUsername(String username);
    User save(User entity);
    void deleteByUsername(String username);
}
