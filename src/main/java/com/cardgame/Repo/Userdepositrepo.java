package com.cardgame.Repo;

import com.cardgame.Entity.Userdeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Userdepositrepo extends JpaRepository<Userdeposit,Long> {
    boolean existsByOrderidOrPaymentid(String orderid, String paymentid);
}
