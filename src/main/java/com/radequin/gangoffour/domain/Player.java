package com.radequin.gangoffour.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Player extends User {

    private static final long serialVersionUID = -8441754006598030025L;
}
