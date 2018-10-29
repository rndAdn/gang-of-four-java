package com.radequin.gangoffour.exception;

public class HandException extends Exception {

    private static final long serialVersionUID = 4229596593885361216L;

    public HandException(String msg) {
        super(msg);
    }

    public HandException(Exception e) {
        super(e);
    }
}
