//package com.cardgame.config;
//
//import com.cardgame.Entity.Gameroommaster;
//import com.cardgame.Repo.Gameroommasterrepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//
//@Component
//public class Gameroomsampledata implements CommandLineRunner {
//
//    @Autowired
//    private Gameroommasterrepo gameroommasterrepo;
//
//    @Override
//    public void run(String... args) throws Exception {
//        gameroommasterrepo.save(new Gameroommaster(5,new BigDecimal(150), Instant.now(),true));
//        gameroommasterrepo.save(new Gameroommaster(5,new BigDecimal(200), Instant.now(),true));
//        gameroommasterrepo.save(new Gameroommaster(5,new BigDecimal(250), Instant.now(),true));
//    }
//}
