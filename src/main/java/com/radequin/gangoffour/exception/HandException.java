package com.radequin.gangoffour.exception;

public class HandException extends Exception {

    public HandException(String msg) {
        super(msg);
    }

    public HandException(Exception msg) {
        super(msg);
    }
}
