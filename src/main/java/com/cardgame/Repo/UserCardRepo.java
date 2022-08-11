package com.cardgame.Repo;

import com.cardgame.Entity.Pack;
import com.cardgame.Entity.User;
import com.cardgame.Entity.UserCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCardRepo extends JpaRepository<UserCard,Long> {
    List<UserCard> findAllByUser(User user);

    List<UserCard> findAllByPack(Pack pack1);

    List<UserCard> findAllByPackAndUser(Pack pack1, User user);

    Optional<UserCard> findByPack(Pack pack1);


    @Query("SELECT V FROM UserCard V WHERE V.user =:user ")
    Page<UserCard> findAllByUserrr(User user, Pageable pageable);


}