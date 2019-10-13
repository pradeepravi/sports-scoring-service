package com.example.sports.service.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "match_set")
public class MatchSet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private TennisMatch tennisMatch;

    @Column(name = "set_id")
    private int setId;

    @Column(name = "points_won_by")
    private String pointsWonBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "score")
    private Score score;

    @Column(name = "created_on")
    private OffsetDateTime createdOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TennisMatch getTennisMatch() {
        return tennisMatch;
    }

    public void setTennisMatch(TennisMatch tennisMatch) {
        this.tennisMatch = tennisMatch;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public String getPointsWonBy() {
        return pointsWonBy;
    }

    public void setPointsWonBy(String pointsWonBy) {
        this.pointsWonBy = pointsWonBy;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(OffsetDateTime createdOn) {
        this.createdOn = createdOn;
    }
}


