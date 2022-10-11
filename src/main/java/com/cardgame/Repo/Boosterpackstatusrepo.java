package com.cardgame.Repo;

import com.cardgame.Entity.Boosterpackstatus;
import com.cardgame.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Boosterpackstatusrepo extends JpaRepository<Boosterpackstatus,Long> {
    boolean existsByUser(User user);

    Optional<Boosterpackstatus> findByUser(User user);
}
