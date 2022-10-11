package com.cardgame.Repo;

import com.cardgame.Entity.User;
import com.cardgame.Entity.UserCard;
import com.cardgame.Entity.Userbestcard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Userbestcardrepo extends JpaRepository<Userbestcard,Long> {
    List<Userbestcard> findAllByUser(User user);


    Optional<Userbestcard> findByUserCardAndUser(UserCard userCard, User user);

    boolean existsByUserCard(UserCard userCard);

    Page<Userbestcard> findAllByUser(User user, Pageable pageable);
}
