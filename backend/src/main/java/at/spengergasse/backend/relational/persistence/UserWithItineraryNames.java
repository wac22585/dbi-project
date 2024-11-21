package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.Itinerary;

import java.util.List;

public interface UserWithItineraryNames
{
    String getUsername();
    List<ItineraryNameOnly> getItineraries();

    interface ItineraryNameOnly
    {
        String getName();
    }
}
