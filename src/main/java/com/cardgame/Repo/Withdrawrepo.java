package com.cardgame.Repo;


import com.cardgame.Entity.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Withdrawrepo extends JpaRepository<Withdrawal,Long> {
}
