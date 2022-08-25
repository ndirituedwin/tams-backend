package com.cardgame.Repo;

import com.cardgame.Entity.GameRoomTable;
import com.cardgame.Entity.Gameroommaster;
import com.cardgame.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Gameroomtablerepo extends JpaRepository<GameRoomTable,Long> {
//    boolean existsByUser(User user);

//    Optional<GameRoomTable> findByUser(User user);

//    void deleteByUser(User user);


    Page<GameRoomTable> findAllByGameroommaster(Gameroommaster gameRoom, Pageable pageable);

    boolean existsByUidoneOrUidtwoOrUidthreeOrUidfourOrUidfive(Long userid, Long userid1, Long userid2, Long userid3, Long userid4);


//    @Query("SELECT V FROM UserCard V WHERE V.user =:user ")


}
