package com.cardgame.Repo;

import com.cardgame.Entity.Changingwalletbalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Changingwalletbalancerepo extends JpaRepository<Changingwalletbalance,Long> {
}
