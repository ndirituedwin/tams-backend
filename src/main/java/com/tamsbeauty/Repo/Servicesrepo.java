package com.tamsbeauty.Repo;

import com.tamsbeauty.Entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Servicesrepo extends JpaRepository<Services,Long> {

    boolean existsByDescription(String description);

    List<Services> findAllByOrderByIdDesc();
}
