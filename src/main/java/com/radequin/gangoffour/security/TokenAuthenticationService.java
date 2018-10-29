package com.radequin.gangoffour.security;


import com.radequin.gangoffour.controller.dto.UserLoginDTO;
import com.radequin.gangoffour.domain.Player;
import com.radequin.gangoffour.domain.User;
import com.radequin.gangoffour.exception.UserException;
import com.radequin.gangoffour.repository.PlayerRepository;
import com.radequin.gangoffour.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.radequin.gangoffour.security.TokenAuthenticationFilter.BEARER;
import static org.apache.commons.lang3.StringUtils.removeStart;


@Service
@Slf4j
public class TokenAuthenticationService {
    private final TokenService tokens;
    private final UserRepository users;
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    TokenAuthenticationService(TokenService tokens, UserRepository users, PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
        this.tokens = tokens;
        this.users = users;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Optional<User> findByName(String username) {
        return Optional.ofNullable(users.findOneByUserName(username));
    }

    public Optional<UserLoginDTO> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);

        User user = users
                .findOneByUserName(username);
        if (user == null) {
            return Optional.empty();
        } else if (user.getUsername().equals(username) && passwordEncoder.matches(password, user.getPassword())) {
            UserLoginDTO userLoginDTO = new UserLoginDTO();
            userLoginDTO.setUsername(username);
            userLoginDTO.setProfilePicture(user.getProfilePicture());
            userLoginDTO.setToken(tokens.expiring(map));
            userLoginDTO.setId(user.getId());
            return Optional.of(userLoginDTO);
        }
        return Optional.empty();
    }

    public Optional<User> register(User user) throws UserException {

        if (findByName(user.getUsername()).isPresent()) {
            throw new UserException("User already exist");
        }


        Player newUser = new Player();
        newUser.setUserName(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        log.debug(newUser.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setProfilePicture(user.getProfilePicture());
        newUser.setId(user.getId());
        try {
            playerRepository.save(newUser);
            return Optional.of(user);
        } catch (Exception e) {
            log.error("create new user error ", e);
            throw new UserException(e);
        }
    }

    public Optional<User> findByToken(String token) {
        Map<String, String> map = tokens.verify(token);

        return Optional.ofNullable(users.findOneByUserName(map.get("username")));

    }

    public Optional<User> isSameUser(User user, String token) {
        Optional<User> logged = findByToken(removeStart(token, BEARER));

        if (logged.isPresent() && logged.get().getUsername().equals(user.getUsername())) {
            return Optional.of(user);
        }
        throw new BadCredentialsException("Your Authentication doesn't allow you this request");
    }

    public void logout(User user) {
        // Nothing to doy
    }

}