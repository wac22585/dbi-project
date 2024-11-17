package at.spengergasse.backend.controller;

import at.spengergasse.backend.dto.ItineraryDto;
import at.spengergasse.backend.service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping(path = ItineraryController.ITINERARY_PATH)
public class ItineraryController {
    public static final String ITINERARY_PATH = "api/itinerary";
    private final ItineraryService itineraryService;

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<ItineraryDto>  addItinerary(@RequestBody ItineraryDto itineraryDto) {
        return itineraryService.saveItinerary(itineraryDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
