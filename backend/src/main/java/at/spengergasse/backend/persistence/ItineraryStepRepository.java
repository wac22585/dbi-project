package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.ItineraryStep;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ItineraryStepRepository extends Repository<ItineraryStep, Long>
{
    ItineraryStep save(ItineraryStep itineraryStep);
    ItineraryStep findById(Long id);
    void deleteById(Long id);
    void deleteByItineraryId(Long id);

    List<ItineraryStep> findAllByItineraryId(Long id);
    List<ItineraryStep> findAll();

    void deleteAll();
}
