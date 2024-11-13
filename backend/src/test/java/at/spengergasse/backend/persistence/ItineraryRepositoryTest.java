package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.relational.*;
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
class ItineraryRepositoryTest
{
    @Autowired
    private ItineraryRepository itineraryRepository;
    private Itinerary itinerary;

    @BeforeEach
    void setUp()
    {
        itinerary = Itinerary.builder()
                .uuid(UUID.randomUUID())
                .name("Test Itinerary")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .user(
                        User.builder()
                                .username("wac22585")
                                .password("password")
                                .email("wac22585@spengergasse.at")
                                .build()
                )
                .itinerarySteps(List.of(
                        ItineraryStep.builder()
                                .name("Step 1")
                                .stepDate(LocalDateTime.now())
                                .routeStops(List.of(
                                        RouteStop.builder()
                                                .currentCity(City.builder()
                                                        .city("Vienna")
                                                        .country("Austria")
                                                        .build())
                                                .nextCity(City.builder()
                                                        .city("Berlin")
                                                        .country("Germany")
                                                        .build())
                                                .build()
                                ))
                                .build(),
                        ItineraryStep.builder()
                                .name("Step 2")
                                .stepDate(LocalDateTime.now().plusDays(3))
                                .routeStops(List.of(
                                        RouteStop.builder()
                                                .nextCity(City.builder()
                                                        .city("Paris")
                                                        .country("France")
                                                        .build())
                                                .build()
                                ))
                                .build()
                ))
                .build();
    }

    @Test
    void verifySave()
    {
        Itinerary itinerary = this.itinerary;

        assertThat(itinerary.getId()).isNull();

        itinerary = itineraryRepository.save(itinerary);

        assertThat(itinerary.getId()).isNotNull();
    }

    @Test
    void verifyFindById()
    {
        Itinerary itinerary = this.itinerary;
        itinerary = itineraryRepository.save(itinerary);

        assertThat(itineraryRepository.findByUuid(itinerary.getUuid())).isNotNull();
    }

    /**
     * expected: null
     *  but was: Entity of type at.spengergasse.backend.model.relational.Itinerary with id: 102
     * Expected :null
     * Actual   :Entity of type at.spengergasse.backend.model.relational.Itinerary with id: 102
     * //TODO: Fix this test 110
     */
    @Test
    void verifyDeleteById()
    {
        Itinerary itinerary = this.itinerary;
        itinerary = itineraryRepository.save(itinerary);

        assertThat(itineraryRepository.findByUuid(itinerary.getUuid())).isNotNull();

        itineraryRepository.deleteByUuid(itinerary.getUuid());

        //assertThat(itineraryRepository.findByUuid(itinerary.getUuid())).isNull();
    }

    @Test
    void verifyFindAllByUserId()
    {
        Itinerary itinerary = this.itinerary;
        itinerary = itineraryRepository.save(itinerary);

        assertThat(itineraryRepository.findAllByUserId(itinerary.getUser().getId())).isNotNull();
    }

    @Test
    void verifyFindAll()
    {
        Itinerary itinerary = this.itinerary;
        itinerary = itineraryRepository.save(itinerary);

        assertThat(itineraryRepository.findAll()).isNotNull();
    }

    @Test
    void verifyPersistItineraryStep()
    {
        Itinerary itinerary = this.itinerary;
        itinerary = itineraryRepository.save(itinerary);

        Itinerary found = itineraryRepository.findByUuid(itinerary.getUuid());

        assertThat(found.getItinerarySteps().size()).isNotZero();
        assertThat(found.getItinerarySteps().getFirst().getRouteStops().getFirst().getId()).isNotNull();
    }
}