package com.radequin.gangoffour.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GamePlayer implements Serializable {
    private static final long serialVersionUID = 1463050164400189860L;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private long id;

    @Column(name = "handCard", nullable = false)
    private String hand;

    @Column(name = "score", nullable = false)
    private long score;

    @Column(name = "order_to_play", nullable = false)
    private int orderToPlay;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "player_id")
    private Player player;

    @Transient
    int cardsLeft;


    public GamePlayer(Player player) {
        setPlayer(player);
    }


    @Override
    public String toString() {
        return "GamePlayer{" +
                "hand='" + hand + '\'' +
                ", score=" + score +
                ", orderToPlay=" + orderToPlay +
                ", cardsLeft=" + cardsLeft +
                ", game=" + game == null ? "null" : "not null" +
                '}';
    }
}
