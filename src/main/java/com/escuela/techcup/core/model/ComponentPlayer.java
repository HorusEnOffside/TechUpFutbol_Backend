package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;

public interface ComponentPlayer {
    Position getPosition();
    int getDorsalNumber();
    PlayerStatus getStatus();
    UserPlayer getUserPlayer();
}
