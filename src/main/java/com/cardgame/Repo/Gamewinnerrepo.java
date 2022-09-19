package com.cardgame.Repo;

import com.cardgame.Entity.Gamewinner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Gamewinnerrepo extends JpaRepository<Gamewinner,Long> {
}
