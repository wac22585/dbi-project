package at.spengergasse.backend.mongodb.persistenceRef;

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
