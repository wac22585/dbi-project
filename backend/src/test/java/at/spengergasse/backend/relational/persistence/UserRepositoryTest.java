package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.Itinerary;
import at.spengergasse.backend.relational.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
class UserRepositoryTest
{
    @Autowired
    private JpaUserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp()
    {
        this.user = User.builder()
                .username("wac22585")
                .email("wac22585@spengergasse.at")
                .password("password")
                .itineraries(List.of(
                        Itinerary.builder()
                                .uuid(UUID.randomUUID())
                                .name("Itinerary 1")
                                .startDate(LocalDateTime.now())
                                .endDate(null)
                                .build()
                ))
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

    @Test
    void verifyDelete()
    {
        User user = this.user;

        user = userRepository.save(user);

        assertThat(userRepository.findById(user.getId())).isNotNull();

        userRepository.deleteById(user.getId());

        assertThat(userRepository.findById(user.getId())).isNull();
    }

    @Test
    void verifyFindByUsername()
    {
        User user = this.user;
        user = userRepository.save(user);

        assertThat(userRepository.findByUsername(user.getUsername())).isNotNull();
        assertThat(user.getUsername()).isEqualTo(userRepository.findByUsername(user.getUsername()).getUsername());
    }

    @Test
    void verifyPersistItinerary()
    {
        User user = this.user;
        user = userRepository.save(user);

        assertThat(userRepository.findById(user.getId()).getItineraries().size()).isEqualTo(1);
    }
}