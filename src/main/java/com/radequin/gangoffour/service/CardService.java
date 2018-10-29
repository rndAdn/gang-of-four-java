package com.radequin.gangoffour.service;


import com.radequin.gangoffour.domain.*;
import com.radequin.gangoffour.exception.HandException;
import com.radequin.gangoffour.utils.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CardService {


    /**
     * change a list of card to a BigInteger that represent the list
     *
     * @param cards list of card
     * @return BigInteger that represent the list
     */
    BigInteger cardsToBigInteger(List<Card> cards) {
        cards.sort(Card::compareTo);
        int index = 0;
        BigInteger value = BigInteger.ZERO;
        BigInteger pow;
        BigInteger puiss = new BigInteger(String.valueOf(123));
        for (Card card : cards) {
            pow = puiss.pow(index);
            value = value.add(pow.multiply(new BigInteger(card.getStringValue())));
            index++;
        }
        return value;

    }

    /**
     * Tell if the card is a valid one
     *
     * @param card to check
     * @return true if the card is valid false otherwise
     */
    private boolean isValidCard(Card card) {
        switch (card.getCardValue()) {
            case ONE:

                if (card.getCardColor() != CardColor.GREEN && card.getCardColor() != CardColor.YELLOW && card.getCardColor() != CardColor.RED && card.getCardColor() != CardColor.MULTI_COLOR) {
                    return false;
                }
                break;
            case TWO:
            case THREE:
            case FOUR:
            case FIVE:
            case SIX:
            case SEVEN:
            case EIGHT:
            case NINE:
            case TEN:

                if (card.getCardColor() != CardColor.GREEN && card.getCardColor() != CardColor.YELLOW && card.getCardColor() != CardColor.RED) {
                    return false;
                }
                break;
            case PHEONIX:
                if (card.getCardColor() != CardColor.GREEN && card.getCardColor() != CardColor.YELLOW) {
                    return false;
                }
                break;
            case DRAGON:
                if (card.getCardColor() != CardColor.RED) {
                    return false;
                }
                break;
            default:
                return false;
        }
        return true;
    }


    boolean compareHand(String hand1, String hand2) throws HandException {
        return compareHand(bigIntegerToCards(hand1), bigIntegerToCards(hand2));
    }

    private boolean compareHand(List<Card> hand1, List<Card> hand2) throws HandException {
        return compareHand(new Tuple<>(hand1, findHandValue(hand1)), new Tuple<>(hand2, findHandValue(hand2)));
    }

    List<Card> bigIntegerToCards(BigInteger value) throws HandException {
        BigInteger bigInteger = value;
        BigInteger puiss = new BigInteger(String.valueOf(123));
        List<Card> cards = new ArrayList<>();
        while (bigInteger.compareTo(BigInteger.ZERO) > 0) {
            BigInteger l = bigInteger.mod(puiss);
            bigInteger = bigInteger.subtract(l);
            bigInteger = bigInteger.divide(puiss);
            cards.add(new Card(Long.parseLong(l.toString())));
        }
        isValidCards(cards);
        return cards;
    }

    List<Card> bigIntegerToCards(String value) throws HandException {
        return bigIntegerToCards(new BigInteger(value));
    }

    private void isValidCards(List<Card> cards) throws HandException {
        for (Card c : cards) {
            if (!isValidCard(c)) {
                String msg = String.format("this Card is not a valid one %s : ", c);
                log.error(msg);
                throw new HandException(msg);
            }
        }


        int sameValueAndColor = 0;
        for (int i = 0; i < cards.size() - 1; i++) {

            if (cards.get(i).equals(cards.get(i + 1))) {
                sameValueAndColor++;
            } else {
                sameValueAndColor = 0;
            }
            if (sameValueAndColor > 1) {
                String msg = String.format("this hand is not a valid hand %s : three or more time the same card", cards);
                log.error(msg);
                throw new HandException(msg);
            }

        }
    }

    private HandValue findHandValue(@NotNull List<Card> cards) throws HandException {
        String msg = String.format("this hand is not a valid hand %s", cards);
        cards.sort(Card::compareTo);
        isValidCards(cards);


        switch (cards.size()) {
            case 1:
                return HandValue.SINGLE_CARD;
            case 2:
                if (sameValue(cards)) {
                    return HandValue.PAIRS;
                }
                throw new HandException(msg);
            case 3:
                if (sameValue(cards)) {
                    return HandValue.THREE_OF_A_KIND;
                }
                throw new HandException(msg);
            case 4:
                if (sameValue(cards)) {
                    return HandValue.GANG_OF_FOUR;
                }
                throw new HandException(msg);
            case 5:
                if (sameValue(cards)) {
                    return HandValue.GANG_OF_FIVE;
                }
                if (sameColor(cards) && isStraight(cards)) { //suite couleur
                    return HandValue.STRAIGHT_FLUSH;
                }
                if (isFull(cards)) { // Full
                    return HandValue.FULL_HOUSE;
                }
                if (sameColor(cards)) { // couleur
                    return HandValue.FLUSH;
                }
                if (isStraight(cards)) { //suite
                    return HandValue.STRAIGHT;
                }
                throw new HandException(msg);
            case 6:
                if (sameValue(cards)) {
                    return HandValue.GANG_OF_SIX;
                }
                throw new HandException(msg);
            case 7:
                if (sameValue(cards)) {
                    return HandValue.GANG_OF_SEVEN;
                }
                throw new HandException(msg);
            default:
                throw new HandException(msg);
        }

    }

    private boolean compareHand(Tuple<List<Card>, HandValue> hand1, Tuple<List<Card>, HandValue> hand2) {
        if (hand1.getSeconde() == hand2.getSeconde() &&
                cardsToBigInteger(hand2.getFirst()).compareTo(cardsToBigInteger(hand1.getFirst())) > 0) {
            return true;
        } else if (hand1.getSeconde().getValue() < hand2.getSeconde().getValue()) {
            return hand2.getSeconde() == HandValue.GANG_OF_FOUR ||
                    hand2.getSeconde() == HandValue.GANG_OF_FIVE ||
                    hand2.getSeconde() == HandValue.GANG_OF_SIX ||
                    hand2.getSeconde() == HandValue.GANG_OF_SEVEN;
        }

        return false;
    }

    private boolean sameValue(List<Card> cards) {
        if (cards.isEmpty()) {
            return false;
        }
        CardValue cardValue = cards.get(0).getCardValue();
        for (Card c : cards) {
            if (!c.getCardValue().equals(cardValue)) {
                return false;
            }

        }
        return true;
    }

    private boolean isFull(List<Card> cards) {
        if (cards.size() != 5) {
            return false;
        }

        CardValue c1 = cards.get(0).getCardValue();
        CardValue c2 = cards.get(1).getCardValue();
        CardValue c3 = cards.get(2).getCardValue();
        CardValue c4 = cards.get(3).getCardValue();
        CardValue c5 = cards.get(4).getCardValue();
        // (AAABB) || (AABBB)
        return (c1.get() == c2.get() && c2.get() == c3.get() && c4.get() == c5.get()) ||
                (c1.get() == c2.get() && c3.get() == c4.get() && c4.get() == c5.get());

    }


    private boolean isStraight(List<Card> cards) {


        if (cards.isEmpty()) {
            return false;
        }
        for (int i = 0; i < cards.size() - 1; i++) {
            CardValue value = cards.get(i).getCardValue();
            CardValue valueP1 = cards.get(i + 1).getCardValue();

            if (value.get() >= valueP1.get()) {
                return false;
            }
            if (value.equals(CardValue.PHEONIX) || value.equals(CardValue.DRAGON)) {
                return false;
            }
            if (valueP1.equals(CardValue.PHEONIX) || valueP1.equals(CardValue.DRAGON)) {
                return false;
            }
        }
        return true;
    }

    private boolean sameColor(List<Card> cards) {
        if (cards.isEmpty()) {
            return false;
        }
        CardColor cardColor = cards.get(0).getCardColor();
        for (Card c : cards) {
            if (!c.getCardColor().equals(cardColor) || (c.getCardValue().equals(CardValue.PHEONIX) || c.getCardValue().equals(CardValue.DRAGON))) {
                return false;
            }

        }
        return true;
    }

    void setCard(GamePlayer player) throws HandException {
        List<Card> cards;
        cards = bigIntegerToCards(new BigInteger(player.getHand()));
        cards.sort(Card::compareTo);
        player.setHand(cardsToBigInteger(cards).toString());
        player.setCardsLeft(cards.size());
    }

    List<Card> createDeck() {
        List<Card> cardsFullCardDeck = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            CardValue cardValue = CardValue.findByValue(i);
            CardColor cardColor;
            for (int k = 0; k <= 2; k++) {
                cardColor = CardColor.findByValue(k);
                for (int j = 0; j < 2; j++) {
                    cardsFullCardDeck.add(new Card(cardValue, cardColor));
                }
            }
        }
        cardsFullCardDeck.add(new Card(CardValue.PHEONIX, CardColor.GREEN));
        cardsFullCardDeck.add(new Card(CardValue.PHEONIX, CardColor.YELLOW));
        cardsFullCardDeck.add(new Card(CardValue.DRAGON, CardColor.RED));
        cardsFullCardDeck.add(new Card(CardValue.ONE, CardColor.MULTI_COLOR));
        log.debug("createDeck size {}", cardsFullCardDeck.size());

        return cardsFullCardDeck;
    }


}
