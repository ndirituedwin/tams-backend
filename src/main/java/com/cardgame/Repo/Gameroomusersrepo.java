//package com.cardgame.Repo;
//
//import com.cardgame.Entity.GameRoom;
//import com.cardgame.Entity.Gameroomusers;
//import com.cardgame.Entity.User;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface Gameroomusersrepo extends JpaRepository<Gameroomusers,Long> {
//
//
//    Optional<Gameroomusers> findByUser(User uid);
//
//    void deleteByUserAndGameRoom(User uid, GameRoom gameRoom);
//
//    boolean existsByUserAndGameRoom(User uid, GameRoom gameRoom);
//
//    Page<Gameroomusers> findAllByGameRoom(GameRoom gameRoom, Pageable pageable);
//
//    boolean existsByUser(User user);
//}
