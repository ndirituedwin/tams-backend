package com.cardgame.Repo;

import com.cardgame.Entity.PackPricelisting;
import com.cardgame.Entity.Unopenedpack;
import com.cardgame.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Packfeelistingrepo extends JpaRepository<PackPricelisting,Long> {
    Optional<PackPricelisting> findByUnopenedpack(Unopenedpack unopenedpack);

    boolean existsByUnopenedpack(Unopenedpack unopenedpack);

    boolean existsByUserAndUnopenedpack(User user, Unopenedpack unopenedpack);

    boolean existsByUnopenedpackAndStatus(Unopenedpack unopenedpack, String active);

    Optional<PackPricelisting> findByUnopenedpackAndStatus(Unopenedpack unopenedpack, String active);
}
