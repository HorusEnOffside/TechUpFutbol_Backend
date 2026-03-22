package com.escuela.techcup.core.model;

import java.util.List;

import lombok.Data;

@Data
public class TechupFutbol {
    private List<Tournament> tournaments;
    private List<User> users;
    private List<Player> players;
}
