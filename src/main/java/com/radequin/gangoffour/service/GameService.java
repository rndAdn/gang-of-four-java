package com.radequin.gangoffour.service;


import com.radequin.gangoffour.domain.*;
import com.radequin.gangoffour.exception.GameException;
import com.radequin.gangoffour.exception.HandException;
import com.radequin.gangoffour.repository.GameRepository;
import com.radequin.gangoffour.repository.PlayerRepository;
import com.radequin.gangoffour.security.TokenAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class GameService {

    private final CardService cardService;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final TokenAuthenticationService tokenAuthenticationService;


    private static final int PLAYER_PER_GAME = 4;
    public static final int DECK_SIZE = 64;
    private static final int HAND_SIZE = DECK_SIZE / PLAYER_PER_GAME;

    @Autowired
    public GameService(CardService cardService, GameRepository gameRepository, PlayerRepository playerRepository, TokenAuthenticationService tokenAuthenticationService) {
        this.cardService = cardService;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    public Optional<Game> findGame(long gameId, long playerId, String authorization) throws HandException {
        Player player = playerRepository.findOneById(playerId);
        tokenAuthenticationService.isSameUser(player, authorization);
        Game game = gameRepository.findOneById(gameId);
        log.debug("{}", game);
        if (game != null) {
            setCardLeft(game);
        }
        return Optional.ofNullable(game);
    }

    public Game createNewGame(long playerId, String authorization) {
        Game newGame = createGame(playerId, authorization);
        gameRepository.save(newGame);
        return newGame;
    }

    public Game joinGames(long gameId, long playerId, String authorization) throws GameException, HandException {
        Game game = gameRepository.findOneById(gameId);


        if (game.getPlayers().size() >= PLAYER_PER_GAME) {
            String msg = String.format("Can't join the game : game already full %d", game.getPlayers().size());
            log.error(msg);
            throw new GameException(msg);
        }

        Player player = playerRepository.findOneById(playerId);
        tokenAuthenticationService.isSameUser(player, authorization);
        GamePlayer gamePlayer = new GamePlayer(player);
        player.addPlayer(gamePlayer);
        for (GamePlayer gp : game.getPlayers()) {
            if (gp.equals(gamePlayer)) {
                String msg = String.format("Can't join the game : player %s already in the game %s", gamePlayer.getPlayer().getUsername(), game);
                log.error(msg);
                throw new GameException(msg);
            }
        }

        addPlayerToGame(gamePlayer, game);
        if (game.getPlayers().size() == PLAYER_PER_GAME) {
            game.setGameStatus(GameStatus.IN_PROGRESS);
            List<Card> deck = cardService.createDeck();
            Collections.shuffle(deck);
            for (GamePlayer p : game.getPlayers()) {
                List<Card> cards = new ArrayList<>();
                for (int i = 0; i < HAND_SIZE; i++) {
                    cards.add(deck.remove(0));
                }
                p.setHand(cardService.cardsToBigInteger(cards).toString());
            }
            GamePlayer nextPlayer = find1M(game);
            game.setNextPlayer(nextPlayer);
        }

        gameRepository.save(game);
        if (game.getPlayers().size() == PLAYER_PER_GAME) {
            for (GamePlayer p : game.getPlayers()) {
                p.setCardsLeft(HAND_SIZE);
            }
        }
        return game;
    }

    public List<Game> listGamesByPlayer(long playerId, String authorization) throws HandException {
        Player player = playerRepository.findOneById(playerId);
        tokenAuthenticationService.isSameUser(player, authorization);
        List<Game> games = gameRepository.findGamesByPlayer(playerId);
        for (Game game : games) {
            setCardLeft(game);
        }
        return games;
    }

    public List<Game> listGamesReachableByPlayer(long playerId, String authorization) throws HandException {
        Player player = playerRepository.findOneById(playerId);
        tokenAuthenticationService.isSameUser(player, authorization);
        List<Game> games = gameRepository.listGamesReachableByPlayer(playerId, GameStatus.WAITS_FOR_PLAYER);
        return games;
    }


    private GamePlayer find1M(Game game) throws HandException, GameException {
        for (GamePlayer pg : game.getPlayers()) {
            List<Card> cards = cardService.bigIntegerToCards(new BigInteger(pg.getHand()));
            long count = cards.stream().filter(c -> c.equals(new Card(CardValue.ONE, CardColor.MULTI_COLOR))).count();
            if (count > 1) {
                throw new HandException("found more than one 1M : " + count);
            } else if (count == 1) {
                return pg;
            }
        }
        throw new GameException("No 1M found");
    }

    private Game createGame(long userId, String token) {
        Game game = new Game();
        Player player = playerRepository.findOneById(userId);
        tokenAuthenticationService.isSameUser(player, token);
        GamePlayer gamePlayer = new GamePlayer(player);
        player.addPlayer(gamePlayer);
        game.setNextPlayer(gamePlayer);
        addPlayerToGame(gamePlayer, game);
        game.setGameStatus(GameStatus.WAITS_FOR_PLAYER);
        game.setCreated(LocalDateTime.now());

        return game;

    }

    private static void addPlayerToGame(GamePlayer player, Game game) {
        game.addPlayer(player);
        player.setGame(game);
        player.setOrderToPlay(game.getPlayers().size());
        player.setHand("0");


    }

    private void setCardLeft(Game game) throws HandException {
        for (GamePlayer p : game.getPlayers()) {
            cardService.setCard(p);
        }
    }

}
