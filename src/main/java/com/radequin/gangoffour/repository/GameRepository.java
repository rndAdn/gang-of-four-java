package com.radequin.gangoffour.repository;

import com.radequin.gangoffour.domain.Game;
import com.radequin.gangoffour.domain.GameStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends CrudRepository<Game, Long> {

    Game findOneById(long id);

    @Override
    List<Game> findAll();

    @Query(value = "SELECT g FROM Game g left join GamePlayer pg on g.id = pg.game.id where pg.id = :player_id")
    List<Game> findGamesByPlayer(@Param("player_id") long playerId);


    @Query(value = "" +
            "SELECT g FROM Game g " +
            "where g.id not in (select g2.id from Game g2 left join GamePlayer pg on g2.id = pg.game.id where pg.player.id = :player_id) " +
            "AND  g.gameStatus like :gameStatus1")
    List<Game> listGamesReachableByPlayer(@Param("player_id") long playerId, @Param("gameStatus1") GameStatus gameStatus1);

}