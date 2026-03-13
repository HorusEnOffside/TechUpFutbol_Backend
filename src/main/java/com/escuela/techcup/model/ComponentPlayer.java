package com.escuela.techcup.model;

import com.escuela.techcup.model.enums.Position;
import com.escuela.techcup.model.enums.PlayerStatus;

public interface ComponentPlayer {
    Position getPosition();
    int getDorsalNumber();
    PlayerStatus getStatus();
    UserPlayer getUserPlayer();
}
