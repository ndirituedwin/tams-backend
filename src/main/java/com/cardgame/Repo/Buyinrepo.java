package com.cardgame.Repo;

import com.cardgame.Entity.BuyIn;
import com.cardgame.Entity.GameRoom;
import com.cardgame.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Buyinrepo extends JpaRepository<BuyIn,Long> {
    boolean existsByUserAndGameRoom(User user, GameRoom room);

    Optional<BuyIn> findByUserAndGameRoom(User user, GameRoom room);
}
