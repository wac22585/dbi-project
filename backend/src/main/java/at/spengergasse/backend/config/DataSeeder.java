package at.spengergasse.backend.config;

import at.spengergasse.backend.mongodb.model.City;
import at.spengergasse.backend.mongodb.model.Itinerary;
import at.spengergasse.backend.mongodb.model.ItineraryStep;
import at.spengergasse.backend.mongodb.model.RouteStop;
import at.spengergasse.backend.mongodb.persistence.MongoCityRepository;
import at.spengergasse.backend.mongodb.persistence.MongoItineraryRepository;
import at.spengergasse.backend.mongodb.persistence.MongoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DataSeeder {

    private final MongoCityRepository cityRepository;

    @Bean
    CommandLineRunner seedDatabase(MongoItineraryRepository itineraryRepository, MongoUserRepository userRepository) {
        return args -> {
            City city1 = cityRepository.save(City.builder()
                    .city("Stockholm")
                    .country("Sweden")
                    .build());

            itineraryRepository.save(Itinerary.builder()
                    .uuid(UUID.randomUUID())
                    .name("10 day Scandinavia Itinerary")
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(7))
                    .itinerarySteps(List.of(
                            ItineraryStep.builder()
                                    .name("Day 1 - Visit the Viking Ship Museum")
                                    .stepDate(LocalDateTime.now())
                                    .routeStops(List.of(
                                            RouteStop.builder()
                                                    .currentCity(City.builder()
                                                            .city("Stockholm")
                                                            .country("Sweden")
                                                            .build())
                                                    .nextCity(City.builder()
                                                            .city("Copenhagen")
                                                            .country("Denmark")
                                                            .build())
                                                    .build()
                                    ))
                                    .build(),
                            ItineraryStep.builder()
                                    .name("Day 2 - Explore the Old Town")
                                    .stepDate(LocalDateTime.now().plusDays(3))
                                    .routeStops(List.of(
                                            RouteStop.builder()
                                                    .currentCity(City.builder()
                                                            .city("Copenhagen")
                                                            .country("Denmark")
                                                            .build())
                                                    .nextCity(City.builder()
                                                            .city("Oslo")
                                                            .country("Norway")
                                                            .build())
                                                    .build()
                                    ))
                                    .build()
                    ))
                    .build());
        };
    }
}
//    @Bean
//    CommandLineRunner seedDatabase(JpaUserRepository userRepository) {
//        return args -> {
//            userRepository.save(User.builder()
//                            .username("hawache22585")
//                            .email("hawache@spengergasse.at")
//                            .password("password")
//                            .itineraries(List.of())
//                            .build());
//            //                            .itineraries(List.of(new ItineraryDto(UUID.randomUUID(), "Test", LocalDateTime.now(), null, null))
//            userRepository.save(User.builder()
//                    .username("havil22528")
//                    .email("havil22528@spengergasse.at")
//                    .password("password")
//                    .itineraries(List.of())
//                    .build());
//            userRepository.save(User.builder()
//                    .username("KAA")
//                    .email("KAA@spengergasse.at")
//                    .password("password")
//                    .itineraries(List.of())
//                    .build());
//        };
//    }
//}
