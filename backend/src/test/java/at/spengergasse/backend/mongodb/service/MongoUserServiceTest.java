package at.spengergasse.backend.mongodb.service;

import at.spengergasse.backend.mongodb.dto.ItineraryDto;
import at.spengergasse.backend.mongodb.dto.ItineraryStepDto;
import at.spengergasse.backend.mongodb.dto.UserDto;
import at.spengergasse.backend.mongodb.model.User;
import at.spengergasse.backend.mongodb.persistence.MongoUserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class MongoUserServiceTest {

    @Autowired
    private MongoUserService userService;

    @MockBean
    private MongoUserRepository userRepository;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setup() {
        userDto = new UserDto(
                "wac22585",
                "wac22585@spengergasse.at",
                "password",
                List.of(new ItineraryDto(
                        UUID.randomUUID(),
                        "Test Itinerary",
                        LocalDateTime.now(),
                        null,
                        List.of(new ItineraryStepDto("Step Nr.1", LocalDateTime.now(), null))
                ))
        );

        user = UserDto.toEntity(userDto);
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        Optional<UserDto> result = userService.saveUser(userDto);

        assertThat(result).isPresent();
        assertThat(result.get().username()).isEqualTo("wac22585");
        assertThat(result.get().email()).isEqualTo("wac22585@spengergasse.at");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindUserByUsername() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);

        Optional<UserDto> result = userService.findUserByUsername("wac22585");

        assertThat(result).isPresent();
        assertThat(result.get().username()).isEqualTo("wac22585");
        assertThat(result.get().email()).isEqualTo("wac22585@spengergasse.at");
        verify(userRepository, times(1)).findByUsername("wac22585");
    }

    @Test
    void testFindUserByEmail() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(user);

        Optional<UserDto> result = userService.findUserByEmail("wac22585@spengergasse.at");

        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("wac22585@spengergasse.at");
        assertThat(result.get().username()).isEqualTo("wac22585");
        verify(userRepository, times(1)).findByEmail("wac22585@spengergasse.at");
    }

    @Test
    void testFindAllUsers() {
        User anotherUser = new User(
                "testuser",
                "testuser@example.com",
                "password",
                List.of()
        );

        when(userRepository.findAll()).thenReturn(List.of(user, anotherUser));

        List<UserDto> result = userService.findAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).username()).isEqualTo("wac22585");
        assertThat(result.get(1).username()).isEqualTo("testuser");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testDeleteUserByUsername() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        doNothing().when(userRepository).deleteByUsername(any(String.class));

        boolean result = userService.deleteUserByUsername("wac22585");

        assertThat(result).isTrue();
        verify(userRepository, times(1)).deleteByUsername("wac22585");
    }

    @Test
    void testDeleteUserByUsername_NotFound() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);

        boolean result = userService.deleteUserByUsername("nonexistentuser");

        assertThat(result).isFalse();
        verify(userRepository, never()).deleteByUsername(any(String.class));
    }
}
