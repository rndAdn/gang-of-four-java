package com.radequin.gangoffour.controller;

import com.radequin.gangoffour.controller.dto.PlayerDTO;
import com.radequin.gangoffour.domain.Player;
import com.radequin.gangoffour.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/player")
public class PlayerController {

    final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getPlayer(@PathVariable long id) {

        Optional<Player> playerOptional = playerService.findPlayer(id);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            PlayerDTO playerDTO = new PlayerDTO(player);
            return ResponseEntity.ok(playerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }


    }

    @GetMapping(value = "/players")
    public List<PlayerDTO> getPlayers() {
        List<Player> playerList = playerService.listPlayers();
        return playerList.stream().map(PlayerDTO::new).collect(Collectors.toList());
    }


}