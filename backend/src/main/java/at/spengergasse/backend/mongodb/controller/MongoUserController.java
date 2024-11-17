package at.spengergasse.backend.mongodb.controller;

import at.spengergasse.backend.mongodb.dto.UserDto;
import at.spengergasse.backend.mongodb.service.MongoUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping(path = MongoUserController.USER_PATH)
public class MongoUserController
{
    public static final String USER_PATH = "api/mongodb/user";
    private final MongoUserService userService;

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<UserDto> fetchUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/email/{email}", produces = "application/json")
    public ResponseEntity<UserDto> fetchUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/all", produces = "application/json")
    public List<UserDto> fetchAllUsers() {
        return userService.findAllUsers();
    }

    @DeleteMapping(value = "/delete/{username}", produces = "application/json")
    public ResponseEntity<UserDto> deleteUser(@PathVariable String username) {
        boolean deleted = userService.deleteUserByUsername(username);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
