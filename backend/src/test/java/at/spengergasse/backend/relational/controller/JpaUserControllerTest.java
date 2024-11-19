package at.spengergasse.backend.relational.controller;

import at.spengergasse.backend.relational.dto.ItineraryDto;
import at.spengergasse.backend.relational.dto.UserDto;
import at.spengergasse.backend.relational.service.JpaUserService;
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

import static at.spengergasse.backend.relational.controller.JpaUserController.USER_PATH;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JpaUserControllerTest
{
    private final String path = "http://localhost/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JpaUserService userService;

    private UserDto userDto;

    @BeforeEach
    void setUp()
    {
        userDto = new UserDto(
                "wac22585",
                "wac22585@spengergasse.at",
                "password",
                List.of(new ItineraryDto(UUID.randomUUID(), "Test", LocalDateTime.now(), null, null)
                ));
    }

    @Test
    void verifyAddNewUser() throws Exception {
        UserDto userDto = this.userDto;
        when(userService.saveUser(userDto)).thenReturn(Optional.ofNullable(userDto));
        mockMvc.perform(MockMvcRequestBuilders.post(path + USER_PATH + "/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyAddEmptyUser() throws Exception {
        when(userService.saveUser(null)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.post(path + USER_PATH + "/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyAddDuplicateUser() throws Exception {
        UserDto userDto = this.userDto;
        when(userService.saveUser(userDto)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.post(path + USER_PATH + "/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyFetchUserByUsername() throws Exception {
        UserDto userDto = this.userDto;
        when(userService.findUserByUserName("wac22585")).thenReturn(Optional.ofNullable(userDto));
        mockMvc.perform(MockMvcRequestBuilders.get(path + USER_PATH + "/wac22585"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyFetchUserByInvalidUsername() throws Exception {
        when(userService.findUserByUserName("abc")).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get(path + USER_PATH + "/"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void verifyFetchUserByNonExistentUsername() throws Exception {
        when(userService.findUserByUserName("wac22585")).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get(path + USER_PATH + "/wac22585"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void verifyUpdateUser() throws Exception {
        UserDto userDto = this.userDto;
        when(userService.updateUserInformation(userDto)).thenReturn(Optional.ofNullable(userDto));
        mockMvc.perform(MockMvcRequestBuilders.put(path + USER_PATH + "/update")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyUpdateEmptyUser() throws Exception {
        when(userService.updateUserInformation(null)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put(path + USER_PATH + "/update")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyUpdateUserWithInvalidProperties() throws Exception {
        UserDto userDto = new UserDto("", "", "", List.of());
        when(userService.updateUserInformation(userDto)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put(path + USER_PATH + "/update")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyUpdateUsername() throws Exception {
        when(userService.updateUserName("wac22585", "wac22585")).thenReturn(Optional.ofNullable(userDto));
        mockMvc.perform(MockMvcRequestBuilders.put(path + USER_PATH + "/updateUsername")
                .param("oldUsername", "wac22585")
                .param("newUsername", "wac22585"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyUpdateUsernameWithInvalidProperties() throws Exception {
        when(userService.updateUserName("", "")).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.put(path + USER_PATH + "/updateUsername")
                .param("oldUsername", "")
                .param("newUsername", ""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyDeleteUser() throws Exception {
        UserDto userDto = this.userDto;
        when(userService.deleteUser(userDto)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete(path + USER_PATH + "/delete")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void verifyDeleteEmptyUser() throws Exception {
        when(userService.deleteUser(null)).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete(path + USER_PATH + "/delete")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(null)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}