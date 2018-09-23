package com.radequin.gangoffour.domain;

public enum CardValue {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    PHEONIX(11),
    DRAGON(12);

    private final int value;

    CardValue(int i) {
        value = i;
    }

    public static CardValue findByValue(int value) {
        for (CardValue v : values()) {
            if (v.get() == value) {
                return v;
            }
        }
        return null;
    }

    public int get() {
        return value;
    }
}
