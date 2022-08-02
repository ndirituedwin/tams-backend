package com.cardgame.Repo;

import com.cardgame.Entity.User;
import com.cardgame.Entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCardRepo extends JpaRepository<UserCard,Long> {
    List<UserCard> findAllByUser(User user);
}
