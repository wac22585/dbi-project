package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.ItineraryStep;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface JpaItineraryStepRepository extends Repository<ItineraryStep, Long>
{
    ItineraryStep save(ItineraryStep itineraryStep);
    ItineraryStep findById(Long id);
    void deleteById(Long id);
    void deleteByItineraryId(Long id);
    List<ItineraryStep> findAll();
    void deleteAll();
}
