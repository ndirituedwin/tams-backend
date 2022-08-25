package com.cardgame.Repo;

import com.cardgame.Entity.CardMarketplacelog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardMarketplacelogrepo extends JpaRepository<CardMarketplacelog,Long> {
}
