package com.cardgame.Repo;

import com.cardgame.Entity.Playerbestcardlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Playerbestcardlogrepo extends JpaRepository<Playerbestcardlog,Long> {
}
