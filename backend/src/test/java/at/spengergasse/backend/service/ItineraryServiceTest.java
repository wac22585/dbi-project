package at.spengergasse.backend.service;

import at.spengergasse.backend.dto.ItineraryDto;
import at.spengergasse.backend.dto.ItineraryStepDto;
import at.spengergasse.backend.model.Itinerary;
import at.spengergasse.backend.persistence.ItineraryRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class ItineraryServiceTest
{
    @Autowired
    private ItineraryService itineraryService;
    @MockBean
    private ItineraryRepository itineraryRepository;

    private ItineraryDto itineraryDto;
    private Itinerary itinerary;

    @BeforeEach
    void setup()
    {
        itineraryDto = new ItineraryDto(
                UUID.randomUUID(),
            "10 day Scandinavia Itinerary",
            LocalDateTime.of(2022, 12, 10, 5, 30),
            LocalDateTime.of(2022, 12, 20, 20, 30),
            List.of(new ItineraryStepDto("Day 1 - Visit the Viking Ship Museum", LocalDateTime.of(2022, 12, 10, 5, 30), List.of()),
                new ItineraryStepDto("Day 2 - Explore the Old Town", LocalDateTime.of(2022, 12, 11, 5, 30), List.of()),
                new ItineraryStepDto("Day 3 - Visit the Little Mermaid", LocalDateTime.of(2022, 12, 12, 5, 30), List.of()),
                new ItineraryStepDto("Day 4 - Visit the Old Town", LocalDateTime.of(2022, 12, 13, 5, 30), List.of()),
                new ItineraryStepDto("Day 5 - Visit the Old Town", LocalDateTime.of(2022, 12, 14, 5, 30), List.of()),
                new ItineraryStepDto("Day 6 - Visit the Old Town",  LocalDateTime.of(2022, 12, 15, 5, 30), List.of()),
                new ItineraryStepDto("Day 7 - Visit the Old Town", LocalDateTime.of(2022, 12, 16, 5, 30), List.of()),
                new ItineraryStepDto("Day 8 Visit the Old Town", LocalDateTime.of(2022, 12, 17, 5, 30), List.of()),
                new ItineraryStepDto("Day 9 - Visit the Old Town", LocalDateTime.of(2022, 12, 18, 5, 30), List.of()),
                new ItineraryStepDto("Day 10 - Visit the Old Town", LocalDateTime.of(2022, 12, 19, 5, 30), List.of())
            ));
        itinerary = ItineraryDto.toEntity(itineraryDto);
    }

    @Test
    void verifySaveItinerary()
    {
        when(itineraryRepository.save(any(Itinerary.class))).thenReturn(itinerary);

        var savedItinerary = itineraryService.saveItinerary(itineraryDto);

        assertThat(savedItinerary).isPresent();
        assertEquals(itineraryDto.name(), savedItinerary.get().name());
        assertEquals(itineraryDto.startDate(), savedItinerary.get().startDate());
        assertEquals(itineraryDto.endDate(), savedItinerary.get().endDate());
        assertEquals(itineraryDto.itinerarySteps().size(), savedItinerary.get().itinerarySteps().size());
    }

    @Test
    void verifyFindItineraryById()
    {
        when(itineraryRepository.findById(any(Long.class))).thenReturn(itinerary);

        var foundItinerary = itineraryService.findItineraryById(1L);

        assertThat(foundItinerary).isPresent();
        assertEquals(itineraryDto.name(), foundItinerary.get().name());
        assertEquals(itineraryDto.startDate(), foundItinerary.get().startDate());
        assertEquals(itineraryDto.endDate(), foundItinerary.get().endDate());
        assertEquals(itineraryDto.itinerarySteps().size(), foundItinerary.get().itinerarySteps().size());
    }


//    @Test
//    void verifyFindItineraryByUuid()
//    {
//        when(itineraryRepository.findByUuid(any(UUID.class))).thenReturn(itinerary);
//
//        var foundItinerary = itineraryService.findItineraryByUuid(UUID.randomUUID());
//
//        assertThat(foundItinerary).isPresent();
//        assertEquals(itineraryDto.name(), foundItinerary.get().name());
//        assertEquals(itineraryDto.startDate(), foundItinerary.get().startDate());
//        assertEquals(itineraryDto.endDate(), foundItinerary.get().endDate());
//        assertEquals(itineraryDto.itinerarySteps().size(), foundItinerary.get().itinerarySteps().size());
//    }



//    @Test
//    void verifyUpdateItinerary()
//    {
//        when(itineraryRepository.findById(any(Long.class))).thenReturn(itinerary);
//
//        var updatedItinerary = itineraryService.updateItinerary(itineraryDto);
//
//        assertThat(updatedItinerary).isPresent();
//        assertEquals(itineraryDto.name(), updatedItinerary.get().name());
//        assertEquals(itineraryDto.startDate(), updatedItinerary.get().startDate());
//        assertEquals(itineraryDto.endDate(), updatedItinerary.get().endDate());
//        assertEquals(itineraryDto.itinerarySteps().size(), updatedItinerary.get().itinerarySteps().size());
//    }

//    List.of(new ItineraryDto("Day 1", LocalDateTime.of(2022, 12, 10, 5, 30), null, null),
//        new ItineraryDto("Day 2", LocalDateTime.of(2022, 12, 11, 5, 30), null, null),
//        new ItineraryDto("Day 3", LocalDateTime.of(2022, 12, 12, 5, 30), null, null),
//        new ItineraryDto("Day 4", LocalDateTime.of(2022, 12, 13, 5, 30), null, null),
//        new ItineraryDto("Day 5", LocalDateTime.of(2022, 12, 14, 5, 30), null, null),
//        new ItineraryDto("Day 6", LocalDateTime.of(2022, 12, 15, 5, 30), null, null),
//        new ItineraryDto("Day 7", LocalDateTime.of(2022, 12, 16, 5, 30), null, null),
//        new ItineraryDto("Day 8", LocalDateTime.of(2022, 12, 17, 5, 30), null, null),
//        new ItineraryDto("Day 9", LocalDateTime.of(2022, 12, 18, 5, 30), null, null),
//        new ItineraryDto("Day 10", LocalDateTime.of(2022, 12, 19, 5, 30), null, null)
//        )
}