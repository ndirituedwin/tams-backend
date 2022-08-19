package com.cardgame.Service;

import com.cardgame.Dto.requests.*;
import com.cardgame.Dto.responses.*;
import com.cardgame.Dto.responses.Page.PagedResponse;
import com.cardgame.Entity.*;
import com.cardgame.Exceptions.CardNotFoundException;
import com.cardgame.Exceptions.GameNotFoundException;
import com.cardgame.Exceptions.UserNotFoundException;
import com.cardgame.Exceptions.UserWalletNotFoundException;
import com.cardgame.Repo.*;
import com.cardgame.Service.mapper.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Transactional
@Slf4j
@Service
public class RoomService {

    private final Loginrepo loginrepo;
    private final GameRoomrepo gameRoomrepo;
    private final Gameroomusersrepo gameroomusersrepo;
    private final Userbestcardrepo userbestcardrepo;
    private final CardService cardService;
    private final Buyinrepo buyinrepo;
    private final Userwalletrepo userwalletrepo;

    public RoomService(Loginrepo loginrepo, GameRoomrepo gameRoomrepo, Gameroomusersrepo gameroomusersrepo, Userbestcardrepo userbestcardrepo, CardService cardService, Buyinrepo buyinrepo, Userwalletrepo userwalletrepo) {
        this.loginrepo = loginrepo;

        this.gameRoomrepo = gameRoomrepo;
        this.gameroomusersrepo = gameroomusersrepo;
        this.userbestcardrepo = userbestcardrepo;
        this.cardService = cardService;
        this.buyinrepo = buyinrepo;
        this.userwalletrepo = userwalletrepo;
    }


//    public JoinRoomResponse joinRoom1(Roomjoinrequest roomjoinrequest){
public ResponseEntity<?> joinRoom1(Roomjoinrequest roomjoinrequest){

        log.info("roomjoinrequest get amount, gameroomid,user id  {}    {}    {}",roomjoinrequest.getAmount(),roomjoinrequest.getGameroomid(),roomjoinrequest.getUserid());

    try {

//           BigDecimal bigDecimal=new BigDecimal(roomjoinrequest.getMinimumamount());
////           log.info("bigdecima {}",bigDecimal);
        User user = loginrepo.findByUID(roomjoinrequest.getUserid()).orElseThrow(() -> new UserNotFoundException("user with the provided uid not found"));
        Userwallet userwallet = userwalletrepo.findByUserid(user.getUID()).orElseThrow(() -> new UserWalletNotFoundException("user wallet nt found"));
        if (roomjoinrequest.getAmount().compareTo(userwallet.getTotalwalletbalance()) == -1) {
        List<GameRoom> gameroomList = gameRoomrepo.findAll();
        if (gameroomList.isEmpty()) {
            GameRoom gameRoom = new GameRoom();
            gameRoom.setStatus(true);
//               gameRoom.setMinimumamount(new BigDecimal(roomjoinrequest.getMinimumamount()));
            gameRoom.setMinimumamount(roomjoinrequest.getAmount());
            gameRoom.setCreateddate(Instant.now());

            gameRoom.setNumberofusers(gameRoom.getNumberofusers() + 1);
            GameRoom savedgameroom = gameRoomrepo.save(gameRoom);
            Gameroomusers gameroomusers = new Gameroomusers();
            gameroomusers.setGameRoom(savedgameroom);
            gameroomusers.setUserid(user.getUID() + 1L);
            gameroomusersrepo.save(gameroomusers);
        } else {

            List<GameRoom> gameroomList1 = gameRoomrepo.findAll();
            GameRoom agam = gameroomList1.stream()
//                       .peek(gameRoom -> log.info("gameroom{} {} {} {}",gameRoom.getCreateddate(),gameRoom.getId(),gameRoom.getNumberofusers(),gameRoom.getMinimumamount()))
//                       .filter(gameRoom -> gameRoom.getNumberofusers()<5 && gameRoom.getMinimumamount().toString().split("[.]")[0].equals(roomjoinrequest.getMinimumamount()))
                    .filter(gameRoom -> gameRoom.getNumberofusers() < 5)
                    .findFirst()
                    .orElse(null);
            if (agam == null) {
                GameRoom gameRoom1 = new GameRoom();
                gameRoom1.setStatus(true);
//                   gameRoom1.setMinimumamount(new BigDecimal(roomjoinrequest.getMinimumamount()));
                gameRoom1.setMinimumamount(roomjoinrequest.getAmount());
                gameRoom1.setCreateddate(Instant.now());

                gameRoom1.setNumberofusers(gameRoom1.getNumberofusers() + 1);
                GameRoom savedgameroom1 = gameRoomrepo.save(gameRoom1);
                Gameroomusers gameroomusers1 = new Gameroomusers();
                gameroomusers1.setGameRoom(savedgameroom1);
                gameroomusers1.setUserid(user.getUID());
                gameroomusersrepo.save(gameroomusers1);
//                   log.info("here instead");
            } else {
                Gameroomusers gameroomusers = new Gameroomusers();
                gameroomusers.setUserid(user.getUID());
                gameroomusers.setGameRoom(agam);
                Long useridd = gameroomusersrepo.save(gameroomusers).getUserid();
                agam.setNumberofusers(agam.getNumberofusers() + 1);
                Integer nou = gameRoomrepo.save(agam).getNumberofusers();
                /**buy in**/

                try {
//                       log.info("goes through here {}",roomjoinrequest);

                    if (roomjoinrequest.getGameroomid() == null || roomjoinrequest.getAmount() == null) {
//                           return new ro("one of the values of the request body is null");
                    }
                    User user_not_foun = loginrepo.findByUID(useridd).orElseThrow(() -> new UserNotFoundException("user not foun"));
                    GameRoom room = gameRoomrepo.findById(agam.getId()).orElseThrow(() -> new GameNotFoundException("Game room not found"));
                    boolean existsbyuseridandgameroomid = buyinrepo.existsByUserAndGameRoom(user, room);
                    if (existsbyuseridandgameroomid) {
                        System.out.println("Buy in exists buy the user and game room ");
//                           return new BUyinresponse("buy in exists buy the user and game room ");
                    }
                    BuyIn buyIn = new BuyIn();
                    buyIn.setGameRoom(room);
                    buyIn.setUser(user_not_foun);
                    buyIn.setCreatedDate(Instant.now());
                    buyIn.setAmount(roomjoinrequest.getAmount());
                    buyinrepo.save(buyIn);
                    System.out.println("BUy in of amount " + roomjoinrequest.getAmount() + " and for the user " + user_not_foun.getUsername() + " saved successfully!");
//                       return new BUyinresponse("BUy in of amount "+buyinrequest.getAmount()+" and for the user "+user.getUsername()+" saved successfully!");
                } catch (Exception e) {
                    return ResponseEntity.ok("An exception has occurred while saving buyin " + e.getMessage());

//                       return new BUyinresponse("An exception has occurred while saving buyin "+e.getMessage());
                }
                /**end*/
//                   log.info("agam {} {} {}",agam.getId(),agam.getCreateddate(),agam.getNumberofusers());
            }

        }
    }

//           return new JoinRoomResponse("game room saven");
        return ResponseEntity.ok(this.userbestthirtycards(roomjoinrequest.getUserid()));


    }catch (Exception e){
        return ResponseEntity.ok("an exception has occurred when joining game room "+e.getMessage());
//           return new JoinRoomResponse("an exception has occurred when joining game room "+e.getMessage());
    }
}

