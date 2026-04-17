package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.TournamentFinalizedException;
import com.escuela.techcup.core.exception.TournamentNotFoundException;
import com.escuela.techcup.core.exception.TournamentNotActiveException;
import com.escuela.techcup.core.exception.TournamentOverlapException;
import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.core.model.enums.CanchaTipo;
import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;
import com.escuela.techcup.persistence.entity.users.OrganizerEntity;
import com.escuela.techcup.persistence.repository.tournament.TournamentRepository;
import com.escuela.techcup.persistence.repository.users.OrganizerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceImplTest {

    @Mock private TournamentRepository tournamentRepository;
    @Mock private OrganizerRepository organizerRepository;

    @InjectMocks private TournamentServiceImpl tournamentService;

    private TournamentEntity tournamentEntity;
    private OrganizerEntity organizerEntity;
    private final LocalDateTime start = LocalDateTime.of(2026, 6, 1, 0, 0);
    private final LocalDateTime end   = LocalDateTime.of(2026, 7, 1, 0, 0);


    @BeforeEach
    void setUp() {
        organizerEntity = new OrganizerEntity();
        organizerEntity.setId("org-1");

        tournamentEntity = new TournamentEntity();
        tournamentEntity.setId("tour-1");
        tournamentEntity.setStartDate(start);
        tournamentEntity.setEndDate(end);
        tournamentEntity.setTeamsMaxAmount(8);
        tournamentEntity.setTeamCost(50.0);
        tournamentEntity.setStatus(TournamentStatus.ACTIVE);
        tournamentEntity.setOrganizer(organizerEntity);
    }

    // --- createTournament ---

    @Test
    void createTournament_returnsWhenValid() {
        when(tournamentRepository.findAll()).thenReturn(List.of());
        when(organizerRepository.findById("org-1")).thenReturn(Optional.of(organizerEntity));
        when(tournamentRepository.save(any())).thenReturn(tournamentEntity);

        Tournament result = tournamentService.createTournament(start, end, 8, 50.0, TournamentStatus.DRAFT, "org-1");

        assertNotNull(result);
    }

    @Test
    void createTournament_throwsWhenEndBeforeStart() {
        assertThrows(InvalidInputException.class,
                () -> tournamentService.createTournament(end, start, 8, 50.0, TournamentStatus.DRAFT, "org-1"));
    }

    @Test
    void createTournament_throwsWhenTeamsMaxAmountLessThan2() {
        assertThrows(InvalidInputException.class,
                () -> tournamentService.createTournament(start, end, 1, 50.0, TournamentStatus.DRAFT, "org-1"));
    }

    @Test
    void createTournament_throwsWhenDatesOverlap() {
        TournamentEntity existing = new TournamentEntity();
        existing.setStartDate(start.minusDays(5));
        existing.setEndDate(end.plusDays(5));
        when(tournamentRepository.findAll()).thenReturn(List.of(existing));

        assertThrows(TournamentOverlapException.class,
                () -> tournamentService.createTournament(start, end, 8, 50.0, TournamentStatus.DRAFT, "org-1"));
    }

    @Test
    void createTournament_throwsWhenOrganizerNotFound() {
        when(tournamentRepository.findAll()).thenReturn(List.of());
        when(organizerRepository.findById("org-1")).thenReturn(Optional.empty());

        assertThrows(InvalidInputException.class,
                () -> tournamentService.createTournament(start, end, 8, 50.0, TournamentStatus.DRAFT, "org-1"));
    }

    // --- getTournamentById ---

    @Test
    void getTournamentById_returnsWhenFound() {
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));

        Tournament result = tournamentService.getTournamentById("tour-1");

        assertNotNull(result);
        assertEquals("tour-1", result.getId());
    }

    @Test
    void getTournamentById_throwsWhenNotFound() {
        when(tournamentRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(TournamentNotFoundException.class,
                () -> tournamentService.getTournamentById("no-existe"));
    }

    @Test
    void getTournamentById_throwsWhenIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> tournamentService.getTournamentById(null));
    }

    // --- getAllTournaments ---

    @Test
    void getAllTournaments_returnsList() {
        when(tournamentRepository.findAll()).thenReturn(List.of(tournamentEntity));

        List<Tournament> result = tournamentService.getAllTournaments();

        assertFalse(result.isEmpty());
    }

    @Test
    void getAllTournaments_returnsEmptyList() {
        when(tournamentRepository.findAll()).thenReturn(List.of());

        assertTrue(tournamentService.getAllTournaments().isEmpty());
    }

    // --- updateTournament ---

    @Test
    void updateTournament_updatesWhenValid() {
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));
        when(tournamentRepository.save(any())).thenReturn(tournamentEntity);

        Tournament result = tournamentService.updateTournament("tour-1", null, null, 0, null, TournamentStatus.IN_PROGRESS);

        assertNotNull(result);
    }

    @Test
    void updateTournament_throwsWhenFinalized() {
        tournamentEntity.setStatus(TournamentStatus.COMPLETED);
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));

        assertThrows(TournamentFinalizedException.class,
                () -> tournamentService.updateTournament("tour-1", null, null, 0, null, null));
    }

    @Test
    void updateTournament_throwsWhenNotFound() {
        when(tournamentRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(TournamentNotFoundException.class,
                () -> tournamentService.updateTournament("no-existe", null, null, 0, null, null));
    }

    // --- finalizeTournament ---

    @Test
    void finalizeTournament_setsStatusCompleted() {
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));

        tournamentService.finalizeTournament("tour-1");

        verify(tournamentRepository).save(argThat(t -> t.getStatus() == TournamentStatus.COMPLETED));
    }

    @Test
    void finalizeTournament_throwsWhenAlreadyFinalized() {
        tournamentEntity.setStatus(TournamentStatus.COMPLETED);
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));

        assertThrows(TournamentFinalizedException.class,
                () -> tournamentService.finalizeTournament("tour-1"));
    }

    @Test
    void finalizeTournament_throwsWhenNotFound() {
        when(tournamentRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(TournamentNotFoundException.class,
                () -> tournamentService.finalizeTournament("no-existe"));
    }

    // --- configureTournament ---

    @Test
    void configureTournament_savesWhenValid() {
        LocalDateTime closing = start.minusDays(1);
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));
        when(tournamentRepository.save(any())).thenReturn(tournamentEntity);

        Tournament result = tournamentService.configureTournament(
                "tour-1", "Reglamento X", closing, null);

        assertNotNull(result);
        verify(tournamentRepository).save(any());
    }



    @Test
    void configureTournament_throwsWhenClosingDateAfterStart() {
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));

        assertThrows(InvalidInputException.class,
                () -> tournamentService.configureTournament("tour-1", "Reglamento", end.plusDays(1), null));
    }

    @Test
    void configureTournament_throwsWhenFinalized() {
        tournamentEntity.setStatus(TournamentStatus.COMPLETED);
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));

        assertThrows(TournamentFinalizedException.class,
                () -> tournamentService.configureTournament("tour-1", "Reglamento", start.minusDays(1), null));
    }

    // --- getActiveTournament ---

    @Test
    void getActiveTournament_returnsWhenExists() {
        when(tournamentRepository.findByStatus(TournamentStatus.ACTIVE))
                .thenReturn(List.of(tournamentEntity));

        Tournament result = tournamentService.getActiveTournament();

        assertNotNull(result);
        assertEquals(TournamentStatus.ACTIVE, result.getStatus());
    }

    @Test
    void getActiveTournament_throwsWhenNone() {
        when(tournamentRepository.findByStatus(TournamentStatus.ACTIVE)).thenReturn(List.of());

        assertThrows(TournamentNotActiveException.class,
                () -> tournamentService.getActiveTournament());
    }

    // --- addCancha ---

    @Test
    void addCancha_addsWithDefaultNameWhenNombreIsNull() {
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));
        when(tournamentRepository.save(any())).thenReturn(tournamentEntity);

        Tournament result = tournamentService.addCancha("tour-1", CanchaTipo.CANCHA_1, null);

        assertNotNull(result);
        verify(tournamentRepository).save(any());
    }

    @Test
    void addCancha_addsWithCustomNombre() {
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));
        when(tournamentRepository.save(any())).thenReturn(tournamentEntity);

        Tournament result = tournamentService.addCancha("tour-1", CanchaTipo.CANCHA_2, "Mi Cancha");

        assertNotNull(result);
    }

    @Test
    void addCancha_throwsWhenTournamentNotFound() {
        when(tournamentRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(TournamentNotFoundException.class,
                () -> tournamentService.addCancha("no-existe", CanchaTipo.CANCHA_1, null));
    }

    @Test
    void addCancha_throwsWhenFinalized() {
        tournamentEntity.setStatus(TournamentStatus.COMPLETED);
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));

        assertThrows(TournamentFinalizedException.class,
                () -> tournamentService.addCancha("tour-1", CanchaTipo.CANCHA_1, null));
    }

    // --- addHorario ---

    @Test
    void addHorario_addsSuccessfully() {
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));
        when(tournamentRepository.save(any())).thenReturn(tournamentEntity);

        Tournament result = tournamentService.addHorario("tour-1", LocalDate.of(2026, 5, 10), "Jornada 1");

        assertNotNull(result);
        verify(tournamentRepository).save(any());
    }

    @Test
    void addHorario_throwsWhenTournamentNotFound() {
        when(tournamentRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(TournamentNotFoundException.class,
                () -> tournamentService.addHorario("no-existe", LocalDate.of(2026, 5, 10), "Jornada 1"));
    }

    @Test
    void addHorario_throwsWhenFinalized() {
        tournamentEntity.setStatus(TournamentStatus.COMPLETED);
        when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));

        assertThrows(TournamentFinalizedException.class,
                () -> tournamentService.addHorario("tour-1", LocalDate.of(2026, 5, 10), "Jornada 1"));
    }
}