package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.Captain;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamPlayerEntity;
import com.escuela.techcup.persistence.mapper.users.PlayerMapper;

import java.awt.image.BufferedImage;
import java.util.UUID;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public class TeamMapper {

    private TeamMapper() {}

    public static Team toModel(TeamEntity entity) {
        if (entity == null) return null;
        
        // Convert logo from byte[] to BufferedImage
        BufferedImage logo = null;
        if (entity.getLogo() != null && entity.getLogo().length > 0) {
            try {
                logo = ImageIO.read(new ByteArrayInputStream(entity.getLogo()));
            } catch (IOException e) {
                logo = null;
            }
        }
        
        Team team = new Team(
                entity.getId().toString(),
                entity.getName(),
                entity.getUniformColor(),
                logo,
                entity.getFormation()
        );
        
        // Map captain
        if (entity.getCaptainPlayer() != null) {
            Player player = PlayerMapper.toModel(entity.getCaptainPlayer());
            Captain captain = new Captain(player);
            team.setCaptain(captain);
        }
        
        // Map players
        if (entity.getPlayers() != null) {
            List<Player> players = entity.getPlayers().stream()
                    .filter(tpe -> tpe.getPlayer() != null)
                    .map(tpe -> PlayerMapper.toModel(tpe.getPlayer()))
                    .collect(Collectors.toList());
            team.setPlayers(players);
        }
        
        return team;
    }

    public static TeamEntity toEntity(Team model) {
        if (model == null) return null;
        TeamEntity entity = new TeamEntity();
        entity.setId(UUID.fromString(model.getId()));
        entity.setName(model.getName());
        entity.setUniformColor(model.getUniformColor());
        entity.setFormation(model.getFormation());
        
        // Map logo from BufferedImage to byte[]
        if (model.getLogo() != null) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(model.getLogo(), "png", baos);
                entity.setLogo(baos.toByteArray());
            } catch (IOException e) {
                entity.setLogo(new byte[0]);
            }
        } else {
            entity.setLogo(new byte[0]);
        }
        
        // Map captain
        if (model.getCaptain() != null) {
            Player captainPlayer = (Player) model.getCaptain().getComponentPlayer();
            entity.setCaptainPlayer(PlayerMapper.toEntity(captainPlayer));
        }
        
        // Map players
        if (model.getPlayers() != null) {
            List<TeamPlayerEntity> teamPlayers = model.getPlayers().stream()
                    .map(player -> {
                        TeamPlayerEntity tpe = new TeamPlayerEntity();
                        tpe.setTeam(entity);
                        tpe.setPlayer(PlayerMapper.toEntity(player));
                        return tpe;
                    })
                    .collect(Collectors.toList());
            entity.setPlayers(teamPlayers);
        }
        
        return entity;
    }
}