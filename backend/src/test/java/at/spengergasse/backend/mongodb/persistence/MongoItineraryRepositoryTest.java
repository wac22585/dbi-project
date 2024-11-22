package at.spengergasse.backend.mongodb.persistence;

import at.spengergasse.backend.mongodb.model.Itinerary;
import at.spengergasse.backend.mongodb.model.ItineraryStep;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MongoItineraryRepositoryTest {

    @Autowired
    private MongoItineraryRepository itineraryRepository;

    private Itinerary itinerary;

    @BeforeEach
    void setUp() {
        itinerary = Itinerary.builder()
                .uuid(UUID.randomUUID())
                .name("European Adventure")
                .startDate(LocalDateTime.of(2024, 11, 1, 10, 0))
                .endDate(LocalDateTime.of(2024, 11, 15, 18, 0))
                .itinerarySteps(List.of(ItineraryStep.builder()
                                .name("Step Nr. 1")
                                .stepDate(LocalDateTime.of(2024, 11, 1, 10, 0))
                        .build()))
                .build();
    }

    @AfterEach
    void tearDown() {
        itineraryRepository.deleteAll();  // Clear database after each test
    }

    @Test
    void testSaveItinerary() {
        Itinerary savedItinerary = itineraryRepository.save(itinerary);
        assertNotNull(savedItinerary.getId());
        assertEquals("European Adventure", savedItinerary.getName());
        assertEquals(LocalDateTime.of(2024, 11, 1, 10, 0),
                savedItinerary.getItinerarySteps().getFirst().getStepDate());
    }

    @Test
    void testFindItineraryById() {
        Itinerary savedItinerary = itineraryRepository.save(itinerary);
        Optional<Itinerary> foundItinerary = itineraryRepository.findById(savedItinerary.getId());
        assertTrue(foundItinerary.isPresent());
        assertEquals("European Adventure", foundItinerary.get().getName());
    }

    @Test
    void testFindAllItineraries() {
        itineraryRepository.save(itinerary);
        itineraryRepository.save(Itinerary.builder()
                .uuid(UUID.randomUUID())
                .name("Asian Adventure")
                .startDate(LocalDateTime.of(2024, 12, 1, 10, 0))
                .endDate(LocalDateTime.of(2024, 12, 20, 18, 0))
                .build());
        List<Itinerary> itineraries = itineraryRepository.findAll();
        assertThat(itineraries.size()).isGreaterThan(1);
    }

    @Test
    void testDeleteItinerary() {
        Itinerary savedItinerary = itineraryRepository.save(itinerary);
        itineraryRepository.deleteById(savedItinerary.getId());
        Optional<Itinerary> foundItinerary = itineraryRepository.findById(savedItinerary.getId());
        assertFalse(foundItinerary.isPresent());
    }

    @Test
    void testUpdateItinerary() {
        Itinerary savedItinerary = itineraryRepository.save(itinerary);
        savedItinerary.setName("Updated European Adventure");
        Itinerary updatedItinerary = itineraryRepository.save(savedItinerary);

        assertEquals("Updated European Adventure", updatedItinerary.getName());
    }
}
