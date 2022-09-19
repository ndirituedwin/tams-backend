package com.cardgame.Repo;

import com.cardgame.Entity.BuyIn;
import com.cardgame.Entity.GameRoomTable;
import com.cardgame.Entity.Gameroommaster;
import com.cardgame.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface Buyinrepo extends JpaRepository<BuyIn,Long> {
    boolean existsByUserAndGameroommaster(User user, Gameroommaster room);

    Optional<BuyIn> findByUserAndGameroommaster(User user, Gameroommaster room);

    Optional<BuyIn> findByIdAndUser(Long buyinid, User user);

    List<BuyIn> findAllByGameRoomTable(GameRoomTable gameRoomTable);


//    Option<BuyIn> findByIdAndUser(Long buyinid, User user);
}
