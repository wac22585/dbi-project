package at.spengergasse.backend.relational.service;

import at.spengergasse.backend.relational.dto.UserDto;
import at.spengergasse.backend.relational.model.Itinerary;
import at.spengergasse.backend.relational.model.User;
import at.spengergasse.backend.relational.persistence.JpaItineraryRepository;
import at.spengergasse.backend.relational.persistence.JpaUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JpaUserService
{
    private final JpaUserRepository userRepository;
    private final JpaItineraryRepository itineraryRepository;

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

    public Optional<UserDto> findUserByUserName(String username)
    {
        if (username == null) return Optional.empty();

        try {
            return Optional.of(UserDto.fromEntity(userRepository.findByUsername(username)));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserDto> updateUserInformation(UserDto userDto)
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
            User update = userRepository.findByUsername(user.getUsername());
            if (update == null) return Optional.empty();

            update.setEmail(user.getEmail());
            update.setUsername(user.getUsername());
            update.setPassword(user.getPassword());

            if (update.getItineraries() != user.getItineraries())
            {
                List<Itinerary> deletes = new ArrayList<>();
                if (update.getItineraries() != null)
                {
                    update.getItineraries().forEach(itinerary -> {
                        itinerary.deleteUser();
                        deletes.add(itinerary);
                        itineraryRepository.deleteByUuid(itinerary.getUuid());
                    });
                }

                deletes.forEach(update::removeItinerary);

                if(user.getItineraries() != null)
                {
                    user.getItineraries().forEach(update::addItinerary);
                }
            }

            userRepository.save(update);
            return Optional.of(UserDto.fromEntity(update));

        } catch(Exception e) {
            return Optional.empty();
        }
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

    public boolean deleteUser(UserDto userDto)
    {
        if (userDto == null) return false;

        try {
            User user = UserDto.toEntity(userDto);
            user.getItineraries().forEach(Itinerary::deleteUser);
            userRepository.deleteByUsername(user.getUsername());
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

//    public List<Object> findWithProjectionAndSorting(String fieldName, String value, String projectionField, String sortField, String sortDirection) {
//        return userRepository.findAll((Specification<User>) (root, query, criteriaBuilder) -> {
//            query.select(root.get(projectionField));
//            Predicate predicate = criteriaBuilder.equal(root.get(fieldName), value);
//            query.where(predicate);
//            if ("asc".equalsIgnoreCase(sortDirection)) {
//                query.orderBy(criteriaBuilder.asc(root.get(sortField)));
//            } else {
//                query.orderBy(criteriaBuilder.desc(root.get(sortField)));
//            }
//            return query.getRestriction();
//        });
//    }
}
