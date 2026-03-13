package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;

import lombok.Data;

@Data
public abstract class PlayerDecorator implements ComponentPlayer {
	protected final ComponentPlayer componentPlayer;

	public PlayerDecorator(ComponentPlayer componentPlayer) {
		this.componentPlayer = componentPlayer;
	}

	public Position getPosition() {
		return componentPlayer.getPosition();
	}

	public int getDorsalNumber() {
		return componentPlayer.getDorsalNumber();
	}

	public PlayerStatus getStatus() {
		return componentPlayer.getStatus();
	}

	public UserPlayer getUserPlayer() {
		return componentPlayer.getUserPlayer();
	}

}
