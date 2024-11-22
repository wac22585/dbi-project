package at.spengergasse.backend.mongodb.persistence;

import java.util.List;

public interface UserProjection
{
    String getUsername();
    String getEmail();
    List<ItineraryNameProjection> getItineraries();

    interface ItineraryNameProjection
    {
        String getName();
    }
}
