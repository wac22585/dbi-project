package at.spengergasse.backend.mongodb.modelRef;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "itinerariesRef")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Itinerary
{
    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private UUID uuid;
    @Column(nullable = false)
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @DBRef
    private User user;
    @DBRef
    private List<ItineraryStep> itinerarySteps;

    @Builder
    public Itinerary(UUID uuid, String name, LocalDateTime startDate, LocalDateTime endDate, User user, List<ItineraryStep> itinerarySteps)
    {
        this.uuid = uuid;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.itinerarySteps = itinerarySteps != null ? itinerarySteps : new ArrayList<>();
    }
}
