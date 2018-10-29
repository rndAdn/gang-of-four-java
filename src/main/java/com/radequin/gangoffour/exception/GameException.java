package com.radequin.gangoffour.exception;

public class GameException extends Exception {
    private static final long serialVersionUID = 1661772467852463236L;

    public GameException(String msg) {
        super(msg);
    }

    public GameException(Exception e) {
        super(e);
    }
}
