package com.cardgame.Repo;

import com.cardgame.Entity.Pack;
import com.cardgame.Entity.Unopenedpack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Unopenedpackrepo extends JpaRepository<Unopenedpack,Long> {
    Optional<Unopenedpack> findByPack(Pack packget);

    boolean existsByPack(Pack pack1);
}
