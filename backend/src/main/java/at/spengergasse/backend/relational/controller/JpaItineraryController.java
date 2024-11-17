package at.spengergasse.backend.relational.controller;

import at.spengergasse.backend.relational.dto.ItineraryDto;
import at.spengergasse.backend.relational.service.JpaItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping(path = JpaItineraryController.ITINERARY_PATH)
public class JpaItineraryController {
    public static final String ITINERARY_PATH = "api/itinerary";
    private final JpaItineraryService itineraryService;

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<ItineraryDto>  addItinerary(@RequestBody ItineraryDto itineraryDto) {
        return itineraryService.saveItinerary(itineraryDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
