package com.radequin.gangoffour.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserLoginDTO {
    @NotNull
    private String username;

    @NotNull
    private String password;

    long id;
    String token;

}
