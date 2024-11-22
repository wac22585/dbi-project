package at.spengergasse.backend.mongodb.persistence;

import at.spengergasse.backend.mongodb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MongoUserRepository extends MongoRepository<User, String>
{
    User findByEmail(String email);
    User findByUsername(String username);
    void deleteByUsername(String username);
    UserWithoutPwd findUserByEmail(String email);
    @Query(
            value = "{ 'itineraries.name': ?0 }", // Filter: Match itinerary name
            fields = "{ 'username': 1, 'email': 1, 'itineraries.name': 1, '_id': 0 }" // Projection
    )
    List<UserWithoutPwd> findUsersByItinerariesSizeGreaterThanOrderByItinerariesAsc(@Param("size") int size);
}
