package com.radequin.gangoffour.controller.dto;

import com.radequin.gangoffour.domain.Move;
import com.radequin.gangoffour.utils.Common;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MoveDTO {

    private int id;

    @NotNull
    private GameDTO game;

    @NotNull
    private String cardsPlayed;

    @NotNull
    private PlayerDTO player;

    private String created;


    public MoveDTO(Move move) {
        setId(move.getId());
        created = move.getCreated().format(Common.DATE_TIME_FORMATTER);
        player = new PlayerDTO(move.getPlayer());
        cardsPlayed = move.getCardsPlayed();
        game = new GameDTO(move.getGame());

    }

    public Move toMove() {
        Move move = new Move();

        move.setId(id);
        move.setCardsPlayed(cardsPlayed);
        move.setCreated(LocalDateTime.now());
        return move;

    }
}
