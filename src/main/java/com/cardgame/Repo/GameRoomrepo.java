package com.cardgame.Repo;

import com.cardgame.Entity.GameRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface GameRoomrepo extends JpaRepository<GameRoom,Long> {


    Page<GameRoom> findAllByStatusEquals(boolean b, Pageable pageable);

    boolean existsById(BigDecimal minimumamount);

    Optional<GameRoom> findById(BigDecimal minimumamount);
}
