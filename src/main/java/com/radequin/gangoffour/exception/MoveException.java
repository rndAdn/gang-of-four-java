package com.radequin.gangoffour.exception;

public class MoveException extends Exception {


    private static final long serialVersionUID = 5594272376504216383L;

    public MoveException(String msg) {
        super(msg);
    }

    public MoveException(Exception e) {
        super(e);
    }
}
