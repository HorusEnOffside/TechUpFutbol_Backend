package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamPlayerEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;
import com.escuela.techcup.persistence.mapper.payment.PaymentMapper;
import com.escuela.techcup.persistence.mapper.tournament.TournamentMapper;
import com.escuela.techcup.persistence.mapper.users.PlayerMapper;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.Captain;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.ComponentPlayer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class TeamMapper {

    private TeamMapper() {
    }


    public static TeamEntity toEntity(Team team) {
        if (team == null) return null;
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setId(team.getId());
        teamEntity.setName(team.getName());
        teamEntity.setLogo(toPngBytes(team.getLogo()));
        teamEntity.setUniformColor(colorToHex(team.getUniformColor()));
        teamEntity.setFormation(team.getFormation());
        if (team.getCaptain() != null) {
            teamEntity.setCaptainPlayer(PlayerMapper.toEntity(team.getCaptain()));
        } else {
            teamEntity.setCaptainPlayer(null);
        }
        if (team.getPayment() != null) {
            teamEntity.setPayment(PaymentMapper.toEntity(team.getPayment()));
        } else {
            teamEntity.setPayment(null);
        }
        if (team.getTournament() != null) {
            teamEntity.setTournament(TournamentMapper.toEntity(team.getTournament()));
        } else {
            teamEntity.setTournament(null);
        }
        if (team.getPlayers() != null) {
            List<TeamPlayerEntity> teamPlayerEntities = new ArrayList<>();
            for (Player player : team.getPlayers()) {
                TeamPlayerEntity tpe = new TeamPlayerEntity();
                tpe.setPlayer(PlayerMapper.toEntity(player));
                tpe.setTeam(teamEntity);
                teamPlayerEntities.add(tpe);
            }
            teamEntity.setPlayers(teamPlayerEntities);
        } else {
            teamEntity.setPlayers(null);
        }
        return teamEntity;
    }

    public static Team toModel(TeamEntity teamEntity) {
        if (teamEntity == null) return null;
        Team team = new Team(
                teamEntity.getId(),
                teamEntity.getName(),
                toBufferedImage(teamEntity.getLogo()),
                hexToColor(teamEntity.getUniformColor()),
                teamEntity.getFormation()
        );
        if (teamEntity.getCaptainPlayer() != null) {
            Captain captain = new Captain(PlayerMapper.toModel(teamEntity.getCaptainPlayer()));
            team.setCaptain(captain);
        } else {
            team.setCaptain(null);
        }
        if (teamEntity.getPayment() != null) {
            team.setPayment(PaymentMapper.toModel(teamEntity.getPayment()));
        } else {
            team.setPayment(null);
        }
        if (teamEntity.getTournament() != null) {
            team.setTournament(TournamentMapper.toModel(teamEntity.getTournament()));
        } else {
            team.setTournament(null);
        }
        if (teamEntity.getPlayers() != null) {
            List<Player> players = new ArrayList<>();
            for (TeamPlayerEntity tpe : teamEntity.getPlayers()) {
                if (tpe.getPlayer() != null) {
                    players.add(PlayerMapper.toModel(tpe.getPlayer()));
                }
            }
            team.setPlayers(players);
        } else {
            team.setPlayers(null);
        }
        return team;
    }

    private static byte[] toPngBytes(BufferedImage image) {
        if (image == null) {
            return new byte[0];
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private static BufferedImage toBufferedImage(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            return null;
        }
    }

    private static String colorToHex(Color color) {
        if (color == null) return null;
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private static Color hexToColor(String hex) {
        if (hex == null) return null;
        return Color.decode(hex);
    }
}
