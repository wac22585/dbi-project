package at.spengergasse.backend.mongodb.model;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class City
{
    @Id
    private String id;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false, unique = true)
    private String country;

    @Builder
    public City(String city, String country)
    {
        this.city = city;
        this.country = country;
    }
}
