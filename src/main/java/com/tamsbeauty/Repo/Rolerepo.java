package com.tamsbeauty.Repo;

import com.tamsbeauty.Entity.Role;
import com.tamsbeauty.Entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Rolerepo extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName roleUser);
}
