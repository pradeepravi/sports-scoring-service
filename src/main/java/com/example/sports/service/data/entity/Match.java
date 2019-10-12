package com.example.sports.service.data.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="tennis_match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @OneToMany(mappedBy="match")
    private Set<MatchSet> matchSets;

    @Column(name="player1")
    private String player1;

    @Column(name="player2")
    private String player2;

    public Match(){}
    public Match(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Set<MatchSet> getMatchSets() {
        return matchSets;
    }

    public void setMatchSets(Set<MatchSet> matchSets) {
        this.matchSets = matchSets;
    }
}
