package at.spengergasse.backend.mongodb.service;

import at.spengergasse.backend.mongodb.dto.UserDto;
import at.spengergasse.backend.mongodb.model.User;
import at.spengergasse.backend.mongodb.persistence.MongoUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MongoUserService
{
    private final MongoUserRepository userRepository;

    public Optional<UserDto> saveUser(UserDto user)
    {
        if(user == null) return Optional.empty();

        try {
            userRepository.save(UserDto.toEntity(user));
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserDto> findUserByUsername(String username)
    {
        if(username == null) return Optional.empty();

        try {
            return Optional.of(UserDto.fromEntity(userRepository.findByUsername(username)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserDto> findUserByEmail(String email)
    {
        if(email == null) return Optional.empty();

        try {
            return Optional.of(UserDto.fromEntity(userRepository.findByEmail(email)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<UserDto> findAllUsers()
    {
        return userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .toList();
    }

    public Optional<UserDto> updateUserName(String username, String newUsername)
    {
        if (username == null || newUsername == null) return Optional.empty();

        try {
            User user = userRepository.findByUsername(username);
            if (user == null) return Optional.empty();

            user.setUsername(newUsername);
            userRepository.save(user);
            return Optional.of(UserDto.fromEntity(user));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public boolean deleteUserByUsername(String username)
    {
        if(username == null || userRepository.findByUsername(username) == null) return false;

        try {
            userRepository.deleteByUsername(username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
