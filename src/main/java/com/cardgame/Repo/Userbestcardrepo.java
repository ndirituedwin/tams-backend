package com.cardgame.Repo;

import com.cardgame.Entity.User;
import com.cardgame.Entity.Userbestcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Userbestcardrepo extends JpaRepository<Userbestcard,Long> {
    List<Userbestcard> findAllByUser(User user);
}
