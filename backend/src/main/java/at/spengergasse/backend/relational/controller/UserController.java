package at.spengergasse.backend.relational.controller;

import at.spengergasse.backend.relational.dto.UserDto;
import at.spengergasse.backend.relational.service.JpaUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping(path = UserController.USER_PATH)
public class UserController {
    public static final String USER_PATH = "api/relational/user";
    private final JpaUserService userService;

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<UserDto> fetchUserByUsername(@PathVariable String username) {
        return userService.findUserByUserName(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/update", produces = "application/json")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        return userService.updateUserInformation(userDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/updateUsername", produces = "application/json")
    public ResponseEntity<UserDto> updateUsername(@RequestParam String oldUsername, @RequestParam String newUsername) {
        return userService.updateUserName(oldUsername, newUsername)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping(value = "/delete", produces = "application/json")
    public ResponseEntity<UserDto> deleteUser(@RequestBody UserDto user) {
        boolean deleted = userService.deleteUser(user);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
