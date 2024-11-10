package at.spengergasse.backend.service;

import at.spengergasse.backend.dto.ItineraryDto;
import at.spengergasse.backend.dto.ItineraryStepDto;
import at.spengergasse.backend.model.Itinerary;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ItineraryServiceTest
{
    @Autowired
    private ItineraryService itineraryService;
    @MockBean
    private ItineraryService itineraryRepository;

    private ItineraryDto itineraryDto;
    private Itinerary itinerary;

    @BeforeEach
    void setup()
    {
        itineraryDto = new ItineraryDto(
            "10 day Scandinavia Itinerary",
            LocalDateTime.of(2022, 12, 10, 5, 30),
            LocalDateTime.of(2022, 12, 20, 20, 30),
            List.of(new ItineraryStepDto("Day 1 - Visit the Viking Ship Museum", "Oslo, Norway"),
                new ItineraryStepDto("Day 2 - Explore the Old Town", "Stockholm, Sweden"),
                new ItineraryStepDto("Day 3 - Visit the Little Mermaid", "Copenhagen, Denmark"),
                new ItineraryStepDto("Day 4 - Visit the Old Town", "Tallinn, Estonia"),
                new ItineraryStepDto("Day 5", "Visit the Old Town", "Riga, Latvia"),
                new ItineraryStepDto("Day 6", "Visit the Old Town", "Vilnius, Lithuania"),
                new ItineraryStepDto("Day 7", "Visit the Old Town", "Warsaw, Poland"),
                new ItineraryStepDto("Day 8", "Visit the Old Town", "Krakow, Poland"),
                new ItineraryStepDto("Day 9", "Visit the Old Town", "Prague, Czech Republic"),
                new ItineraryStepDto("Day 10", "Visit the Old Town", "Vienna, Austria")
            )
        );
        itinerary = ItineraryDto.toEntity(itineraryDto);

    }

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