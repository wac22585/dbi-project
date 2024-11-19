package at.spengergasse.backend.relational.controller;

import at.spengergasse.backend.relational.dto.ItineraryDto;
import at.spengergasse.backend.relational.model.*;
import at.spengergasse.backend.relational.service.JpaItineraryService;
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

import static at.spengergasse.backend.relational.controller.JpaItineraryController.ITINERARY_PATH;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JpaItineraryControllerTest
{
    private final String path = "http://localhost/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JpaItineraryService itineraryService;

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

    @Test
    void verifyAddEmptyItinerary() throws Exception {
        when(itineraryService.saveItinerary(null)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.post(path + ITINERARY_PATH + "/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyFetchItineraryByUuid() throws Exception {
        UUID uuid = itineraryId;
        when(itineraryService.findItineraryUUID(uuid)).thenReturn(Optional.ofNullable(itineraryDto));
        mockMvc.perform(MockMvcRequestBuilders.get(path + ITINERARY_PATH + "/" + uuid))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyFetchItineraryByInvalidUuid() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(itineraryService.findItineraryUUID(uuid)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get(path + ITINERARY_PATH + "/" + uuid))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void verifyUpdateItinerary() throws Exception {
        ItineraryDto itineraryDto = this.itineraryDto;
        when(itineraryService.updateItinerary(itineraryDto)).thenReturn(Optional.ofNullable(itineraryDto));
        mockMvc.perform(MockMvcRequestBuilders.put(path + ITINERARY_PATH + "/update")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itineraryDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyUpdateItineraryWithInvalidProperties() throws Exception {
        when(itineraryService.updateItinerary(null)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put(path + ITINERARY_PATH + "/update")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyUpdateItineraryUserWithInvalidProperties() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(itineraryService.updateItineraryUser(uuid, null)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put(path + ITINERARY_PATH + "/updateUser")
                .param("uuid", uuid.toString())
                .param("user", String.valueOf(user.getId())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyDeleteItineraryByUuid() throws Exception {
        UUID uuid = itineraryId;
        when(itineraryService.deleteItineraryByUUID(uuid)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete(path + ITINERARY_PATH + "/delete/" + uuid))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyDeleteItineraryByInvalidUuid() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(itineraryService.deleteItineraryByUUID(uuid)).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete(path + ITINERARY_PATH + "/delete/" + uuid))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyDeleteEmptyItinerary() throws Exception {
        when(itineraryService.deleteItinerary(null)).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete(path + ITINERARY_PATH + "/delete")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyAddDuplicateItinerary() throws Exception {
        ItineraryDto itineraryDto = this.itineraryDto;
        when(itineraryService.saveItinerary(itineraryDto)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.post(path + ITINERARY_PATH + "/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(itineraryDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}