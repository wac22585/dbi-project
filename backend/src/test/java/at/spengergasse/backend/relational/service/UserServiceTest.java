package at.spengergasse.backend.relational.service;

import at.spengergasse.backend.relational.dto.ItineraryDto;
import at.spengergasse.backend.relational.dto.UserDto;
import at.spengergasse.backend.relational.model.User;
import at.spengergasse.backend.relational.persistence.JpaUserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class UserServiceTest
{
    @Autowired
    private UserService userService;
    @MockBean
    private JpaUserRepository userRepository;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp()
    {
        userDto = new UserDto(
        "wac22585",
           "wac22585@spengergasse.at",
        "password",
                List.of(new ItineraryDto(UUID.randomUUID(), "Test", LocalDateTime.now(), null, null)
                ));
        user = UserDto.toEntity(userDto);
    }

    @Test
    void verifySaveUser()
    {
        when(userRepository.save(any(User.class))).thenReturn(user);

        var savedUser = userService.saveUser(userDto);

        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().email()).isEqualTo(user.getEmail());
        assertThat(savedUser.get().itineraries().getFirst().name())
                .isEqualTo(user.getItineraries().getFirst().getName());
    }

    @Test
    void verifyFindUserByUserName()
    {
        when(userRepository.findByUsername("wac22585")).thenReturn(user);

        var foundUser = userService.findUserByUserName("wac22585");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().email()).isEqualTo(user.getEmail());
    }

    @Test
    void verifyUpdateUser()
    {
        UserDto updatedUser = new UserDto("wac22585", "wac22585@spengergasse.at", "newpass", List.of());
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(UserDto.toEntity(updatedUser));

        var updated = userService.updateUserInformation(updatedUser);

        assertThat(updated).isPresent();
        assertThat(updated.get().username()).isEqualTo(updatedUser.username());
        assertThat(updated.get().email()).isEqualTo(userDto.email());
    }

    @Test
    void verifyUpdateUserItineraryList()
    {
        UserDto updatedUser = new UserDto("wac22585", "wac22585@spengergasse.at", "newpass",
                List.of(new ItineraryDto(UUID.randomUUID(), "Test2", LocalDateTime.now(), null, null)));
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(UserDto.toEntity(updatedUser));

        var updated = userService.updateUserInformation(updatedUser);

        assertThat(updated).isPresent();
        assertThat(updated.get().itineraries().getFirst().name())
                .isEqualTo(user.getItineraries().getFirst().getName());
    }

    @Test
    void verifyUpdateUserName()
    {
        UserDto updatedUser = new UserDto("abc123", "wac22585@spengergasse.at", "password", List.of());
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(UserDto.toEntity(updatedUser));

        var updated = userService.updateUserName("wac22585", "abc123");

        assertThat(updated).isPresent();
        assertThat(updated.get().username()).isEqualTo(updatedUser.username());
        assertThat(updated.get().email()).isEqualTo(userDto.email());
    }

    @Test
    void verifyDeleteUser()
    {
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);

        var deleted = userService.deleteUser(userDto);

        assertThat(deleted).isTrue();
    }
}