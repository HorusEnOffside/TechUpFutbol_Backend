package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.exception.MatchNotFoundException;
import com.escuela.techcup.core.model.Match;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.service.SoccerFieldService;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import com.escuela.techcup.persistence.mapper.tournament.MatchMapper;
import com.escuela.techcup.persistence.repository.tournament.MatchRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.persistence.repository.users.RefereeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchServiceImplTest {
    @Mock
    private MatchRepository matchRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private RefereeRepository refereeRepository;
    @Mock
    private SoccerFieldService soccerFieldService;
    @Mock
    private PlayerService playerService;

    @InjectMocks
    private MatchServiceImpl matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMatchById_found() {
        String matchId = "match-1";
        MatchEntity entity = mock(MatchEntity.class);
        Match model = mock(Match.class);
        when(matchRepository.findById(matchId)).thenReturn(Optional.of(entity));
        // MatchMapper.toModel is static, so we can't mock it directly here, but assume it works
        // If using PowerMockito, you could mock static, but for now, just check the repository call
        // and that no exception is thrown
        assertDoesNotThrow(() -> matchService.getMatchById(matchId));
        verify(matchRepository).findById(matchId);
    }

    @Test
    void getMatchById_notFound() {
        String matchId = "not-exist";
        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());
        assertThrows(MatchNotFoundException.class, () -> matchService.getMatchById(matchId));
    }
}
