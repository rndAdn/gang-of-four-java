package com.radequin.gangoffour.service;


import com.radequin.gangoffour.domain.*;
import com.radequin.gangoffour.exception.GameException;
import com.radequin.gangoffour.exception.HandException;
import com.radequin.gangoffour.exception.MoveException;
import com.radequin.gangoffour.repository.GameRepository;
import com.radequin.gangoffour.repository.MoveRepository;
import com.radequin.gangoffour.repository.PlayerRepository;
import com.radequin.gangoffour.security.TokenAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class MoveService {

    private final CardService cardService;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final MoveRepository moveRepository;
    private final TokenAuthenticationService tokenAuthenticationService;


    @Autowired
    public MoveService(CardService cardService, GameRepository gameRepository, PlayerRepository playerRepository, MoveRepository moveRepository, TokenAuthenticationService tokenAuthenticationService) {
        this.cardService = cardService;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.moveRepository = moveRepository;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }


    public Move playMove(Move move, long gameId, long userId, String authorization) throws MoveException, HandException, GameException {

        Game game = gameRepository.findOneById(gameId);
        Optional<Move> lastMove = getLastMove(game);
        Player player = playerRepository.findOneById(userId);
        tokenAuthenticationService.isSameUser(player, authorization);
        GamePlayer gamePlayer = getNextGamePlayer(game, player);

        // CHeck if player is next player
        if (!game.getNextPlayer().getPlayer().equals(player)) {
            String msg = String.format("Move unauthorized : you're not next player : {} you are : %s %s", game.getNextPlayer().getPlayer().getUsername(), player.getUsername());
            log.error(msg);
            throw new MoveException(msg);
        }

        // if player pass turn
        if (passTurn(move)) {
            GamePlayer nextPlayer = getNextGamePlayer(game, game.getNextPlayer());
            // we check if the next player is the one whom played last
            if (lastMove.isPresent() && nextPlayer.getPlayer() == lastMove.get().getPlayer()) {
                // if he is the he won the game

                game.setGameStatus(GameStatus.FINISHED);
                gameRepository.save(game);
                return move;
            }
        }

        List<Card> cardsPlayed = cardService.bigIntegerToCards(move.getCardsPlayed());
        List<Card> playerCards = cardService.bigIntegerToCards(gamePlayer.getHand());

        // check if the player really have cards he want to play
        if (!contains(playerCards, cardsPlayed)) {
            String msg = String.format("You can't play cards you don't have Hand %s  move %s", playerCards, cardsPlayed);
            log.error(msg);
            throw new MoveException(msg);
        }

        // remove played card from the player
        for (Card card : cardsPlayed) {
            playerCards.remove(card);

        }

        // if the player hands are empty the he won the game
        if (playerCards.isEmpty()) {
            game.setGameStatus(GameStatus.FINISHED);
            gamePlayer.setHand(cardService.cardsToBigInteger(playerCards).toString());
            move.setGame(game);
            move.setPlayer(player);
            game.addMove(move);
            gameRepository.save(game);
            gameRepository.save(game);
            return move;
        }

        if (lastMove.isPresent()) {
            if (!cardService.compareHand(move.getCardsPlayed(), lastMove.get().getCardsPlayed())) {
                String msg = String.format("You can't play cards");
                log.error(msg);
                throw new MoveException(msg);
            }

        }

        game.setNextPlayer(getNextGamePlayer(game, game.getNextPlayer()));
        gamePlayer.setHand(cardService.cardsToBigInteger(playerCards).toString());
        move.setGame(game);
        move.setPlayer(player);
        game.addMove(move);
        gameRepository.save(game);
        return move;
    }


    private static Optional<Move> getLastMove(Game game) {
        List<Move> gameMoves = game.getMoves();
        if (gameMoves == null || gameMoves.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(gameMoves.get(gameMoves.size() - 1));
    }

    private static GamePlayer getNextGamePlayer(Game game, GamePlayer gamePlayer) throws GameException {
        int order = gamePlayer.getOrderToPlay() + 1;
        order = (order % 4) + 1;
        for (GamePlayer playerGame : game.getPlayers()) {
            if (playerGame.getOrderToPlay() == order) {
                return playerGame;
            }
        }
        throw new GameException("");

    }

    private static GamePlayer getNextGamePlayer(Game game, Player player) throws GameException {
        for (GamePlayer playerGame : game.getPlayers()) {
            if (playerGame.getPlayer().equals(player)) {
                return playerGame;
            }
        }
        throw new GameException("");

    }

    private static boolean passTurn(Move move) {
        return (move.getCardsPlayed().equals("0"));
    }

    private static boolean contains(List<Card> list1, List<Card> list2) {

        List<Card> list1Copy = new ArrayList<>(list1.size());
        list1Copy.addAll(list1);
        for (Card c : list2) {
            boolean found = false;
            for (Card c2 : list1Copy) {
                if (c.equals(c2)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            } else {
                list1Copy.remove(c);
            }
        }


        return true;
    }
}
