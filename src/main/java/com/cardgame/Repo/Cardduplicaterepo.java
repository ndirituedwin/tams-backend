package com.cardgame.Repo;

import com.cardgame.Entity.Cardduplicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Cardduplicaterepo extends JpaRepository<Cardduplicate,Long> {
    List<Cardduplicate> findAllByIsTakenEquals(boolean b);
}
