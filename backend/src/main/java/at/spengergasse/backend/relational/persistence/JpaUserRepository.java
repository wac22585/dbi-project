package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.User;
import org.springframework.data.repository.Repository;

public interface JpaUserRepository extends Repository<User, Long>
{
    User save(User user);
    User findById(Long id);
    User findByUsername(String username);
    void deleteById(Long id);
    void deleteByUsername(String username);
    void deleteAll();
}
