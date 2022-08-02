package com.cardgame.Repo;

import com.cardgame.Entity.Userbestcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Userbestcardrepo extends JpaRepository<Userbestcard,Long> {
}
