package com.radequin.gangoffour.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.radequin.gangoffour.domain.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class PlayerDTO extends UserDTO {

    private long id;
    private String profilePicture;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int turn;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int cardLeft;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String hand;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String firstName;
    @JsonIgnore
    private String lastName;


    public PlayerDTO(Player player) {
        setId(player.getId());
        setUserName(player.getUsername());
        setEmail(player.getEmail());
        setProfilePicture(player.getProfilePicture());
    }


}