   @Transactional
    public ResponseEntity<?> joinRoom(Roomjoinrequest roomjoinrequest){


        try {


//            check if the game room exists.If it does not exists no moving on
            GameRoom gameRoom=gameRoomrepo.findById(roomjoinrequest.getGameroomid()).orElseThrow(() -> new GameNotFoundException("game room not found"));
//        check if user exists
            User user=loginrepo.findByUID(roomjoinrequest.getUserid()).orElseThrow(() -> new UserNotFoundException("user with the provided uid not found"));
//            getting user wallet balance
            Userwallet userwallet=userwalletrepo.findByUserid(user.getUID()).orElseThrow(() -> new UserWalletNotFoundException("user wallet nt found"));
//            check if the walet balance is greater than wamount user wants to deposit.
            if (roomjoinrequest.getAmount().compareTo(userwallet.getTotalwalletbalance())==-1) {
//              update game room status
                gameRoom.setStatus(true);
                gameRoom.setNumberofusers(gameRoom.getNumberofusers() + 1);
                gameRoomrepo.save(gameRoom);
                List<GameRoom> gameroomList1 = gameRoomrepo.findAll();
                GameRoom agam = gameroomList1.stream()
//                       .peek(gameRoom -> log.info("gameroom{} {} {} {}",gameRoom.getCreateddate(),gameRoom.getId(),gameRoom.getNumberofusers(),gameRoom.getMinimumamount()))
//                       .filter(gameRoom -> gameRoom.getNumberofusers()<5 && gameRoom.getMinimumamount().toString().split("[.]")[0].equals(roomjoinrequest.getMinimumamount()))
                        .filter(gameRoom2 -> gameRoom2.getNumberofusers() < 5)
                        .findFirst()
                        .orElse(null);
                if (agam == null) {
                    return ResponseEntity.ok("All game rooms are full!!");
                } else {
                    //              check game room users if there is a user with same game room and same user id
                    boolean existsByUseridAndGameRoom = gameroomusersrepo.existsByUseridAndGameRoom(user, gameRoom);
                    if (existsByUseridAndGameRoom) {
                    return ResponseEntity.ok("You cannot join the game room twice ");
                    } else {

//                     if game room user does not existwith same user id and game room
                    Gameroomusers gameroomusers = new Gameroomusers();
                    gameroomusers.setUserid(user.getUID());
                    gameroomusers.setGameRoom(agam);
                    Long useridd = gameroomusersrepo.save(gameroomusers).getUserid();
                    agam.setNumberofusers(agam.getNumberofusers() + 1);
                    Integer nou = gameRoomrepo.save(agam).getNumberofusers();


                    try {
//                       log.info("goes through here {}",roomjoinrequest);

                        if (roomjoinrequest.getGameroomid() == null || roomjoinrequest.getAmount() == null) {
                           return ResponseEntity.ok("one of the values of the request body is null");
                        }
//                        User user_not_foun = loginrepo.findByUID(useridd).orElseThrow(() -> new UserNotFoundException("user not foun"));
                        GameRoom room = gameRoomrepo.findById(agam.getId()).orElseThrow(() -> new GameNotFoundException("Game room not found"));
                        boolean existsbyuseridandgameroomid = buyinrepo.existsByUserAndGameRoom(user, room);
                        if (existsbyuseridandgameroomid) {
                            System.out.println("Buy in exists buy the user and game room ");
//                           return new BUyinresponse("buy in exists buy the user and game room ");
                        }
                        BuyIn buyIn = new BuyIn();
                        buyIn.setGameRoom(room);
                        buyIn.setUser(user);
                        buyIn.setCreatedDate(Instant.now());
                        buyIn.setAmount(roomjoinrequest.getAmount());
                        buyinrepo.save(buyIn);
                        userwallet.setTotalwalletbalance(userwallet.getTotalwalletbalance().subtract(roomjoinrequest.getAmount()));
                        userwalletrepo.save(userwallet);
                          System.out.println("BUy in of amount " + roomjoinrequest.getAmount() + " and for the user " + user.getUsername() + " saved successfully!");
//                       return new BUyinresponse("BUy in of amount "+buyinrequest.getAmount()+" and for the user "+user.getUsername()+" saved successfully!");
                    } catch (Exception e) {
                        return ResponseEntity.ok("An exception has occurred while saving buyin " + e.getMessage());

//                       return new BUyinresponse("An exception has occurred while saving buyin "+e.getMessage());
                    }

                }
            }


            }else{
                return ResponseEntity.ok("You do not have enough money on your wallet to join the game room, your balance is: "+userwallet.getTotalwalletbalance());
            }
//           return new JoinRoomResponse("game room saven");
            return ResponseEntity.ok(this.userbestthirtycards(roomjoinrequest.getUserid()));


        }catch (Exception e){
            return ResponseEntity.ok("an exception has occurred when joining game room "+e.getMessage());
//           return new JoinRoomResponse("an exception has occurred when joining game room "+e.getMessage());
        }
    }



