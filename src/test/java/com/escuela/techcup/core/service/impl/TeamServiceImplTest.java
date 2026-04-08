package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.exception.*;
import com.escuela.techcup.core.model.Invitation;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.enums.Career;
import com.escuela.techcup.core.model.enums.InvitationStatus;
import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.persistence.entity.tournament.*;
import com.escuela.techcup.persistence.entity.users.*;
import com.escuela.techcup.persistence.repository.tournament.*;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import com.escuela.techcup.persistence.repository.users.UserRepository;

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

    @Mock private TeamRepository teamRepository;
    @Mock private TeamPlayerRepository teamPlayerRepository;
    @Mock private PlayerRepository playerRepository;
    @Mock private InvitationRepository invitationRepository;
    @Mock private TournamentRepository tournamentRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private TeamServiceImpl teamService;

    private TeamEntity teamEntity;
    private PlayerEntity playerEntity;
    private UserPlayerEntity userEntity;
    private TournamentEntity tournamentEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserPlayerEntity();
        userEntity.setId("user-1");

        playerEntity = new PlayerEntity();
        playerEntity.setId("player-1");
        playerEntity.setUser(userEntity);

        tournamentEntity = new TournamentEntity();
        tournamentEntity.setId("tour-1");
        tournamentEntity.setStatus(TournamentStatus.ACTIVE);

        teamEntity = new TeamEntity();
        teamEntity.setId("team-1");
        teamEntity.setName("Los Tigres");
        teamEntity.setUniformColor("Rojo y Negro");
        teamEntity.setTournament(tournamentEntity);
    }

    // --- createTeam ---

    @Test
    void createTeam_returnsTeamAndAssignsCaptain() {
        when(teamRepository.existsByNameIgnoreCase("Los Tigres")).thenReturn(false);
        when(tournamentRepository.findByStatus(TournamentStatus.ACTIVE)).thenReturn(List.of(tournamentEntity));
        when(playerRepository.findByUserId("user-1")).thenReturn(Optional.of(playerEntity));
        when(userRepository.save(any())).thenReturn(userEntity);
        when(teamRepository.save(any())).thenReturn(teamEntity);

        Team result = teamService.createTeam("Los Tigres", "Rojo y Negro", null, "user-1");

        assertNotNull(result);
        assertEquals("Los Tigres", result.getName());
        verify(teamPlayerRepository).save(any()); // capitán agregado como jugador
        verify(userRepository).save(any());        // rol CAPTAIN asignado
    }

    @Test
    void createTeam_throwsWhenNameIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.createTeam(null, "Rojo", null, "user-1"));
    }

    @Test
    void createTeam_throwsWhenNameIsBlank() {
        assertThrows(InvalidInputException.class,
                () -> teamService.createTeam("  ", "Rojo", null, "user-1"));
    }

    @Test
    void createTeam_throwsWhenNameTooShort() {
        assertThrows(InvalidInputException.class,
                () -> teamService.createTeam("Los", "Rojo", null, "user-1"));
    }

    @Test
    void createTeam_throwsWhenUniformColorsIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.createTeam("Los Tigres", null, null, "user-1"));
    }

    @Test
    void createTeam_throwsWhenCaptainIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.createTeam("Los Tigres", "Rojo", null, null));
    }

    @Test
    void createTeam_throwsWhenNameAlreadyExists() {
        when(teamRepository.existsByNameIgnoreCase("Los Tigres")).thenReturn(true);

        assertThrows(TeamAlreadyExistsException.class,
                () -> teamService.createTeam("Los Tigres", "Rojo", null, "user-1"));
    }

    @Test
    void createTeam_throwsWhenNoActiveTournament() {
        when(teamRepository.existsByNameIgnoreCase("Los Tigres")).thenReturn(false);
        when(tournamentRepository.findByStatus(TournamentStatus.ACTIVE)).thenReturn(List.of());

        assertThrows(TournamentNotActiveException.class,
                () -> teamService.createTeam("Los Tigres", "Rojo", null, "user-1"));
    }

    @Test
    void createTeam_throwsWhenCaptainPlayerNotFound() {
        when(teamRepository.existsByNameIgnoreCase("Los Tigres")).thenReturn(false);
        when(tournamentRepository.findByStatus(TournamentStatus.ACTIVE)).thenReturn(List.of(tournamentEntity));
        when(playerRepository.findByUserId("user-1")).thenReturn(Optional.empty());

        assertThrows(InvalidInputException.class,
                () -> teamService.createTeam("Los Tigres", "Rojo", null, "user-1"));
    }

    // --- invitePlayer ---

    @Test
    void invitePlayer_savesInvitationWhenValid() {
        when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
        when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
        when(invitationRepository.existsByTeamIdAndPlayerId("team-1", "player-1")).thenReturn(false);
        when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());

        teamService.invitePlayer("team-1", "player-1", "Únete");

        verify(invitationRepository).save(any());
    }

    @Test
    void invitePlayer_throwsWhenTeamIdIsNull() {
        assertThrows(InvalidInputException.class, () -> teamService.invitePlayer(null, "p1", "msg"));
    }

    @Test
    void invitePlayer_throwsWhenPlayerIdIsNull() {
        assertThrows(InvalidInputException.class, () -> teamService.invitePlayer("t1", null, "msg"));
    }

    @Test
    void invitePlayer_throwsWhenTeamNotFound() {
        when(teamRepository.findById("no-existe")).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class, () -> teamService.invitePlayer("no-existe", "p1", "msg"));
    }

    @Test
    void invitePlayer_throwsWhenPlayerAlreadyInvited() {
        when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
        when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
        when(invitationRepository.existsByTeamIdAndPlayerId("team-1", "player-1")).thenReturn(true);

        assertThrows(PlayerAlreadyInvitedException.class, () -> teamService.invitePlayer("team-1", "player-1", "msg"));
    }

    @Test
    void invitePlayer_throwsWhenTeamFull() {
        when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
        when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
        when(invitationRepository.existsByTeamIdAndPlayerId("team-1", "player-1")).thenReturn(false);
        when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(
                new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity(),
                new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity(),
                new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity(),
                new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity()
        ));

        assertThrows(InvalidInputException.class, () -> teamService.invitePlayer("team-1", "player-1", "msg"));
    }

    // --- respondInvitation ---

    @Test
    void respondInvitation_acceptsAndCreatesTeamPlayer() {
        InvitationEntity invitation = buildInvitation();
        when(invitationRepository.findByIdAndStatus("inv-1", InvitationStatus.PENDING))
                .thenReturn(Optional.of(invitation));

        teamService.respondInvitation("inv-1", InvitationStatus.ACCEPTED);

        verify(teamPlayerRepository).save(any());
        verify(userRepository).save(any()); // rol PLAYER asignado
        verify(invitationRepository).save(invitation);
    }

    @Test
    void respondInvitation_rejectsWithoutCreatingTeamPlayer() {
        InvitationEntity invitation = buildInvitation();
        when(invitationRepository.findByIdAndStatus("inv-1", InvitationStatus.PENDING))
                .thenReturn(Optional.of(invitation));

        teamService.respondInvitation("inv-1", InvitationStatus.REJECTED);

        verify(teamPlayerRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void respondInvitation_throwsWhenNotFound() {
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

    @Test
    void respondInvitation_throwsWhenActionIsNull() {
        assertThrows(InvalidInputException.class,
                () -> teamService.respondInvitation("inv-1", null));
    }

    // --- getInvitationsByPlayer ---

    @Test
    void getInvitationsByPlayer_returnsListWhenFound() {
        InvitationEntity inv = buildInvitation();
        when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
        when(invitationRepository.findByPlayerId("player-1")).thenReturn(List.of(inv));

        List<Invitation> result = teamService.getInvitationsByPlayer("player-1");

        assertEquals(1, result.size());
        assertEquals("inv-1", result.get(0).getId());
        assertEquals("team-1", result.get(0).getTeamId());
    }

    @Test
    void getInvitationsByPlayer_returnsEmptyWhenNone() {
        when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
        when(invitationRepository.findByPlayerId("player-1")).thenReturn(List.of());

        assertTrue(teamService.getInvitationsByPlayer("player-1").isEmpty());
    }

    @Test
    void getInvitationsByPlayer_throwsWhenPlayerIdIsNull() {
        assertThrows(InvalidInputException.class, () -> teamService.getInvitationsByPlayer(null));
    }

    @Test
    void getInvitationsByPlayer_throwsWhenPlayerNotFound() {
        when(playerRepository.findById("no-existe")).thenReturn(Optional.empty());
        assertThrows(InvalidInputException.class, () -> teamService.getInvitationsByPlayer("no-existe"));
    }

    // --- validateTeamComposition ---

    @Test
    void validateTeamComposition_returnsTrueWhenBetween7And12() {
        when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(
                new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity(),
                new TeamPlayerEntity(), new TeamPlayerEntity(), new TeamPlayerEntity(),
                new TeamPlayerEntity()
        ));
        assertTrue(teamService.validateTeamComposition("team-1"));
    }

    @Test
    void validateTeamComposition_returnsFalseWhenLessThan7() {
        when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(new TeamPlayerEntity()));
        assertFalse(teamService.validateTeamComposition("team-1"));
    }

    @Test
    void validateTeamComposition_returnsFalseWhenMoreThan12() {
        when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(
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
        assertThrows(InvalidInputException.class, () -> teamService.validateTeamComposition(null));
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
    void getTeamById_returnsWhenFound() {
        when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
        Team result = teamService.getTeamById("team-1");
        assertNotNull(result);
        assertEquals("Los Tigres", result.getName());
    }

    @Test
    void getTeamById_throwsWhenNotFound() {
        when(teamRepository.findById("no-existe")).thenReturn(Optional.empty());
        assertThrows(TeamNotFoundException.class, () -> teamService.getTeamById("no-existe"));
    }

    @Test
    void getTeamById_throwsWhenIdIsNull() {
        assertThrows(InvalidInputException.class, () -> teamService.getTeamById(null));
    }

    // --- getAllTeams ---

    @Test
    void getAllTeams_returnsList() {
        when(teamRepository.findAll()).thenReturn(List.of(teamEntity));
        assertFalse(teamService.getAllTeams().isEmpty());
    }

    @Test
    void getAllTeams_returnsEmptyList() {
        when(teamRepository.findAll()).thenReturn(List.of());
        assertTrue(teamService.getAllTeams().isEmpty());
    }

    // --- validateEngineeringMajority ---

    @Test
    void validateEngineeringMajority_throwsWhenNoPlayers() {
        when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
        assertThrows(InvalidInputException.class, () -> teamService.validateEngineeringMajority("team-1"));
    }

    @Test
    void validateEngineeringMajority_returnsTrueWhenMajorityEngineering() {
        StudentEntity s = new StudentEntity(); s.setCareer(Career.ENGINEERING);
        TeacherEntity t = new TeacherEntity(); t.setCareer(Career.DATA_SCIENCE);
        FamiliarEntity f = new FamiliarEntity();

        when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(
                buildTp(s), buildTp(t), buildTp(f)
        ));
        assertTrue(teamService.validateEngineeringMajority("team-1"));
    }

    @Test
    void validateEngineeringMajority_returnsFalseWhenMajorityNotEngineering() {
        StudentEntity s = new StudentEntity(); s.setCareer(Career.OTHER);
        FamiliarEntity f1 = new FamiliarEntity();
        FamiliarEntity f2 = new FamiliarEntity();

        when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(
                buildTp(s), buildTp(f1), buildTp(f2)
        ));
        assertFalse(teamService.validateEngineeringMajority("team-1"));
    }

    @Test
    void validateEngineeringMajority_throwsWhenTeamIdIsNull() {
        assertThrows(InvalidInputException.class, () -> teamService.validateEngineeringMajority(null));
    }

    // --- helpers ---

    private InvitationEntity buildInvitation() {
        InvitationEntity inv = new InvitationEntity();
        inv.setId("inv-1");
        inv.setTeam(teamEntity);
        inv.setPlayer(playerEntity);
        inv.setStatus(InvitationStatus.PENDING);
        return inv;
    }

    private TeamPlayerEntity buildTp(UserPlayerEntity userType) {
        PlayerEntity p = new PlayerEntity();
        p.setUser(userType);
        TeamPlayerEntity tp = new TeamPlayerEntity();
        tp.setPlayer(p);
        return tp;
    }
}