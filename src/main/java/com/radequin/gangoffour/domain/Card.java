package com.radequin.gangoffour.domain;

import javax.validation.constraints.NotNull;

public class Card implements Comparable<Card> {

    private final CardValue cardValue;
    private final CardColor cardColor;

    private final long value;

    public Card(CardValue cardValue, CardColor cardColor) {
        this.cardValue = cardValue;
        this.cardColor = cardColor;
        value = cardColor.getIntValue() + (cardValue.get() * 10);
    }

    public Card(long value) {
        this.value = value;
        cardValue = CardValue.findByValue((int) (value / 10));
        cardColor = CardColor.findByValue((int) (value % 10));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }

        Card card = (Card) o;

        return cardValue == card.cardValue && cardColor == card.cardColor;
    }

    @Override
    public int hashCode() {
        int result = cardValue.hashCode();
        result = 31 * result + cardColor.hashCode();
        return result;
    }

    @Override
    public String toString() {

        return String.format("%d%s", cardValue.get(), cardColor.getCharValue());
    }

    public String getStringValue() {
        return String.format("%d", value);
    }

    public CardValue getCardValue() {
        return cardValue;
    }

    @Override
    public int compareTo(@NotNull Card card) {

        if (value < card.value) {
            return -1;
        }
        if (value == card.value) {
            return 0;
        }
        return 1;

    }

    public CardColor getCardColor() {
        return cardColor;
    }
}
