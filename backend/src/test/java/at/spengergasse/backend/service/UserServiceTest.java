package at.spengergasse.backend.service;

import at.spengergasse.backend.dto.ItineraryDto;
import at.spengergasse.backend.dto.UserDto;
import at.spengergasse.backend.model.User;
import at.spengergasse.backend.persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class UserServiceTest
{
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp()
    {
        userDto = new UserDto(
        "wac22585",
           "wac22585@spengergasse.at",
        "password",
                List.of(new ItineraryDto("Test", LocalDateTime.now(), null, null)
                ));
        user = UserDto.toEntity(userDto);
    }

    @Test
    void verifySaveUser()
    {
        when(userRepository.save(user)).thenReturn(user);

        var savedUser = userService.saveUser(userDto);

        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().email()).isEqualTo(user.getEmail());
    }

    @Test
    void verifyFindUserById()
    {

    }

    @Test
    void verifyFindUserByUserName()
    {

    }

    @Test
    void verifyUpdateUser()
    {

    }

    @Test
    void verifyDeleteUser()
    {

    }
}