package com.radequin.gangoffour.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NaturalIdCache
public class Game implements Serializable {

    private static final long serialVersionUID = 8081352928159568759L;
    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "next_player_id", nullable = false)
    private GamePlayer nextPlayer;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GamePlayer> players = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Move> moves = new ArrayList<>();

    public void addPlayer(GamePlayer player) {
        if (players == null) {
            players = new ArrayList<>();
        }
        players.add(player);
    }

    public void addMove(Move move) {
        if (moves == null) {
            moves = new ArrayList<>();
        }
        moves.add(move);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", nextPlayer=" + "" + (nextPlayer != null) +
                ", created=" + created +
                ", gameStatus=" + gameStatus +
                ", players=" + players.isEmpty() +
                '}';
    }
}
