package com.cardgame.Repo;

import com.cardgame.Entity.Gameroommaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gameroommasterrepo extends JpaRepository<Gameroommaster,Long> {

    Page<Gameroommaster> findAllByStatusEquals(boolean b, Pageable pageable);
}
