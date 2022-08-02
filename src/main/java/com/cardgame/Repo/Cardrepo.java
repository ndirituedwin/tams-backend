package com.cardgame.Repo;

import com.cardgame.Entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Cardrepo extends JpaRepository<Card,Long> {
}
