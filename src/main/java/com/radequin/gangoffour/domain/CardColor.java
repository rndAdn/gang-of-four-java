package com.radequin.gangoffour.domain;

public enum CardColor {

    GREEN(0, 'G'),
    YELLOW(1, 'Y'),
    RED(2, 'R'),
    MULTI_COLOR(3, 'M');

    private final int intColor;
    private final char charColor;

    CardColor(int i, char color) {
        intColor = i;
        charColor = color;
    }

    public static CardColor findByValue(int value) {
        for (CardColor v : values()) {
            if (v.getIntValue() == value) {
                return v;
            }
        }
        return null;
    }

    public static CardColor findByValue(char value) {
        for (CardColor v : values()) {
            if (v.charColor == value) {
                return v;
            }
        }
        return null;
    }

    public int getIntValue() {
        return intColor;
    }

    public char getCharValue() {
        return charColor;
    }
}
