package com.tamsbeauty.Repo;

import com.tamsbeauty.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Userrepo extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);

    boolean existsByMobile(String mobile);

    Optional<User> findByUsernameOrMobile(String mobilenumber, String mobilenumber1);
}
