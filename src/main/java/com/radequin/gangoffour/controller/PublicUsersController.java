package com.radequin.gangoffour.controller;


import com.radequin.gangoffour.controller.dto.UserDTO;
import com.radequin.gangoffour.controller.dto.UserLoginDTO;
import com.radequin.gangoffour.domain.User;
import com.radequin.gangoffour.exception.UserException;
import com.radequin.gangoffour.security.TokenAuthenticationService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/public/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
@Slf4j
final class PublicUsersController {


    TokenAuthenticationService authentication;


    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        log.debug("{}", userDTO);
        User newUser = User.builder()
                .userName(userDTO.getUserName())
                .password(userDTO.getPassword())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .profilePicture(userDTO.getProfilePicture())
                .build();
        try {

            authentication.register(newUser);
            UserLoginDTO userLoginDTO = new UserLoginDTO();
            userLoginDTO.setUsername(userDTO.getUserName());
            userLoginDTO.setPassword(userDTO.getPassword());
            userLoginDTO.setProfilePicture(userDTO.getProfilePicture());
            return ResponseEntity.ok(login(userLoginDTO));
        } catch (UserException e) {
            return ResponseEntity.badRequest().body("User Not Created " + e.getMessage());
        }
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(
            @RequestBody UserLoginDTO userDTO) {
        log.debug("username {}, pwd {}", userDTO.getUsername(), userDTO.getPassword());
        Optional<UserLoginDTO> userLoginDTO = authentication
                .login(userDTO.getUsername(), userDTO.getPassword());

        if (userLoginDTO.isPresent()) {
            return ResponseEntity.ok(userLoginDTO.get());
        } else {
            return ResponseEntity.status(401).body("invalid login and/or password");
        }
    }

    @GetMapping("/up")
    String up() {
        return "I'm up";
    }

    @PostMapping("/up")
    String upp() {
        return "I'm up";
    }
}