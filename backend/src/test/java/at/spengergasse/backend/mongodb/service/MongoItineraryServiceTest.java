package at.spengergasse.backend.mongodb.service;

import at.spengergasse.backend.mongodb.dto.ItineraryDto;
import at.spengergasse.backend.mongodb.dto.ItineraryStepDto;
import at.spengergasse.backend.mongodb.model.Itinerary;
import at.spengergasse.backend.mongodb.model.ItineraryStep;
import at.spengergasse.backend.mongodb.persistence.MongoItineraryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MongoItineraryServiceTest {

    @Autowired
    private MongoItineraryService itineraryService;

    @MockBean
    private MongoItineraryRepository itineraryRepository;

    private ItineraryDto itineraryDto;
    private Itinerary itinerary;

    @BeforeEach
    void setup() {
        itineraryDto = new ItineraryDto(
                UUID.randomUUID(),
                "Test Itinerary",
                LocalDateTime.now(),
                null,
                List.of(new ItineraryStepDto("Step Nr.1", LocalDateTime.now(), null))
        );

        itinerary = ItineraryDto.toEntity(itineraryDto);
    }

    @AfterEach
    void tearDown() {
        itineraryRepository.deleteAll();  // Clear database after each test
    }

    @Test
    void testSaveItinerary() {
        when(itineraryRepository.save(any(Itinerary.class))).thenReturn(itinerary);

        Optional<ItineraryDto> result = itineraryService.saveItinerary(itineraryDto);

        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("Test Itinerary");
        assertThat(result.get().itinerarySteps()).hasSize(1);
        verify(itineraryRepository, times(1)).save(any(Itinerary.class));
    }

    @Test
    void testFindItineraryByUUID() {
        when(itineraryRepository.findByUuid(any(UUID.class))).thenReturn(itinerary);

        Optional<ItineraryDto> result = itineraryService.findItineraryByUUID(itineraryDto.uuid());

        assertThat(result).isPresent();
        assertThat(result.get().uuid()).isEqualTo(itineraryDto.uuid());
        assertThat(result.get().itinerarySteps()).hasSize(1);
        assertThat(result.get().itinerarySteps().get(0).name()).isEqualTo("Step Nr.1");
        verify(itineraryRepository, times(1)).findByUuid(itineraryDto.uuid());
    }

    @Test
    void testFindAllItineraries() {
        Itinerary itinerary2 = new Itinerary(
                UUID.randomUUID(),
                "Test Itinerary 2",
                LocalDateTime.now(),
                null,
                List.of(new ItineraryStep("Step Nr.2", LocalDateTime.now(), null))
        );

        when(itineraryRepository.findAll()).thenReturn(List.of(itinerary, itinerary2));

        List<ItineraryDto> result = itineraryService.findAllItineraries();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Test Itinerary");
        assertThat(result.get(1).name()).isEqualTo("Test Itinerary 2");
        verify(itineraryRepository, times(1)).findAll();
    }

    @Test
    void testDeleteItineraryByUUID() {
        when(itineraryRepository.findByUuid(any(UUID.class))).thenReturn(itinerary);
        doNothing().when(itineraryRepository).deleteByUuid(any(UUID.class));

        boolean result = itineraryService.deleteItineraryByUUID(itineraryDto.uuid());

        assertThat(result).isTrue();
        verify(itineraryRepository, times(1)).deleteByUuid(itineraryDto.uuid());
    }

    @Test
    void testDeleteItineraryByUUID_NotFound() {
        when(itineraryRepository.findByUuid(any(UUID.class))).thenReturn(null);

        boolean result = itineraryService.deleteItineraryByUUID(UUID.randomUUID());

        assertThat(result).isFalse();
        verify(itineraryRepository, never()).deleteByUuid(any(UUID.class));
    }
}
