package com.cardgame.Repo;

import com.cardgame.Entity.PackPricelisting;
import com.cardgame.Entity.Unopenedpack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Packfeelistingrepo extends JpaRepository<PackPricelisting,Long> {
    Optional<PackPricelisting> findByPack(Unopenedpack unopenedpack);
}