    public JoinRoomResponse leaveroom(LeaveRoomRequest leaveRoomRequest) {

        User user=loginrepo.findByUID(leaveRoomRequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));

        Gameroomusers gameroomusers=gameroomusersrepo.findByUserid(user.getUID()).orElseThrow(() -> new CardNotFoundException("game room not fund"));
//        log.info("game room users {}",gameroomusers);
        try {
            GameRoom gameRoom=gameroomusers.getGameRoom();
//            log.info("game room {}",gameRoom);
            gameroomusersrepo.deleteByUseridAndGameRoom(user.getUID(),gameRoom);
            gameRoom.setNumberofusers(gameRoom.getNumberofusers()-1);
            gameRoomrepo.save(gameRoom);
            return new JoinRoomResponse("you have left the room");
        }catch (Exception e){

            return new JoinRoomResponse("An error has occurred while leaving the room "+e.getMessage());
        }
    }


    public Object userbestthirtycards(Long uid) {

        if (uid==null){
            return "the uid is null";
        }
        User user=loginrepo.findByUID(uid).orElseThrow(() -> new UserNotFoundException("user not found"));
        try {
            List<Userbestcard> userbestthirtycardsList=userbestcardrepo.findAllByUser(user);
            if (userbestthirtycardsList.isEmpty()){
                return ResponseEntity.ok(new Userbestthirtycards("you have no best cards"));
            }else{
//               return ResponseEntity.ok(userbestthirtycardsList);
               return userbestthirtycardsList;
            }

        }catch (Exception e){
//            return ResponseEntity.ok( new Userbestthirtycards("an error has occurred while getting user bes cards "+e.getMessage()));
            return new Userbestthirtycards("an error has occurred while getting user bes cards "+e.getMessage());
        }

    }

    public PagedResponse<GameRoomResponse> gamerooms(int page, int size) {
          cardService.validatePagenumberandSize(page,size);
          try {
              Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
              Page<GameRoom> gameRooms = gameRoomrepo.findAllByStatusEquals(true,pageable);
//              log.info("usercards {}",gameRooms);


              if (gameRooms.getNumberOfElements() == 0) {
                  return new PagedResponse<>(Collections.emptyList(), gameRooms.getNumber(), gameRooms.getSize(), gameRooms.getTotalElements(), gameRooms.getTotalPages(), gameRooms.isLast());
              }
              List<GameRoomResponse> gameroomsresponses = gameRooms.map(ModelMapper::mapgameroomtogameroomresponse).getContent();
//              log.info("logging gameroomsresponses {}", gameroomsresponses);
              return new PagedResponse<>(gameroomsresponses, gameRooms.getNumber(), gameRooms.getSize(), gameRooms.getTotalElements(), gameRooms.getTotalPages(), gameRooms.isLast());

          }catch (Exception e){
              throw new RuntimeException("en exception has occurred while fetching rooms "+e.getMessage());
          }

    }

    public BUyinresponse buyin(Buyinrequest buyinrequest) {

//        log.info("buyin request {}",buyinrequest);
        if (buyinrequest==null){
            return new BUyinresponse("the request body may not be null");
        }
        if (buyinrequest.getGameroomid()==null|| buyinrequest.getAmount()==null || buyinrequest.getUserid()==null){
            return new BUyinresponse("one of the values of the request body is null");
        }

        try {
            User user=loginrepo.findByUID(buyinrequest.getUserid()).orElseThrow(() -> new UserNotFoundException("user not foun"));
            GameRoom room=gameRoomrepo.findById(buyinrequest.getGameroomid()).orElseThrow(() -> new GameNotFoundException("Game room not found"));
            boolean existsbyuseridandgameroomid=buyinrepo.existsByUserAndGameRoom(user,room);
            if (existsbyuseridandgameroomid){
                return new BUyinresponse("buy in exists buy the user and game room ");
            }
            BuyIn buyIn=new BuyIn();
            buyIn.setGameRoom(room);
            buyIn.setUser(user);
            buyIn.setCreatedDate(Instant.now());
            buyIn.setAmount(buyinrequest.getAmount());
            buyinrepo.save(buyIn);
            return new BUyinresponse("BUy in of amount "+buyinrequest.getAmount()+" and for the user "+user.getUsername()+" saved successfully!");
        }catch (Exception e){
            return new BUyinresponse("An exception has occurred while saving buyin "+e.getMessage());
        }

    }

    public Getuserbuyinresponse getuserbuyin(UserBuyInRequest userBuyInRequest) {

        if (userBuyInRequest.getUserid()==null||userBuyInRequest.getGameroomid()==null){
            return new Getuserbuyinresponse("provide enough details");

        }
        try {

            User user=loginrepo.findByUID(userBuyInRequest.getUserid()).orElseThrow(() -> new UserNotFoundException("user not foun"));
            GameRoom room=gameRoomrepo.findById(userBuyInRequest.getGameroomid()).orElseThrow(() -> new GameNotFoundException("Game room not found"));
            boolean existsbyuseridandgameroomid=buyinrepo.existsByUserAndGameRoom(user,room);
            if (existsbyuseridandgameroomid){
                BuyIn buyIn=buyinrepo.findByUserAndGameRoom(user,room).orElseThrow(() -> new GameNotFoundException("BUy in not found"));
                return new Getuserbuyinresponse(buyIn.getUser().getId(),buyIn.getGameRoom().getId(),buyIn.getAmount(),buyIn.getId());
            }
            return new Getuserbuyinresponse("No details found for that user with that game room");
        }catch (Exception e){
            return new Getuserbuyinresponse("An error has occurred while fetching user buy in details "+e.getMessage());
        }
    }

    public Object creategameroom(Creategameroomrequest creategameroomrequest) {

        if (creategameroomrequest.getMinimumamount() == null || creategameroomrequest.getMinimumamount().compareTo(BigDecimal.ZERO) < 1) {
            return "Invalid minimum amount entered";
        }

        log.info("the create game room request   {}", creategameroomrequest.getMinimumamount());
        GameRoom gameRoom = new GameRoom();
        if (!(creategameroomrequest.getGameroomid() == null || creategameroomrequest.getGameroomid() == 0)) {
        log.info("eddd");
        boolean existsbygameid = gameRoomrepo.existsById(creategameroomrequest.getGameroomid());
        if (existsbygameid) {
            log.info("Dddddddd");
            GameRoom existinggameroom = gameRoomrepo.findById(creategameroomrequest.getGameroomid()).orElseThrow(() -> new GameNotFoundException("Game room not found"));
            existinggameroom.setNumberofusers(existinggameroom.getNumberofusers());
            existinggameroom.setMinimumamount(creategameroomrequest.getMinimumamount());
            gameRoomrepo.save(existinggameroom);
        }else{

        }
    }else{
            gameRoom.setCreateddate(Instant.now());
            gameRoom.setStatus(false);
            gameRoom.setNumberofusers(0);
            gameRoom.setMinimumamount(creategameroomrequest.getMinimumamount());
            return gameRoomrepo.save(gameRoom);
        }

        return "game room saved";
    }
}

