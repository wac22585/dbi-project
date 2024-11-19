package at.spengergasse.backend.mongodb.controller;

import at.spengergasse.backend.mongodb.dto.ItineraryDto;
import at.spengergasse.backend.mongodb.dto.ItineraryStepDto;
import at.spengergasse.backend.mongodb.dto.UserDto;
import at.spengergasse.backend.mongodb.service.MongoUserService;
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

import static at.spengergasse.backend.mongodb.controller.MongoUserController.USER_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MongoUserControllerTest
{
    private final String path = "http://localhost/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MongoUserService userService;

    private UserDto userDto;

    @BeforeEach
    void setup() {
        userDto = new UserDto(
                "wac22585",
                "wac22585@spengergasse.at",
                "password",
                List.of(new ItineraryDto(
                        UUID.randomUUID(),
                        "Test Itinerary",
                        LocalDateTime.now(),
                        null,
                        List.of(new ItineraryStepDto("Step Nr.1", LocalDateTime.now(), null))
                ))
        );
    }

    @Test
    void verifyAddNewUser() throws Exception {
        UserDto userDto = this.userDto;
        when(userService.saveUser(any())).thenReturn(Optional.ofNullable(userDto));
        mockMvc.perform(MockMvcRequestBuilders.post(path + USER_PATH + "/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyFindUserByUsername() throws Exception {
        UserDto userDto = this.userDto;
        when(userService.findUserByUsername(userDto.username())).thenReturn(Optional.of(userDto));
        mockMvc.perform(MockMvcRequestBuilders.get(path + USER_PATH + "/" + userDto.username())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyFindUserByEmail() throws Exception {
        UserDto userDto = this.userDto;
        when(userService.findUserByEmail(userDto.email())).thenReturn(Optional.of(userDto));
        mockMvc.perform(MockMvcRequestBuilders.get(path + USER_PATH + "/email/" + userDto.email())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyFindAllUsers() throws Exception {
        UserDto userDto = this.userDto;
        when(userService.findAllUsers()).thenReturn(List.of(userDto));
        mockMvc.perform(MockMvcRequestBuilders.get(path + USER_PATH + "/all")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyDeleteUser() throws Exception {
        UserDto userDto = this.userDto;
        when(userService.deleteUserByUsername(userDto.username())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete(path + USER_PATH + "/delete/" + userDto.username())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}