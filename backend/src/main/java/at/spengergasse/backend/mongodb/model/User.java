package at.spengergasse.backend.mongodb.model;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User
{
    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private List<Itinerary> itineraries;

    @Builder
    public User(String username, String email, String password, List<Itinerary> itineraries)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.itineraries = itineraries != null ? itineraries : new ArrayList<>();
    }
}
