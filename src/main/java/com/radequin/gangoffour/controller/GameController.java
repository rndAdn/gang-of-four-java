package com.radequin.gangoffour.controller;


import com.radequin.gangoffour.controller.dto.GameDTO;
import com.radequin.gangoffour.controller.dto.MoveDTO;
import com.radequin.gangoffour.controller.dto.PlayerDTO;
import com.radequin.gangoffour.domain.Game;
import com.radequin.gangoffour.domain.GamePlayer;
import com.radequin.gangoffour.domain.Move;
import com.radequin.gangoffour.exception.GameException;
import com.radequin.gangoffour.exception.HandException;
import com.radequin.gangoffour.exception.MoveException;
import com.radequin.gangoffour.service.GameService;
import com.radequin.gangoffour.service.MoveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game")
@Slf4j
public class GameController {

    private final GameService gameService;
    private final MoveService moveService;


    @Autowired
    public GameController(GameService gameService, MoveService moveService) {
        this.gameService = gameService;
        this.moveService = moveService;
    }

    @PostMapping(value = "/create/user/{playerId}")
    public ResponseEntity<?> createGame(@PathVariable long playerId, @RequestHeader("Authorization") String authorization) {
        try {
            Game newGame = gameService.createNewGame(playerId, authorization);


            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newGame.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/join/{gameId}/user/{playerId}")
    public ResponseEntity<?> joinGame(@PathVariable long gameId, @PathVariable long playerId, @RequestHeader("Authorization") String authorization) {
        try {
            Game game = gameService.joinGames(gameId, playerId, authorization);
            GameDTO gameDTO = new GameDTO(game);
            return ResponseEntity.ok(gameDTO);
        } catch (GameException | BadCredentialsException |
                HandException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping(value = "/games/player/{playerId}")
    public ResponseEntity<?> getGamesByPlayer(@PathVariable long playerId, @RequestHeader("Authorization") String authorization) throws HandException {

        try {
            return ResponseEntity.ok(gameService.listGamesByPlayer(playerId, authorization).stream().map(GameDTO::new).collect(Collectors.toList()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/games/reachable/player/{playerId}")
    public ResponseEntity<?> getReachableGames(@PathVariable long playerId, @RequestHeader("Authorization") String authorization) throws HandException {

        try {
            return ResponseEntity.ok(gameService.listGamesReachableByPlayer(playerId, authorization).stream().map(GameDTO::new).collect(Collectors.toList()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{gameId}/player/{playerId}")
    public ResponseEntity<?> getGame(@PathVariable long gameId, @PathVariable long playerId, @RequestHeader("Authorization") String authorization) throws HandException {
        Optional<Game> gameOptional = gameService.findGame(gameId, playerId, authorization);

        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            GameDTO gameDTO = new GameDTO(game);

            for (GamePlayer p : game.getPlayers()) {
                if (p.getId() == playerId) {
                    for (PlayerDTO playerDTO : gameDTO.getPlayers()) {
                        if (playerDTO.getId() == playerId) {
                            playerDTO.setHand(p.getHand());
                            return ResponseEntity.ok(gameDTO);
                        }
                    }
                }
            }

            return ResponseEntity.ok(gameDTO);

        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping(value = "/{gameId}/player/{playerId}/move")
    public ResponseEntity<?> playMove(@PathVariable long gameId, @PathVariable long playerId, @RequestHeader("Authorization") String authorization, @RequestBody MoveDTO moveDTO) {
        log.debug("playMove called");
        Move move = moveDTO.toMove();
        try {
            Move newMove = moveService.playMove(move, gameId, playerId, authorization);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newMove.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } catch (MoveException | HandException | GameException e) {
            return ResponseEntity.badRequest().body(e);
        }

    }
}
