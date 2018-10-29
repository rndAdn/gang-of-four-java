package com.radequin.gangoffour.domain;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Move {

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private int id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "cardsPlayed", nullable = false)
    private String cardsPlayed;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
