package com.example.sports.service.data.entity.repository;

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

  /*  @Query("SELECT DISTINCT ms FROM MatchSet ms ORDER BY createdOn DESC")
    Optional<MatchSet>  findByCreatedDateDescLimitOne();

    @Query(" SELECT DISTINCT ms FROM MatchSet ms " +
            " WHERE ms.match.id = :matchId " +
            " AND ms.setId = :setId " +
            " AND ms.pointsWonBy = :pointsWonBy " +
            " ORDER BY ms.createdOn DESC " )
    List<MatchSet> findByNameMatchIdAndSetId(@Param("pointsWonBy") String pointsWonBy, @Param("matchId") Long matchId,
                                             @Param("setId") int setId);*/

    @Query(" SELECT DISTINCT ms from MatchSet ms " +
            " WHERE ms.match.id = :matchId " +
            " ORDER BY createdOn DESC" )
    List<MatchSet> findByMatchId(@Param("matchId") Long matchId);

    @Query("SELECT DISTINCT pointsWonBy as player, count(score) as wins from MatchSet ms " +
            " WHERE ms.match.id = :matchId and ms.score in ( :scores )" +
            " GROUP BY pointsWonBy")
    List<Map<String, String>> findAllGroupByPointsWonBy(@Param("matchId") Long matchId, @Param("scores") List<Score> scores) ;

    @Query("SELECT DISTINCT pointsWonBy as player, score as score, count(score) as count from MatchSet ms " +
            " WHERE ms.match.id = :matchId and ms.score in ( :scores )" +
            " GROUP BY pointsWonBy, score")
    List<Map<String, String>> findAllGroupByPointsWonByAndScore(@Param("matchId") Long matchId, @Param("scores") List<Score> scores) ;
    //TODO Fix this to non Native
    @Query(value = " select * from " +
            "( SELECT points_won_by player, score, ROW_NUMBER()  OVER (PARTITION BY points_won_by ORDER BY created_on DESC ) as r " +
            " FROM match_set " +
            " WHERE set_id= :setId and match_id = :matchId) x " +
            " WHERE x.r =1 ", nativeQuery = true)
    List<Map<String, String>> findByNameMatchIdAndSetIdGroupByPointsWonBy(@Param("matchId") Long matchId, @Param("setId") int setId);

}
