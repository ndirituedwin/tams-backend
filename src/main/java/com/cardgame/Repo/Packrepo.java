package com.cardgame.Repo;

import com.cardgame.Entity.Pack;
import com.cardgame.Entity.UserCard;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Packrepo extends JpaRepository<Pack,Long> {


}
