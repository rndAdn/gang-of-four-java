package com.radequin.gangoffour.controller.dto;

import com.radequin.gangoffour.domain.Game;
import com.radequin.gangoffour.domain.GameStatus;
import com.radequin.gangoffour.utils.Common;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class GameDTO {


    private long id;

    @NotNull
    private PlayerDTO nextPlayer;

    @NotNull
    private String created;

    @NotNull
    private GameStatus gameStatus;

    @NotNull
    private Set<PlayerDTO> players;

    public GameDTO(Game game) {
        setId(game.getId());
        created = game.getCreated().format(Common.DATE_TIME_FORMATTER);
        gameStatus = game.getGameStatus();
        nextPlayer = new PlayerDTO(game.getNextPlayer());
        players = game.getPlayers().stream().map(PlayerDTO::new).collect(Collectors.toSet());
    }


}
