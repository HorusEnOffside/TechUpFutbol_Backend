package com.escuela.techcup.core.model;

import lombok.Data;
import java.util.List;

@Data
public class EliminationBracket {
    private List<MatchPair> roundOf16;
    private List<MatchPair> quarterFinals;
    private List<MatchPair> semiFinals;
    private List<MatchPair> finals;

    @Data
    public static class MatchPair {
        private Team team1;
        private Team team2;
    }
}
