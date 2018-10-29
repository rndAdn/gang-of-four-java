package com.radequin.gangoffour.repository;

import com.radequin.gangoffour.domain.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    @Override
    Player save(Player s);

    Player findOneByUserName(String userName);

    @Override
    List<Player> findAll();

    Player findOneById(long id);

    void deleteByUserName(String username);


}
