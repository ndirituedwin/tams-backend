package com.cardgame.Repo;

import com.cardgame.Entity.CardMarketplacelog;
import com.cardgame.Entity.PackMarketplacelog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackdMarketplacelogrepo extends JpaRepository<PackMarketplacelog,Long> {
}
