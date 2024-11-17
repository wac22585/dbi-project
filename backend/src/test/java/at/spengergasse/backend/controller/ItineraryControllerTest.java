package at.spengergasse.backend.controller;

import at.spengergasse.backend.dto.ItineraryDto;
import at.spengergasse.backend.model.*;
import at.spengergasse.backend.service.ItineraryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static at.spengergasse.backend.controller.ItineraryController.ITINERARY_PATH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItineraryControllerTest
{
    private final String path = "http://localhost/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItineraryService itineraryService;

    private ItineraryDto itineraryDto;
    private Itinerary itinerary;
    private UUID itineraryId;
    private User user;

    @BeforeEach
    void setUp()
    {
        itineraryId = UUID.randomUUID();

        user = User.builder()
                .username("jxh17")
                .password("password")
                .email("vil22528@spengergasse.at")
                .build();

        itinerary = Itinerary.builder()
                .uuid(itineraryId)
                .name("10 day Scandinavia Itinerary")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .user(user)
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
                .build();


        itineraryDto = ItineraryDto.fromEntity(itinerary);
    }

    @Test
    void verifyAddNewItinerary() throws Exception {
        ItineraryDto itineraryDto = this.itineraryDto;
        when(itineraryService.saveItinerary(itineraryDto)).thenReturn(Optional.ofNullable(itineraryDto));
        mockMvc.perform(MockMvcRequestBuilders.post(path + ITINERARY_PATH + "/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itineraryDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

}