package at.spengergasse.backend.mongodb.controller;

import at.spengergasse.backend.mongodb.dto.ItineraryDto;
import at.spengergasse.backend.mongodb.model.Itinerary;
import at.spengergasse.backend.mongodb.service.MongoItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping(path = MongoItineraryController.ITINERARY_PATH)
public class MongoItineraryController
{
    public static final String ITINERARY_PATH = "api/mongodb/itinerary";
    private final MongoItineraryService itineraryService;

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<ItineraryDto> addItinerary(@RequestBody ItineraryDto itineraryDto) {
        return itineraryService.saveItinerary(itineraryDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/{uuid}", produces = "application/json")
    public ResponseEntity<ItineraryDto> fetchItineraryByUuid(@PathVariable UUID uuid) {
        return itineraryService.findItineraryByUUID(uuid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<ItineraryDto>> fetchAllItineraries() {
        return ResponseEntity.ok(itineraryService.findAllItineraries());
    }

    @GetMapping(value = "/delete/{uuid}", produces = "application/json")
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
}
