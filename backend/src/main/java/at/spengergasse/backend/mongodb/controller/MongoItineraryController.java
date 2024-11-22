package at.spengergasse.backend.mongodb.controller;

import at.spengergasse.backend.mongodb.dto.ItineraryDto;
import at.spengergasse.backend.mongodb.model.City;
import at.spengergasse.backend.mongodb.model.Itinerary;
import at.spengergasse.backend.mongodb.model.ItineraryStep;
import at.spengergasse.backend.mongodb.model.RouteStop;
import at.spengergasse.backend.mongodb.persistence.MongoCityRepository;
import at.spengergasse.backend.mongodb.persistence.MongoItineraryRepository;
import at.spengergasse.backend.mongodb.service.MongoItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping(path = MongoItineraryController.ITINERARY_PATH)
@CrossOrigin(origins = "http://localhost:3001")
public class MongoItineraryController
{
    public static final String ITINERARY_PATH = "api/mongodb/itinerary";
    private final MongoItineraryService itineraryService;
    private final MongoCityRepository cityRepository;
    private final MongoItineraryRepository itineraryRepository;

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<ItineraryDto> addItinerary(@RequestBody ItineraryDto itineraryDto) {
        return itineraryService.saveItinerary(itineraryDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @CrossOrigin(origins = "http://localhost:3001")
    @GetMapping(value = "/{uuid}", produces = "application/json")
    public ResponseEntity<ItineraryDto> fetchItineraryByUuid(@PathVariable UUID uuid) {
        return itineraryService.findItineraryByUUID(uuid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/update", produces = "application/json")
    public ResponseEntity<ItineraryDto> updateItinerary(@RequestBody ItineraryDto itineraryDto) {
        return itineraryService.updateItinerary(itineraryDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<ItineraryDto>> fetchAllItineraries() {
        List<ItineraryDto> itineraries = itineraryService.findAllItineraries();
        if (itineraries.isEmpty()) {
            seedDatabase();
        }
        return ResponseEntity.ok(itineraryService.findAllItineraries());
    }

    @DeleteMapping(value = "/delete/{uuid}", produces = "application/json")
    public ResponseEntity<ItineraryDto> deleteItineraryByUuid(@PathVariable UUID uuid) {
        boolean deleted = itineraryService.deleteItineraryByUUID(uuid);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value = "/delete", produces = "application/json")
    public ResponseEntity<ItineraryDto> deleteItinerary(@RequestBody ItineraryDto itineraryDto) {
        Itinerary it = ItineraryDto.toEntity(itineraryDto);
        boolean deleted = itineraryService.deleteItinerary(it);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


    private void seedDatabase()
    {
        City city1 = cityRepository.save(City.builder()
                .city("Stockholm")
                .country("Sweden")
                .build());

        itineraryRepository.save(Itinerary.builder()
                .uuid(UUID.randomUUID())
                .name("10 day Scandinavia Itinerary")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .itinerarySteps(List.of(
                        ItineraryStep.builder()
                                .name("Day 1 - Visit the Viking Ship Museum")
                                .stepDate(LocalDateTime.now())
                                .routeStops(List.of(
                                        RouteStop.builder()
                                                .currentCity(City.builder()
                                                        .city("Stockholm")
                                                        .country("Sweden")
                                                        .build())
                                                .nextCity(City.builder()
                                                        .city("Copenhagen")
                                                        .country("Denmark")
                                                        .build())
                                                .build()
                                ))
                                .build(),
                        ItineraryStep.builder()
                                .name("Day 2 - Explore the Old Town")
                                .stepDate(LocalDateTime.now().plusDays(3))
                                .routeStops(List.of(
                                        RouteStop.builder()
                                                .currentCity(City.builder()
                                                        .city("Copenhagen")
                                                        .country("Denmark")
                                                        .build())
                                                .nextCity(City.builder()
                                                        .city("Oslo")
                                                        .country("Norway")
                                                        .build())
                                                .build()
                                ))
                                .build()
                ))
                .build());
    }
}
