package at.spengergasse.backend.mongodb.service;

import at.spengergasse.backend.mongodb.dto.ItineraryDto;
import at.spengergasse.backend.mongodb.model.Itinerary;
import at.spengergasse.backend.mongodb.persistence.MongoItineraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MongoItineraryService
{
    private final MongoItineraryRepository itineraryRepository;

    // Create or Update an itinerary
    public Optional<ItineraryDto> saveItinerary(ItineraryDto itinerary)
    {
        if (itinerary == null) return Optional.empty();

        try {
            Itinerary save = ItineraryDto.toEntity(itinerary);
            itineraryRepository.save(save);
            return Optional.of(itinerary);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Find an itinerary by ID
    public Optional<ItineraryDto> findItineraryByUUID(UUID uuid)
    {
        if(uuid == null) return Optional.empty();

        try {
            return Optional.of(ItineraryDto.fromEntity(itineraryRepository.findByUuid(uuid)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Get all itineraries
    public List<ItineraryDto> findAllItineraries() {
        return itineraryRepository.findAll().stream()
                .map(ItineraryDto::fromEntity)
                .toList();
    }

    // Delete an itinerary
    public boolean deleteItinerary(Itinerary itinerary)
    {
        if (itinerary == null || itinerary.getUuid() == null) return false;

        try {
            itineraryRepository.deleteById(itinerary.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Delete an itinerary by ID
    public boolean deleteItineraryByUUID(UUID uuid)
    {
        if (uuid == null || itineraryRepository.findByUuid(uuid) == null) return false;
        try {
            itineraryRepository.deleteByUuid(uuid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
