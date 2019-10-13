package com.example.sports.service.data.repository;

import com.example.sports.service.data.entity.MatchSet;
import com.example.sports.service.data.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SetRepository extends JpaRepository<MatchSet, Long>  {

    @Query(" SELECT DISTINCT ms from MatchSet ms " +
            " WHERE ms.tennisMatch.id = :matchId " +
            " ORDER BY createdOn DESC" )
    List<MatchSet> findByMatchId(@Param("matchId") Long matchId);

    @Query("SELECT DISTINCT pointsWonBy as player, count(score) as wins from MatchSet ms " +
            " WHERE ms.tennisMatch.id = :matchId and ms.score in ( :scores )" +
            " GROUP BY pointsWonBy")
    List<Map<String, String>> findAllGroupByPointsWonBy(@Param("matchId") Long matchId, @Param("scores") List<Score> scores) ;

    @Query(value = " select * from " +
            "( SELECT points_won_by player, score, ROW_NUMBER()  OVER (PARTITION BY points_won_by ORDER BY created_on DESC ) as r " +
            " FROM match_set " +
            " WHERE set_id= :setId and match_id = :matchId) x " +
            " WHERE x.r =1 ", nativeQuery = true)
    List<Map<String, String>> findByNameMatchIdAndSetIdGroupByPointsWonBy(@Param("matchId") Long matchId, @Param("setId") int setId);

}
