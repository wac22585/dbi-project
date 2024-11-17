package at.spengergasse.backend.mongodb.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "itineraries")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Itinerary
{
    @Id
    private String id;
    private UUID uuid;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private User user;
    private List<ItineraryStep> itinerarySteps;

    @Builder
    public Itinerary(UUID uuid, String name, LocalDateTime startDate, LocalDateTime endDate, User user, List<ItineraryStep> itinerarySteps) {
        this.uuid = uuid;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.itinerarySteps = itinerarySteps != null ? itinerarySteps : new ArrayList<>();
    }
}
