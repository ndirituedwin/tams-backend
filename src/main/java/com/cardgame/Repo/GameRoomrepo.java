package com.cardgame.Repo;

import com.cardgame.Entity.GameRoom;
import com.cardgame.Entity.Gameroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoomrepo extends JpaRepository<GameRoom,Long> {


}
