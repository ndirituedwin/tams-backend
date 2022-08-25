package com.cardgame.Repo;

import com.cardgame.Entity.Unopenedpack;
import com.cardgame.Entity.User;
import com.cardgame.Entity.UserCard;
import com.cardgame.Entity.Usercardfee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface Usercardfeerepo extends JpaRepository<Usercardfee,Long> {
//    boolean existsByUsercard(Long id);
//
//    Optional<Usercardfee> findByUsercard(Long id);
//
//    boolean existsByUserAndUsercard(User user, Long id);
//
//
//    Optional<Usercardfee> findByusercard(Long id);



        Optional<Usercardfee> findByUserCard(UserCard userCard);

    boolean existsByUserAndUserCard(User user, UserCard userCard);

    boolean existsByUserCard(UserCard userCard);

    boolean existsByUserCardAndStatus(UserCard userCard, String active);

    Optional<Usercardfee> findByUserCardAndStatus(UserCard userCard, String active);

    boolean existsByUser(User user);

    Optional<Usercardfee> findByUserAndUserCard(User user, UserCard userCard);

    boolean existsByUserAndStatus(User user, String active);

    Optional<Usercardfee> findByUserAndStatus(User user, String active);

    List<Usercardfee> findAllByUserCardAndStatus(UserCard userCard, String active);

    List<Usercardfee> findAllByUserCard(UserCard userCard);

    boolean existsByUserAndUserCardAndStatus(User user, UserCard userCard, String active);
}
