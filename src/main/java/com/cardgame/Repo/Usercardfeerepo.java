package com.cardgame.Repo;

import com.cardgame.Entity.UserCard;
import com.cardgame.Entity.Usercardfee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface Usercardfeerepo extends JpaRepository<Usercardfee,Long> {
    Optional<Usercardfee> findByUserCard(UserCard userCard);
}
