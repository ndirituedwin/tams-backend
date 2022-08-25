package com.cardgame.Repo;

import com.cardgame.Entity.BuyIn;
import com.cardgame.Entity.Gameroommaster;
import com.cardgame.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Buyinrepo extends JpaRepository<BuyIn,Long> {
    boolean existsByUserAndGameroommaster(User user, Gameroommaster room);

    Optional<BuyIn> findByUserAndGameroommaster(User user, Gameroommaster room);
}
