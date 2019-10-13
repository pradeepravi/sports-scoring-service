package com.example.sports.service.data.repository;

import com.example.sports.service.data.entity.TennisMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<TennisMatch, Long> {

}
