package com.cardgame.Repo;

import com.cardgame.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Loginrepo extends JpaRepository<User,Long> {
    Optional<User> findByUID(Long uid);
}
