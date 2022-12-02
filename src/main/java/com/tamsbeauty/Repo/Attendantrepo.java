package com.tamsbeauty.Repo;

import com.tamsbeauty.Entity.Attendant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Attendantrepo extends JpaRepository<Attendant,Long> {
    boolean existsByMobile(String mobile);
}
