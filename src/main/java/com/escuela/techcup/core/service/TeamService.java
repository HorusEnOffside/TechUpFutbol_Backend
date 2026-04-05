package com.escuela.techcup.core.service;

import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.enums.InvitationStatus;

import java.util.List;
import java.awt.image.BufferedImage;

public interface TeamService {

    Team createTeam(String name, String uniformColors, BufferedImage logo, String captainUserId);

    void invitePlayer(String teamId, String playerId, String message);

    void respondInvitation(String invitationId, InvitationStatus action);

    boolean validateTeamComposition(String teamId);

    boolean validatePlayerUniquePerTournament(String playerId, String tournamentId);

    Team getTeamById(String teamId);

    List<Team> getAllTeams();

    boolean validateEngineeringMajority(String teamId);
}