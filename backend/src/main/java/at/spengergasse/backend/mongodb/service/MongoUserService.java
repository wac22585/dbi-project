package at.spengergasse.backend.mongodb.service;

import at.spengergasse.backend.mongodb.persistence.MongoUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MongoUserService
{
    private final MongoUserRepository userRepository;
}
