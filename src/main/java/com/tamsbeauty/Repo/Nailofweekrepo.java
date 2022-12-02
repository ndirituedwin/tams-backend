package com.tamsbeauty.Repo;

import com.tamsbeauty.Entity.WeekNail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Nailofweekrepo extends JpaRepository<WeekNail,Long> {
}
