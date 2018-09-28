package com.radequin.gangoffour.service;

import com.radequin.gangoffour.domain.Player;
import com.radequin.gangoffour.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Optional<Player> findPlayer(long id) {
        return Optional.ofNullable(playerRepository.findOneById(id));
    }


    public List<Player> listPlayers() {
        List<Player> users = playerRepository.findAll();
        log.debug("user list {}", users);
        return users;
    }


}