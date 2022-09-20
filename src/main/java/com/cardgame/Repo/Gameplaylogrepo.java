package com.cardgame.Repo;

import com.cardgame.Entity.Gameplaylog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Gameplaylogrepo extends JpaRepository<Gameplaylog,Long> {
}
