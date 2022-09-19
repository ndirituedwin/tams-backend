//package com.cardgame.Dto.requests.gamelogic.nnnn;
//
//import java.util.Arrays;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class tx {
//
//    log.info("The player 140 cards {} player 199 {}",cardPairingtest.getUid140cards(),cardPairingtest.getUid199cards());
//    Integer[] uid140=new Integer[5];
//    Integer[] uid199=new Integer[5];
//    AtomicInteger a= new AtomicInteger();
//    AtomicInteger b= new AtomicInteger();
//    String[] uid140cards=cardPairingtest.getUid140cards().split(",");
//    String[] uid199cards=cardPairingtest.getUid199cards().split(",");
//      Arrays.stream(uid140cards).forEach(s -> uid140[a.getAndIncrement()]= Integer.valueOf(s));
//      Arrays.stream(uid199cards).forEach(s -> uid199[b.getAndIncrement()]= Integer.valueOf(s));
//      log.info("The INteger arrays 140 {} 199 {}",uid);
//}
//
//
//
//
//package com.cardgame.Dto.requests.gamelogic;
//
//
//        import com.cardgame.Service.RoomService;
//
//        import java.util.*;
//        import java.util.concurrent.atomic.AtomicInteger;
//        import java.util.stream.IntStream;
//
//        import static com.cardgame.config.ApiUtils.PLAYER_ONE;
//        import static com.cardgame.config.ApiUtils.PLAYER_TWO;
//
//public class CardPairingtest {
//
//
//    private String uid140cards;
//    private String uid199cards;
//
//
//    public CardPairingtest(String uid140cards, String uid199cards) {
//        this.uid140cards = uid140cards;
//        this.uid199cards = uid199cards;
//    }
//
//    public String getUid140cards() {
//        return uid140cards;
//    }
//
//    public void setUid140cards(String uid140cards) {
//        this.uid140cards = uid140cards;
//    }
//
//    public String getUid199cards() {
//        return uid199cards;
//    }
//
//    public void setUid199cards(String uid199cards) {
//        this.uid199cards = uid199cards;
//    }
//}
//
