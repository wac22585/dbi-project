package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface JpaUserRepository extends JpaRepository<User, Long>
{
    User findByUsername(String username);
    void deleteByUsername(String username);
    UserWithoutPwd findUserByEmail(String email);
    @Query("SELECT u FROM User u WHERE SIZE(u.itineraries) > :size ORDER BY SIZE(u.itineraries) ASC")
    List<UserWithoutPwd> findUsersByItinerariesSizeGreaterThanOrderByItinerariesAsc(@Param("size") int size);
}
