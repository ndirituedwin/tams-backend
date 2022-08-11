package com.cardgame.Repo;

import com.cardgame.Entity.Carduserchangetracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Carduserchangetrackingrepo extends JpaRepository<Carduserchangetracking,Long> {

}
