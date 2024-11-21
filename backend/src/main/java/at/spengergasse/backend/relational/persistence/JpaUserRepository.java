package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaUserRepository extends JpaRepository<User, Long>
{
    User findByUsername(String username);
    void deleteByUsername(String username);
    UserWithoutPwd findUserByEmail(String email);
    UserWithItineraryNames findUserByUsername(String username);
}
