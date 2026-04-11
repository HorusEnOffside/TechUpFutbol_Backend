package com.escuela.techcup.persistence.entity.tournament;

import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "card")
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int minute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerEntity player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchEntity match;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardType type;

    @Column(length = 255)
    private String description;

    public enum CardType { YELLOW, RED }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getMinute() { return minute; }
    public void setMinute(int minute) { this.minute = minute; }
    public PlayerEntity getPlayer() { return player; }
    public void setPlayer(PlayerEntity player) { this.player = player; }
    public MatchEntity getMatch() { return match; }
    public void setMatch(MatchEntity match) { this.match = match; }
    public CardType getType() { return type; }
    public void setType(CardType type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
