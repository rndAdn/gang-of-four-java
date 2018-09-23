package com.radequin.gangoffour.domain;

public enum HandValue {

    SINGLE_CARD(1, "single card"),

    PAIRS(2, "pair"),

    THREE_OF_A_KIND(3, "three of a kind"),

    STRAIGHT(51, "straight"),
    FLUSH(52, "flush"),
    FULL_HOUSE(53, "full house"),
    STRAIGHT_FLUSH(54, "straight flush"),

    GANG_OF_FOUR(61, "gang of four"),
    GANG_OF_FIVE(62, "gang of five"),
    GANG_OF_SIX(63, "gang of six"),
    GANG_OF_SEVEN(64, "gang of seven");

    private final int value;
    private final String name;

    HandValue(int i, String name) {
        value = i;
        this.name = name;
    }

    public static HandValue findByValue(int value) {
        for (HandValue v : values()) {
            if (v.value == value) {
                return v;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + ' ' + value;
    }
}