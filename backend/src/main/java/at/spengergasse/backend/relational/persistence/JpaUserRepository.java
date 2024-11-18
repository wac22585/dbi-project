package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.User;
import at.spengergasse.backend.relational.service.JpaUserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface JpaUserRepository extends JpaRepository<User, Long>
{
    User findByUsername(String username);
    void deleteByUsername(String username);
}
