package com.radequin.gangoffour.utils;

public class Tuple<T, U> {


    T first;
    U seconde;

    public Tuple(T first, U seconde) {
        this.first = first;
        this.seconde = seconde;
    }

    public T getFirst() {
        return first;
    }

    public U getSeconde() {
        return seconde;
    }
}