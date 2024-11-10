package at.spengergasse.backend.service;

import at.spengergasse.backend.dto.UserDto;
import at.spengergasse.backend.model.User;
import at.spengergasse.backend.persistence.ItineraryRepository;
import at.spengergasse.backend.persistence.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserService
{
    private final UserRepository userRepository;
    private final ItineraryRepository itineraryRepository;

    public Optional<UserDto> saveUser(UserDto user)
    {
        if (user == null) return Optional.empty();

        try {
            User save = UserDto.toEntity(user);
            return Optional.of(UserDto.fromEntity(userRepository.save(save)));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserDto> findUserById(Long id)
    {
        if (id == null) return Optional.empty();

        try {
            return Optional.of(UserDto.fromEntity(userRepository.findById(id)));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserDto> findUserByUserName(String username)
    {
        if (username == null) return Optional.empty();

        try {
            return Optional.of(UserDto.fromEntity(userRepository.findByUsername(username)));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserDto> updateUser(UserDto userDto)
    {
        //check for invalid properties
        if (userDto == null) return Optional.empty();

        User user = UserDto.toEntity(userDto);

        if (user.getUsername().isEmpty()
                || user.getPassword().isEmpty()
                || user.getEmail().isEmpty()) return Optional.empty();

        if (user.getUsername().isBlank()
                || user.getPassword().isBlank()
                || user.getEmail().isBlank()) return Optional.empty();

        try {
            User update = userRepository.findById(user.getId());
            if (update == null) return Optional.empty();

            update.setEmail(user.getEmail());
            update.setUsername(user.getUsername());
            update.setPassword(user.getPassword());

            if (update.getItineraries() != user.getItineraries())
            {
                update.getItineraries().forEach(itinerary -> {
                    itinerary.setUser(null);
                    itineraryRepository.deleteById(itinerary.getId());
                });
                user.getItineraries().forEach(update::addItinerary);
            }

            userRepository.save(update);
            return Optional.of(UserDto.fromEntity(update));

        } catch(Exception e) {
            return Optional.empty(); //no update possible
        }
    }

    public boolean deleteUser(User user)
    {
        if (user == null || user.getId() == null) return false;

        try {
            user.getItineraries().forEach(itinerary -> {
                itinerary.setUser(null);
            });
            userRepository.deleteById(user.getId());
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
