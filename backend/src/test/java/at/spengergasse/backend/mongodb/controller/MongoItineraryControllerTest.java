package at.spengergasse.backend.mongodb.controller;

import at.spengergasse.backend.mongodb.dto.ItineraryDto;
import at.spengergasse.backend.mongodb.model.*;
import at.spengergasse.backend.mongodb.service.MongoItineraryService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static at.spengergasse.backend.mongodb.controller.MongoItineraryController.ITINERARY_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MongoItineraryControllerTest
{
    private final String path = "http://localhost/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MongoItineraryService itineraryService;

    private ItineraryDto itineraryDto;
    private Itinerary itinerary;
    private UUID itineraryId;

    @BeforeEach
    void setup() {
        itineraryId = UUID.randomUUID();

        itinerary = Itinerary.builder()
                .uuid(itineraryId)
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
                .build();


        itineraryDto = ItineraryDto.fromEntity(itinerary);
    }

    @Test
    void verifyAddNewItinerary() throws Exception {
        ItineraryDto itineraryDto = this.itineraryDto;
        when(itineraryService.saveItinerary(any())).thenReturn(Optional.ofNullable(itineraryDto));
        mockMvc.perform(MockMvcRequestBuilders.post(path + ITINERARY_PATH + "/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itineraryDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyFindItineraryByUuid() throws Exception {
        ItineraryDto itineraryDto = this.itineraryDto;
        when(itineraryService.findItineraryByUUID(any())).thenReturn(Optional.ofNullable(itineraryDto));
        mockMvc.perform(MockMvcRequestBuilders.get(path + ITINERARY_PATH + "/" + itineraryId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itineraryDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyFindAllItineraries() throws Exception {
        List<ItineraryDto> itineraryDtos = List.of(itineraryDto);
        when(itineraryService.findAllItineraries()).thenReturn(itineraryDtos);
        mockMvc.perform(MockMvcRequestBuilders.get(path + ITINERARY_PATH + "/all")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itineraryDtos)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyDeleteItineraryByUuid() throws Exception {
        when(itineraryService.deleteItineraryByUUID(any())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.get(path + ITINERARY_PATH + "/delete/" + itineraryId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itineraryDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyDeleteItinerary() throws Exception {
        when(itineraryService.deleteItinerary(any())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete(path + ITINERARY_PATH + "/delete")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itineraryDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}