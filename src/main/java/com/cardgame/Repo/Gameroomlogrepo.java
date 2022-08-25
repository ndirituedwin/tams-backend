package com.cardgame.Repo;

import com.cardgame.Entity.Gameroomlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gameroomlogrepo extends JpaRepository<Gameroomlog,Long> {
}
