package com.cardgame.Repo;

import com.cardgame.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Loginrepo extends JpaRepository<User,Long> {
}
