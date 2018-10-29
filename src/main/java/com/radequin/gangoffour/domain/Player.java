package com.radequin.gangoffour.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Player extends User {

    private static final long serialVersionUID = -8441754006598030025L;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<GamePlayer> playerGames = new ArrayList<>();


    public void addPlayer(GamePlayer player) {
        if (playerGames == null) {
            playerGames = new ArrayList<>();
        }
        playerGames.add(player);
    }
}
