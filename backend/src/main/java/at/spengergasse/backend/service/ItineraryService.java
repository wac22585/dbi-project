package at.spengergasse.backend.service;

import at.spengergasse.backend.dto.ItineraryDto;
import at.spengergasse.backend.dto.UserDto;
import at.spengergasse.backend.model.Itinerary;
import at.spengergasse.backend.model.ItineraryStep;
import at.spengergasse.backend.model.User;
import at.spengergasse.backend.persistence.ItineraryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;

    /**
     * Create an itinerary
     *
     * @param itinerary the itinerary to save
     * @return the saved itinerary
     */
    public Optional<ItineraryDto> saveItinerary(ItineraryDto itinerary) {
        if (itinerary == null) return Optional.empty();

        try {
            Itinerary save = ItineraryDto.toEntity(itinerary);
            return Optional.of(ItineraryDto.fromEntity(itineraryRepository.save(save)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Create a new itinerary with the given details.
     *
     * @param name           the name of the itinerary
     * @param startDate      the start date of the itinerary
     * @param endDate        the end date of the itinerary
     * @param user           the user associated with the itinerary
     * @param itinerarySteps the list of itinerary steps
     * @return the created itinerary
     */
    public Optional<ItineraryDto> createItinerary(String name, LocalDateTime startDate, LocalDateTime endDate, User user, List<ItineraryStep> itinerarySteps) {
        //Check for invalid properties
        if (endDate.isBefore(startDate)) return Optional.empty();

        if (user == null || user.getId() == null) return Optional.empty();

        try {
            Itinerary itinerary = Itinerary.builder()
                    .name(name)
                    .startDate(startDate)
                    .endDate(endDate)
                    .user(user)
                    .itinerarySteps(itinerarySteps)
                    .build();

            return Optional.of(ItineraryDto.fromEntity(itineraryRepository.save(itinerary)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<ItineraryDto> updateItinerary(ItineraryDto itinerary)
    {
        //Check for invalid properties
        if (itinerary == null) return Optional.empty();

        Itinerary update = ItineraryDto.toEntity(itinerary);

        if (update.getName().isEmpty()
                || update.getStartDate() == null
                || update.getEndDate() == null) return Optional.empty();

        if (update.getName().isBlank()
                || update.getStartDate().isAfter(update.getEndDate())) return Optional.empty();

        try {
            Itinerary updated = itineraryRepository.findById(update.getId());
            if (updated == null) return Optional.empty();

            updated.setName(update.getName());
            updated.setStartDate(update.getStartDate());
            updated.setEndDate(update.getEndDate());
            updated.setItinerarySteps(update.getItinerarySteps());

            if (updated.getUser() != update.getUser())
            {
                updated.getUser().removeItinerary(updated);
                updated.setUser(update.getUser());
            }


            return Optional.of(ItineraryDto.fromEntity(itineraryRepository.save(updated)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    /**
     * Delete an itinerary by its ID
     *
     * @param uuid the ID of the itinerary to delete
     */
    public boolean deleteItineraryByUUID(UUID uuid) {
        try {
            itineraryRepository.deleteByUuid(uuid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteItinerary(Itinerary itinerary)
    {
        if (itinerary == null || itinerary.getId() == null) return false;

        try {
            itinerary.getUser().removeItinerary(itinerary);
            itineraryRepository.deleteById(itinerary.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Retrieve an itinerary by its ID
     *
     * @param id the ID of the itinerary
     * @return an Optional containing the found itinerary, or empty if not found
     */
    public Optional<ItineraryDto> findItineraryById(Long id) {
        if (id == null) return Optional.empty();

        try {
            return Optional.of(ItineraryDto.fromEntity(itineraryRepository.findById(id)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieve all itineraries
     *
     * @return a list of all itineraries
     */
    public List<Itinerary> getAllItineraries() {
        return itineraryRepository.findAll();
    }

    /**
     * Retrieve all itineraries for a specific user by user ID
     *
     * @param userId the ID of the user
     * @return a list of itineraries associated with the user
     */
    public List<Itinerary> getItinerariesByUserId(Long userId) {
        return itineraryRepository.findAllByUserId(userId);
    }

    /**
     * Update the user associated with an itinerary
     *
     * @param itineraryId the ID of the itinerary
     * @param user        the user to associate with the itinerary
     * @return an Optional containing the updated itinerary, or empty if not found
     */
    public Optional<ItineraryDto> updateItineraryUser(Long itineraryId, User user) {
        Optional<ItineraryDto> optionalItinerary = findItineraryById(itineraryId);
        if (optionalItinerary.isPresent()) {
            Itinerary itinerary = ItineraryDto.toEntity(optionalItinerary.get());
            itinerary.setUser(user);
            Itinerary updatedItinerary = itineraryRepository.save(itinerary);
            return Optional.of(ItineraryDto.fromEntity(updatedItinerary));
        }
        return Optional.empty();
    }
}

