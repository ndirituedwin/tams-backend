package com.cardgame.Repo;

import com.cardgame.Entity.Userwallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Userwalletrepo extends JpaRepository<Userwallet,Long> {
    Optional<Userwallet> findByUserid(long id);

    boolean existsByUserid(long id);


}