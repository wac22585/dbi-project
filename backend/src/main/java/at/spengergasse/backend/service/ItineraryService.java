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

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;

    /**
     * Create an itinerary
     *
     * @param itinerary the itinerary to save or update
     * @return the saved itinerary
     */
    public Optional<ItineraryDto> saveItinerary(ItineraryDto itinerary)
    {
        if (itinerary == null) return Optional.empty();

        try {
            Itinerary save = ItineraryDto.toEntity(itinerary);
            return Optional.of(ItineraryDto.fromEntity(itineraryRepository.save(save)));
        } catch(Exception e) {
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
    public Optional<ItineraryDto> createItinerary(String name, LocalDateTime startDate, LocalDateTime endDate, User user, List<ItineraryStep> itinerarySteps)
    {
        //Check for invalid properties
        if(endDate.isBefore(startDate)) return Optional.empty();

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
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Itinerary> updateItinerary(Itinerary itinerary) {return Optional.empty();}


    /**
     * Delete an itinerary by its ID
     *
     * @param id the ID of the itinerary to delete
     */
    public void deleteItineraryById(Long id)
    {
        itineraryRepository.deleteById(id);
    }

    /**
     * Retrieve an itinerary by its ID
     *
     * @param id the ID of the itinerary
     * @return an Optional containing the found itinerary, or empty if not found
     */
    public Optional<ItineraryDto> getItineraryById(Long id)
    {
        if (id == null) return Optional.empty();

        try {
            return Optional.of(ItineraryDto.fromEntity(itineraryRepository.findById(id)));
        } catch(Exception e) {
            return Optional.empty();
        }    }

    /**
     * Retrieve all itineraries
     *
     * @return a list of all itineraries
     */
    public List<Itinerary> getAllItineraries()
    {
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
     * @param user the user to associate with the itinerary
     * @return an Optional containing the updated itinerary, or empty if not found
     */
    public Optional<Itinerary> updateItineraryUser(Long itineraryId, User user) {
        /*Optional<ItineraryDto> optionalItinerary = getItineraryById(itineraryId);
        optionalItinerary.ifPresent(itinerary -> itinerary.setUser(user));
        return optionalItinerary;*/
        return null;
    }



    /*Itinerary save(Itinerary itinerary);
    Itinerary findById(Long id);
    void deleteById(Long id);

    List<Itinerary> findAllByUserId(Long userId);
    List<Itinerary> findAll();*/


}

