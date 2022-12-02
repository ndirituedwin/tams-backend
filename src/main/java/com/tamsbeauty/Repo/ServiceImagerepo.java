package com.tamsbeauty.Repo;

import com.tamsbeauty.Entity.ServiceImage;
import com.tamsbeauty.Entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceImagerepo extends JpaRepository<ServiceImage,Long> {
    void deleteAllByService(Services services);
}
