package com.radequin.gangoffour.domain;

import lombok.Getter;

@Getter
public enum GameStatus {

    WAITS_FOR_PLAYER("waits_for_player"),
    IN_PROGRESS("in_progress"),
    FINISHED("finished"),
    ;

    private final String value;

    GameStatus(String value) {
        this.value = value;
    }
}
