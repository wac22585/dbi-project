package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.User;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, Long>
{
    User save(User user);
    User findById(Long id);
    User findByUsername(String username);
    void deleteById(Long id);

    void deleteAll();
}
