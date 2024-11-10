package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
class UserRepositoryTest
{
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp()
    {
        this.user = User.builder()
                .username("wac22585")
                .email("wac22585@spengergasse.at")
                .password("password")
                .build();
    }

    @Test
    void verifySave()
    {
        User user = this.user;

        assertThat(user.getId()).isNull();

        user = userRepository.save(user);

        assertThat(user.getId()).isNotNull();
    }
}