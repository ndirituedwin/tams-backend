package com.cardgame.Repo;

import com.cardgame.Entity.Boughtpacktracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Boughtpacktrackingrepo extends JpaRepository<Boughtpacktracking,Long> {
}
