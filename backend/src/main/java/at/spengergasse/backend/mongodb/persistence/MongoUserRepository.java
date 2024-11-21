package at.spengergasse.backend.mongodb.persistence;

import at.spengergasse.backend.mongodb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MongoUserRepository extends MongoRepository<User, String>
{
    User findByEmail(String email);
    User findByUsername(String username);
    void deleteByUsername(String username);
    UserWithoutPwd findUserByEmail(String email);
}
