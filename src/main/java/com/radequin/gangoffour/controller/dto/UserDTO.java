package com.radequin.gangoffour.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDTO {

    private long id;

    @NotNull
    private String userName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @NotNull
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String profilePicture;


}