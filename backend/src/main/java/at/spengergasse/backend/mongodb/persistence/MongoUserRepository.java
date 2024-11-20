package at.spengergasse.backend.mongodb.persistence;

import at.spengergasse.backend.mongodb.model.User;
import at.spengergasse.backend.mongodb.persistence.UserWithoutPwd;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MongoUserRepository extends MongoRepository<User, String> {

    List<User> findAll();
    @Query("{ '?0' : ?1 }")
    List<User> findByField(String fieldName, String value);
    @Query(value = "{ '?0' : ?1 }", fields = "{ '?2' : 1 }")
    List<User> findWithProjection(String fieldName, String value, String projectionField);
    @Query(value = "{ '?0' : ?1 }", fields = "{ '?2' : 1 }")
    List<User> findWithProjectionAndSorting(String fieldName, String value, String projectionField, String sortField, boolean isAscending);
    //FIXME: has to be replaced cuz of the projection findByEmail()
    User findByEmail(String email);
    User findByUsername(String username);
    User save(User entity);
    void deleteByUsername(String username);
    UserWithoutPwd findUserByEmail(String email);
}
