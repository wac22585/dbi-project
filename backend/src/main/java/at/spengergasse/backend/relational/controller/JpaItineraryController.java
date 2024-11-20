package at.spengergasse.backend.relational.controller;

import at.spengergasse.backend.relational.dto.ItineraryDto;
import at.spengergasse.backend.relational.model.Itinerary;
import at.spengergasse.backend.relational.model.User;
import at.spengergasse.backend.relational.service.JpaItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping(path = JpaItineraryController.ITINERARY_PATH)
@CrossOrigin(origins = "http://localhost:3001")
public class JpaItineraryController {
    public static final String ITINERARY_PATH = "api/relational/itinerary";
    private final JpaItineraryService itineraryService;

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<ItineraryDto>> fetchAllItineraries() {
        List<ItineraryDto> itineraries = itineraryService.getAllItineraries();
        return ResponseEntity.ok(itineraries);
    }

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<ItineraryDto>  addItinerary(@RequestBody ItineraryDto itineraryDto) {
        return itineraryService.saveItinerary(itineraryDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/{uuid}", produces = "application/json")
    public ResponseEntity<ItineraryDto> fetchItineraryByUuid(@PathVariable UUID uuid) {
        return itineraryService.findItineraryUUID(uuid)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/update", produces = "application/json")
    public ResponseEntity<ItineraryDto> updateItinerary(@RequestBody ItineraryDto itineraryDto) {
        return itineraryService.updateItinerary(itineraryDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/updateUser", produces = "application/json")
    public ResponseEntity<ItineraryDto> updateItineraryUser(@RequestParam UUID uuid, @RequestParam User user) {
        return itineraryService.updateItineraryUser(uuid, user)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping(value = "/delete", produces = "application/json")
    public ResponseEntity<ItineraryDto> deleteItinerary(@RequestBody ItineraryDto itineraryDto) {
        Itinerary it = ItineraryDto.toEntity(itineraryDto);
        boolean deleted = itineraryService.deleteItinerary(it);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value = "/delete/{uuid}", produces = "application/json")
    public ResponseEntity<ItineraryDto> deleteItineraryByUuid(@PathVariable UUID uuid) {
        boolean deleted = itineraryService.deleteItineraryByUUID(uuid);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
