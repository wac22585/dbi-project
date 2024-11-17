package at.spengergasse.backend.mongodb.service;

import at.spengergasse.backend.mongodb.model.Itinerary;
import at.spengergasse.backend.mongodb.persistence.MongoItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MongoItineraryService {

    private final MongoItineraryRepository itineraryRepository;

    @Autowired
    public MongoItineraryService(MongoItineraryRepository itineraryRepository) {
        this.itineraryRepository = itineraryRepository;
    }

    // Create or Update an itinerary
    public Itinerary saveItinerary(Itinerary itinerary) {
        return itineraryRepository.save(itinerary);
    }

    // Find an itinerary by ID
    public Optional<Itinerary> getItineraryById(String id) {
        return itineraryRepository.findById(id);
    }

    // Get all itineraries
    public List<Itinerary> getAllItineraries() {
        return itineraryRepository.findAll();
    }

    // Delete an itinerary by ID
    public void deleteItineraryById(String id) {
        itineraryRepository.deleteById(id);
    }
}
