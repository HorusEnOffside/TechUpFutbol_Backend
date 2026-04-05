package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.TeamAlreadyExistsException;
import com.escuela.techcup.core.exception.TeamNotFoundException;
import com.escuela.techcup.core.exception.InvitationNotFoundException;
import com.escuela.techcup.core.exception.PlayerAlreadyInvitedException;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.enums.InvitationStatus;
import com.escuela.techcup.persistence.entity.tournament.InvitationEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamPlayerEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.StudentEntity;
import com.escuela.techcup.persistence.repository.tournament.InvitationRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamPlayerRepository;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamPlayerRepository teamPlayerRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private InvitationRepository invitationRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    private TeamEntity teamEntity;
    private PlayerEntity playerEntity;

    @BeforeEach
    void setUp() {
        teamEntity = new TeamEntity();
        teamEntity.setId("team-1");
        teamEntity.setName("Los Tigres");
        teamEntity.setUniformColor("Rojo y Negro");

        playerEntity = new PlayerEntity();
        playerEntity.setId("player-1");
    }

    // --- createTeam ---

    @Test
    void createTeam_returnsTeamWhenValid() {
        when(teamRepository.existsByNameIgnoreCase("Los Tigres")).thenReturn(false);
        when(teamRepository.save(any())).thenReturn(teamEntity);

        Team result = teamService.createTeam("Los Tigres", "Rojo y Negro", null, "captain-1");

        assertNotNull(result);
        assertEquals("Los Tigres", result.getName());
    }

    @Test
    void createTeam_throwsWhenNameIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.createTeam(null, "Rojo", null, "captain-1"));
    }

    @Test
    void createTeam_throwsWhenNameIsBlank() {
        assertThrows(InvalidInputException.class,
                () -> teamService.createTeam("  ", "Rojo", null, "captain-1"));
    }

    @Test
    void createTeam_throwsWhenNameTooShort() {
        assertThrows(InvalidInputException.class,
                () -> teamService.createTeam("Los", "Rojo", null, "captain-1"));
    }

    @Test
    void createTeam_throwsWhenUniformColorsIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.createTeam("Los Tigres", null, null, "captain-1"));
    }

    @Test
    void createTeam_throwsWhenNameAlreadyExists() {
        when(teamRepository.existsByNameIgnoreCase("Los Tigres")).thenReturn(true);

        assertThrows(TeamAlreadyExistsException.class,
                () -> teamService.createTeam("Los Tigres", "Rojo", null, "captain-1"));
    }

    // --- invitePlayer ---

    @Test
    void invitePlayer_savesInvitationWhenValid() {
        when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
        when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
        when(invitationRepository.existsByTeamIdAndPlayerId("team-1", "player-1")).thenReturn(false);

        teamService.invitePlayer("team-1", "player-1", "Únete al equipo");

        verify(invitationRepository).save(any());
    }

    @Test
    void invitePlayer_throwsWhenTeamIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.invitePlayer(null, "player-1", "msg"));
    }

    @Test
    void invitePlayer_throwsWhenPlayerIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.invitePlayer("team-1", null, "msg"));
    }

    @Test
    void invitePlayer_throwsWhenTeamNotFound() {
        when(teamRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class,
                () -> teamService.invitePlayer("no-existe", "player-1", "msg"));
    }

    @Test
    void invitePlayer_throwsWhenPlayerAlreadyInvited() {
        when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
        when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
        when(invitationRepository.existsByTeamIdAndPlayerId("team-1", "player-1")).thenReturn(true);

        assertThrows(PlayerAlreadyInvitedException.class,
                () -> teamService.invitePlayer("team-1", "player-1", "msg"));
    }

    // --- respondInvitation ---

    @Test
    void respondInvitation_acceptsAndCreatesTeamPlayer() {
        InvitationEntity invitation = new InvitationEntity();
        invitation.setId("inv-1");
        invitation.setTeam(teamEntity);
        invitation.setPlayer(playerEntity);
        invitation.setStatus(InvitationStatus.PENDING);

        when(invitationRepository.findByIdAndStatus("inv-1", InvitationStatus.PENDING))
                .thenReturn(Optional.of(invitation));

        teamService.respondInvitation("inv-1", InvitationStatus.ACCEPTED);

        verify(teamPlayerRepository).save(any());
        verify(invitationRepository).save(invitation);
    }

    @Test
    void respondInvitation_rejectsWithoutCreatingTeamPlayer() {
        InvitationEntity invitation = new InvitationEntity();
        invitation.setId("inv-1");
        invitation.setTeam(teamEntity);
        invitation.setPlayer(playerEntity);
        invitation.setStatus(InvitationStatus.PENDING);

        when(invitationRepository.findByIdAndStatus("inv-1", InvitationStatus.PENDING))
                .thenReturn(Optional.of(invitation));

        teamService.respondInvitation("inv-1", InvitationStatus.REJECTED);

        verify(teamPlayerRepository, never()).save(any());
    }

    @Test
    void respondInvitation_throwsWhenInvitationNotFound() {
        when(invitationRepository.findByIdAndStatus("no-existe", InvitationStatus.PENDING))
                .thenReturn(Optional.empty());

        assertThrows(InvitationNotFoundException.class,
                () -> teamService.respondInvitation("no-existe", InvitationStatus.ACCEPTED));
    }

    @Test
    void respondInvitation_throwsWhenIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.respondInvitation(null, InvitationStatus.ACCEPTED));
    }

    // --- validateTeamComposition ---

    @Test
    void validateTeamComposition_returnsTrueWhenBetween7And12() {
        when(teamPlayerRepository.findByTeamId("team-1"))
                .thenReturn(List.of(
                        new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity(),
                        new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity(),
                        new TeamPlayerEntity()
                ));

        assertTrue(teamService.validateTeamComposition("team-1"));
    }

    @Test
    void validateTeamComposition_returnsFalseWhenLessThan7() {
        when(teamPlayerRepository.findByTeamId("team-1"))
                .thenReturn(List.of(new TeamPlayerEntity(), new TeamPlayerEntity()));

        assertFalse(teamService.validateTeamComposition("team-1"));
    }

    @Test
    void validateTeamComposition_returnsFalseWhenMoreThan12() {
        when(teamPlayerRepository.findByTeamId("team-1"))
                .thenReturn(List.of(
                        new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity(),
                        new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity(),
                        new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity(),
                        new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity(),
                        new TeamPlayerEntity()
                ));

        assertFalse(teamService.validateTeamComposition("team-1"));
    }

    @Test
    void validateTeamComposition_throwsWhenTeamIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.validateTeamComposition(null));
    }

    // --- validatePlayerUniquePerTournament ---

    @Test
    void validatePlayerUniquePerTournament_returnsTrueWhenNotExists() {
        when(teamPlayerRepository.existsByPlayerIdAndTournamentId("p1", "t1")).thenReturn(false);

        assertTrue(teamService.validatePlayerUniquePerTournament("p1", "t1"));
    }

    @Test
    void validatePlayerUniquePerTournament_returnsFalseWhenExists() {
        when(teamPlayerRepository.existsByPlayerIdAndTournamentId("p1", "t1")).thenReturn(true);

        assertFalse(teamService.validatePlayerUniquePerTournament("p1", "t1"));
    }

    @Test
    void validatePlayerUniquePerTournament_throwsWhenPlayerIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.validatePlayerUniquePerTournament(null, "t1"));
    }

    @Test
    void validatePlayerUniquePerTournament_throwsWhenTournamentIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.validatePlayerUniquePerTournament("p1", null));
    }

    // --- getTeamById ---

    @Test
    void getTeamById_returnsTeamWhenFound() {
        when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));

        Team result = teamService.getTeamById("team-1");

        assertNotNull(result);
        assertEquals("Los Tigres", result.getName());
    }

    @Test
    void getTeamById_throwsWhenNotFound() {
        when(teamRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class,
                () -> teamService.getTeamById("no-existe"));
    }

    @Test
    void getTeamById_throwsWhenIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.getTeamById(null));
    }

    // --- getAllTeams ---

    @Test
    void getAllTeams_returnsListWhenTeamsExist() {
        when(teamRepository.findAll()).thenReturn(List.of(teamEntity));

        List<Team> result = teamService.getAllTeams();

        assertFalse(result.isEmpty());
    }

    @Test
    void getAllTeams_returnsEmptyListWhenNoTeams() {
        when(teamRepository.findAll()).thenReturn(List.of());

        List<Team> result = teamService.getAllTeams();

        assertTrue(result.isEmpty());
    }

    // --- validateEngineeringMajority ---

    @Test
    void validateEngineeringMajority_throwsWhenTeamIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.validateEngineeringMajority(null));
    }

    @Test
    void validateEngineeringMajority_throwsWhenNoPlayers() {
        when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());

        assertThrows(InvalidInputException.class,
                () -> teamService.validateEngineeringMajority("team-1"));
    }
}