package at.spengergasse.backend.mongodb.persistence;

import at.spengergasse.backend.mongodb.model.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MongoUserRepositoryTest {

    @Autowired
    private MongoUserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        // Initialize a sample User object for testing
        user = User.builder()
                .username("johndoe")
                .email("johndoe@example.com")
                .password("password123")
                .itineraries(List.of(
                        Itinerary.builder()
                                .uuid(UUID.randomUUID())
                                .name("European Adventure")
                                .startDate(LocalDateTime.of(2024, 11, 1, 10, 0))
                                .endDate(LocalDateTime.of(2024, 11, 15, 18, 0))
                                .itinerarySteps(List.of(ItineraryStep.builder()
                                        .name("Step Nr. 1")
                                        .stepDate(LocalDateTime.of(2024, 11, 1, 10, 0))
                                        .routeStops(List.of(
                                                RouteStop.builder()
                                                        .totalDistance(500f)
                                                        .arrivalTime(LocalDateTime.of(2024, 11, 1, 10, 0))
                                                        .currentCity(City.builder()
                                                                .city("Vienna")
                                                                .country("Austria")
                                                                .build())
                                                        .nextCity(City.builder()
                                                                .city("Bratislava")
                                                                .country("Slovakia")
                                                                .build())
                                                        .build()))
                                        .build(),
                                        ItineraryStep.builder()
                                                .name("Step Nr. 2")
                                                .stepDate(LocalDateTime.of(2024, 11, 2, 10, 0))
                                                .routeStops(List.of(
                                                        RouteStop.builder()
                                                                .totalDistance(1000f)
                                                                .arrivalTime(LocalDateTime.of(2024, 11, 2, 10, 0))
                                                                .currentCity(City.builder()
                                                                        .city("Bratislava")
                                                                        .country("Slovakia")
                                                                        .build())
                                                                .nextCity(City.builder()
                                                                        .city("Budapest")
                                                                        .country("Hungary")
                                                                        .build())
                                                                .build()))
                                                .build()))
                                .build()))
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();  // Clear database after each test
    }

    @Test
    void testSaveUser() {
        // Test saving a new user
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
        assertEquals("johndoe", savedUser.getUsername());
    }

    @Test
    void testFindUserById() {
        // Test finding a user by ID
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("johndoe", foundUser.get().getUsername());
    }

    @Test
    void testFindAllUsers() {
        // Test finding all users
        userRepository.save(user);
        userRepository.save(User.builder()
                .username("janedoe")
                .email("janedoe@example.com")
                .password("password456")
                .build());

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    void testDeleteUserById() {
        // Test deleting a user by ID
        User savedUser = userRepository.save(user);
        userRepository.deleteById(savedUser.getId());

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testUpdateUser() {
        // Test updating an existing user
        User savedUser = userRepository.save(user);
        savedUser.setUsername("johnupdated");
        User updatedUser = userRepository.save(savedUser);

        assertEquals("johnupdated", updatedUser.getUsername());
    }
}
