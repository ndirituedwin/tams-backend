package com.cardgame.Repo;

import com.cardgame.Entity.GameRoom;
import com.cardgame.Entity.Gameroomusers;
import com.cardgame.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Gameroomusersrepo extends JpaRepository<Gameroomusers,Long> {


    Optional<Gameroomusers> findByUserid(long uid);

    void deleteByUseridAndGameRoom(long uid, GameRoom gameRoom);

    boolean existsByUseridAndGameRoom(long uid, GameRoom gameRoom);
}
