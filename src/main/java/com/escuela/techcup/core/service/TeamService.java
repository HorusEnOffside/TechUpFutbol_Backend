package com.escuela.techcup.core.service;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.core.model.Invitation;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.core.model.enums.InvitationStatus;
import org.springframework.web.multipart.MultipartFile;

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

    // Consultar invitaciones de un jugador
    List<Invitation> getInvitationsByPlayer(String playerId);

    void changeFormation(Formation formation, String teamId, String matchId);
    List<Formation> getAllFormations();
    Formation getEnemyFormation(String teamId);

    Payment uploadPayment(String teamId, PaymentDTO paymentDTO, MultipartFile voucher);
}