package at.spengergasse.backend.mongodb.persistenceRef;



import at.spengergasse.backend.mongodb.modelRef.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MongoUserRefRepositoryTest
{
    @Autowired
    private MongoUserRefRepository userRefRepository;
    @Autowired
    private MongoCityRefRepository cityRepository;
    @Autowired
    private MongoRouteStopRefRepository routeStopRepository;
    @Autowired
    private MongoItineraryStepRefRepository itineraryStepRepository;
    @Autowired
    private MongoItineraryRefRepository itineraryRepository;
    private User user;

    @BeforeEach
    void setup() {
        // Persist cities
        City vienna = City.builder()
                .city("Vienna")
                .country("Austria")
                .build();
        City bratislava = City.builder()
                .city("Bratislava")
                .country("Slovakia")
                .build();
        City savedVienna = cityRepository.save(vienna);
        City savedBratislava = cityRepository.save(bratislava);

        // Persist route stops
        RouteStop routeStop1 = RouteStop.builder()
                .totalDistance(500f)
                .arrivalTime(LocalDateTime.of(2024, 11, 1, 10, 0))
                .currentCity(savedVienna)
                .nextCity(savedBratislava)
                .build();
        RouteStop savedRouteStop1 = routeStopRepository.save(routeStop1);

        // Persist itinerary steps
        ItineraryStep step1 = ItineraryStep.builder()
                .name("Step Nr. 1")
                .stepDate(LocalDateTime.of(2024, 11, 1, 10, 0))
                .routeStops(List.of(savedRouteStop1))
                .build();
        ItineraryStep savedStep1 = itineraryStepRepository.save(step1);

        // Persist itineraries
        Itinerary itinerary = Itinerary.builder()
                .uuid(UUID.randomUUID())
                .name("European Adventure")
                .startDate(LocalDateTime.of(2024, 11, 1, 10, 0))
                .endDate(LocalDateTime.of(2024, 11, 15, 18, 0))
                .itinerarySteps(List.of(savedStep1))
                .build();
        Itinerary savedItinerary = itineraryRepository.save(itinerary);

        // Create and save user
        user = User.builder()
                .username("johndoe")
                .email("johndoe@example.com")
                .password("password123")
                .itineraries(List.of(savedItinerary))
                .build();
    }

    @Test
    void verifySave()
    {
        User saved = userRefRepository.save(this.user);
        assertThat(saved).isNotNull();
        assertThat(saved.getItineraries().size()).isNotZero();
    }

}