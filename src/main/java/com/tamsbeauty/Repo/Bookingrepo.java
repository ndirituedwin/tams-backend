package com.tamsbeauty.Repo;

import com.tamsbeauty.Entity.Booking;
import com.tamsbeauty.Entity.Services;
import com.tamsbeauty.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Bookingrepo extends JpaRepository<Booking,Long> {
    boolean existsByUserAndServices(User user, Services service);
}
