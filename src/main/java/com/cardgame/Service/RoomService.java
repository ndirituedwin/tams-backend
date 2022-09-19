package com.cardgame.Service;

import com.cardgame.Dto.requests.*;
import com.cardgame.Dto.requests.gamelogic.CardPairingtest;
import com.cardgame.Dto.requests.gamelogic.Winninghandrequest;
import com.cardgame.Dto.requests.gamelogic.Winninghandresponse;
import com.cardgame.Dto.responses.*;
import com.cardgame.Dto.responses.Page.PagedResponse;
import com.cardgame.Entity.*;
import com.cardgame.Exceptions.*;
import com.cardgame.Repo.*;
import com.cardgame.Service.mapper.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.cardgame.config.ApiUtils.*;
import static com.cardgame.config.ApiUtils.PLAYER_TWO;

@Transactional
@Slf4j
@Service
public class RoomService {

    private final Userrepo userrepo;
//    private final GameRoomrepo gameRoomrepo;
//    private final Gameroomusersrepo gameroomusersrepo;
    private final Userbestcardrepo userbestcardrepo;
    private final CardService cardService;
    private final Buyinrepo buyinrepo;
    private final Userwalletrepo userwalletrepo;
    private final Gameroommasterrepo gameroommasterrepo;
    private final Gameroomtablerepo gameroomtablerepo;
    private final Gameroomlogrepo gameroomlogrepo;
    private final WebSocketService webSocketService;
    private final  UserCardRepo userCardRepo;

    public RoomService(Userrepo userrepo, Userbestcardrepo userbestcardrepo, CardService cardService, Buyinrepo buyinrepo, Userwalletrepo userwalletrepo, Gameroommasterrepo gameroommasterrepo, Gameroomtablerepo gameroomtablerepo, Gameroomlogrepo gameroomlogrepo, WebSocketService webSocketService, UserCardRepo userCardRepo) {
        this.userrepo = userrepo;
        this.userbestcardrepo = userbestcardrepo;
        this.cardService = cardService;
        this.buyinrepo = buyinrepo;
        this.userwalletrepo = userwalletrepo;
        this.gameroommasterrepo = gameroommasterrepo;
        this.gameroomtablerepo = gameroomtablerepo;
        this.gameroomlogrepo = gameroomlogrepo;
        this.webSocketService = webSocketService;
        this.userCardRepo = userCardRepo;
    }


    @Transactional
    public ResponseEntity<?> joinRoom(Roomjoinrequest roomjoinrequest) {
        if (roomjoinrequest.getMastertableid() == null || roomjoinrequest.getUserid() == null) {
            return ResponseEntity.ok("one of the values of the request body is null"+"::"+"false");
        }

        try {
            User user = userrepo.findById(roomjoinrequest.getUserid()).orElseThrow(() -> new UserNotFoundException("user with the provided uid not found"));
            Userwallet userwallet = userwalletrepo.findByUser(user).orElseThrow(() -> new UserWalletNotFoundException("user wallet nt found"));
            Gameroommaster gameroommaster=gameroommasterrepo.findById(roomjoinrequest.getMastertableid()).orElseThrow(() -> new GameNotFoundException("Game lobby with the given id could not be found"));

            boolean check=gameroomtablerepo.existsByUidoneOrUidtwoOrUidthreeOrUidfourOrUidfive(roomjoinrequest.getUserid(),roomjoinrequest.getUserid(),roomjoinrequest.getUserid(),roomjoinrequest.getUserid(),roomjoinrequest.getUserid());
           if (userwallet.getTotalwalletbalance().compareTo(gameroommaster.getAmount()) >0){
              if (check){
                   return new ResponseEntity<>("You can not join the room twice"+"::"+"false",HttpStatus.BAD_REQUEST);
               }else{
                   List<GameRoomTable> gameRoomTables=gameroomtablerepo.findAll();
                   if (gameRoomTables.isEmpty()){
                       GameRoomTable gameRoomTable1=new GameRoomTable();
                       gameRoomTable1.setUidone(user.getUID());
                       gameRoomTable1.setGameroommaster(gameroommaster);
                       gameRoomTable1.setNumberofusers(1);
                       GameRoomTable   savedgameRoomTable=gameroomtablerepo.save(gameRoomTable1);

                       Gameroomlog gameroomlog=new Gameroomlog();
                       gameroomlog.setCreateddate(Instant.now());
                       gameroomlog.setUser(user);
                       gameroomlog.setGameroommaster(gameroommaster);
                       gameroomlog.setGameRoomTable(savedgameRoomTable);
                       gameroomlog.setNumberofparticipants(savedgameRoomTable.getNumberofusers());
                       gameroomlog.setAction(JOINED_GAME_ROOM);
                       Gameroomlog savedgameroomlog=gameroomlogrepo.save(gameroomlog);

                       webSocketService.notifyFrontend();
                       return new ResponseEntity<>("you have joined the room "+"::"+"true"+"::"+savedgameRoomTable.getId()+"::"+savedgameRoomTable.getGameroommaster().getId()+"::"+savedgameRoomTable.getNumberofusers(),HttpStatus.OK);

                   }else{
                       List<GameRoomTable> gameRoomTabless=gameroomtablerepo.findAll();


                       GameRoomTable gameRoomTable = gameRoomTabless.stream()
                               .filter(gameRoom -> gameRoom.getNumberofusers() < 5 && Objects.equals(gameRoom.getGameroommaster().getId(), roomjoinrequest.getMastertableid()))
                               .findFirst()
                               .orElse(null);
                       System.out.println("the value is here "+gameRoomTable);

                       if (gameRoomTable==null){
                           System.out.println("will execute here instead cox is null ");

                           GameRoomTable gameRoomTable1=new GameRoomTable();
                           gameRoomTable1.setUidone(user.getUID());
                           gameRoomTable1.setGameroommaster(gameroommaster);
                           gameRoomTable1.setNumberofusers(1);
                           GameRoomTable savedgameRoomTable=gameroomtablerepo.save(gameRoomTable1);

                           Gameroomlog gameroomlog11=new Gameroomlog();
                           gameroomlog11.setCreateddate(Instant.now());
                           gameroomlog11.setUser(user);
                           gameroomlog11.setGameroommaster(gameroommaster);
                           gameroomlog11.setGameRoomTable(savedgameRoomTable);
                           gameroomlog11.setNumberofparticipants(savedgameRoomTable.getNumberofusers());
                           gameroomlog11.setAction(JOINED_GAME_ROOM);
                           Gameroomlog savedgameroomlog11=gameroomlogrepo.save(gameroomlog11);

                           webSocketService.notifyFrontend();
                           return new ResponseEntity<>("you have joined the room "+"::"+"true"+"::"+savedgameRoomTable.getId()+"::"+savedgameRoomTable.getGameroommaster().getId()+"::"+savedgameRoomTable.getNumberofusers(),HttpStatus.OK);
                       }else{
                           System.out.println("will execute here instead ");
;
                           List<String> longs=new ArrayList<>();

                           longs.add(gameRoomTable.getUidone() !=null?gameRoomTable.getUidone()+"":"");
                           longs.add(gameRoomTable.getUidtwo() !=null?gameRoomTable.getUidtwo()+"":"");
                           longs.add(gameRoomTable.getUidthree() !=null?gameRoomTable.getUidthree()+"":"");
                           longs.add(gameRoomTable.getUidfour() !=null?gameRoomTable.getUidfour()+"":"");
                           longs.add(gameRoomTable.getUidfive() !=null?gameRoomTable.getUidfive()+"":"");
//                           log.info("the longs {}", longs);
                           String longone=longs.stream().filter(s -> s.equals("")).findFirst().orElseThrow(null);

                           longs.set(longs.indexOf(longone),roomjoinrequest.getUserid()+"");
                           gameRoomTable.setNumberofusers(gameRoomTable.getNumberofusers()+1);
                           gameRoomTable.setGameroommaster(gameroommaster);
                           gameRoomTable.setUidone(Objects.equals(longs.get(0), "") ?null: Long.valueOf(longs.get(0)));
                           gameRoomTable.setUidtwo(Objects.equals(longs.get(1), "") ?null: Long.valueOf(longs.get(1)));
                           gameRoomTable.setUidthree(Objects.equals(longs.get(2), "") ?null: Long.valueOf(longs.get(2)));
                           gameRoomTable.setUidfour(Objects.equals(longs.get(3), "") ?null: Long.valueOf(longs.get(3)));
                           gameRoomTable.setUidfive(Objects.equals(longs.get(4), "") ?null: Long.valueOf(longs.get(4)));


                           /**BUy IN**/
                           Gameroomlog gameroomlog22=new Gameroomlog();
                           gameroomlog22.setCreateddate(Instant.now());
                           gameroomlog22.setUser(user);
                           gameroomlog22.setGameroommaster(gameroommaster);
                           gameroomlog22.setGameRoomTable(gameRoomTable);
                           gameroomlog22.setNumberofparticipants(gameRoomTable.getNumberofusers());
                           gameroomlog22.setAction(JOINED_GAME_ROOM);
                           Gameroomlog savedgameroomlog=gameroomlogrepo.save(gameroomlog22);
//                           return new  ResponseEntity<>("game room saved"+"::"+"true",HttpStatus.CREATED);

                           webSocketService.notifyFrontend();
                           return ResponseEntity.ok("game room saved"+"::"+"true"+"::"+gameRoomTable.getId()+"::"+gameRoomTable.getGameroommaster().getId()+"::"+gameRoomTable.getNumberofusers());
                       }
                   }

               }
           }else{
               return new ResponseEntity<>("You do not have enough amount to enter into the game room your wallet balance is "+userwallet.getTotalwalletbalance()+".The minimum amount required to join this room is "+gameroommaster.getAmount()+"::"+"false",HttpStatus.BAD_REQUEST);
           }


//           Long id=savedgameRoomTable.getId();

//             log.info("the data {}",savedgameRoomTable.getId());
//            return ResponseEntity.ok("game room saved"+"::"+"true"+"::"+savedgameRoomTable.getId()+"::"+savedgameRoomTable.getGameroommaster().getId());
//            return ResponseEntity.ok("game room saved"+"::"+"true");
//            return new  ResponseEntity<>("game room saved"+"::"+"true",HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An exception has occurred trying to save game room " + e.getMessage()+"::"+"false", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }



    public JoinRoomResponse leaveroom(LeaveRoomRequest leaveRoomRequest) {


//        log.info("game room users {}",gameroomusers);
        try {
            User user = userrepo.findById(leaveRoomRequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));

            List<GameRoomTable> gameRoomTables=gameroomtablerepo.findAll();
            for (GameRoomTable gameRoomTable:gameRoomTables){
                if (gameRoomTable.getUidone() !=null && gameRoomTable.getUidone() .equals(user.getUID())){
                    gameRoomTable.setUidone(null);
                    gameRoomTable.setNumberofusers(gameRoomTable.getNumberofusers()-1);
                    Gameroomlog gameroomlog=new Gameroomlog();

                    gameroomlog.setCreateddate(Instant.now());
                    gameroomlog.setUser(user);
                    gameroomlog.setGameroommaster(gameRoomTable.getGameroommaster());
                    gameroomlog.setGameRoomTable(gameRoomTable);
                    gameroomlog.setNumberofparticipants(gameRoomTable.getNumberofusers());
                    gameroomlog.setAction(LEFT_GAME_ROOM);
                    Gameroomlog savedgameroomlog=gameroomlogrepo.save(gameroomlog);

                    break;
                } else if (gameRoomTable.getUidtwo()!=null && gameRoomTable.getUidtwo().equals(user.getUID())) {
                    gameRoomTable.setUidtwo(null);
                    gameRoomTable.setNumberofusers(gameRoomTable.getNumberofusers()-1);

                    Gameroomlog gameroomlog=new Gameroomlog();
                    gameroomlog.setCreateddate(Instant.now());
                    gameroomlog.setUser(user);
                    gameroomlog.setGameroommaster(gameRoomTable.getGameroommaster());
                    gameroomlog.setGameRoomTable(gameRoomTable);
                    gameroomlog.setNumberofparticipants(gameRoomTable.getNumberofusers());
                    gameroomlog.setAction(LEFT_GAME_ROOM);

                    Gameroomlog savedgameroomlog=gameroomlogrepo.save(gameroomlog);
                    break;
                } else if (gameRoomTable.getUidthree() !=null && gameRoomTable.getUidthree().equals(user.getUID())) {
                    gameRoomTable.setUidthree(null);
                    gameRoomTable.setNumberofusers(gameRoomTable.getNumberofusers()-1);

                    Gameroomlog gameroomlog=new Gameroomlog();
                    gameroomlog.setCreateddate(Instant.now());
                    gameroomlog.setUser(user);
                    gameroomlog.setGameroommaster(gameRoomTable.getGameroommaster());
                    gameroomlog.setGameRoomTable(gameRoomTable);
                    gameroomlog.setNumberofparticipants(gameRoomTable.getNumberofusers());
                    gameroomlog.setAction(LEFT_GAME_ROOM);

                    Gameroomlog savedgameroomlog=gameroomlogrepo.save(gameroomlog);
                    break;
                } else if (gameRoomTable.getUidfour() !=null && gameRoomTable.getUidfour().equals(user.getUID())) {
                    gameRoomTable.setUidfour(null);
                    gameRoomTable.setNumberofusers(gameRoomTable.getNumberofusers()-1);

                    Gameroomlog gameroomlog=new Gameroomlog();
                    gameroomlog.setCreateddate(Instant.now());
                    gameroomlog.setUser(user);
                    gameroomlog.setGameroommaster(gameRoomTable.getGameroommaster());
                    gameroomlog.setGameRoomTable(gameRoomTable);
                    gameroomlog.setNumberofparticipants(gameRoomTable.getNumberofusers());
                    gameroomlog.setAction(LEFT_GAME_ROOM);

                    Gameroomlog savedgameroomlog=gameroomlogrepo.save(gameroomlog);
                    break;

                } else if (gameRoomTable.getUidfive() !=null && gameRoomTable.getUidfive().equals(user.getUID())) {
                    gameRoomTable.setUidfive(null);
                    gameRoomTable.setNumberofusers(gameRoomTable.getNumberofusers()-1);

                    Gameroomlog gameroomlog=new Gameroomlog();
                    gameroomlog.setCreateddate(Instant.now());
                    gameroomlog.setUser(user);
                    gameroomlog.setGameroommaster(gameRoomTable.getGameroommaster());
                    gameroomlog.setGameRoomTable(gameRoomTable);
                    gameroomlog.setNumberofparticipants(gameRoomTable.getNumberofusers());
                    gameroomlog.setAction(LEFT_GAME_ROOM);

                    Gameroomlog savedgameroomlog=gameroomlogrepo.save(gameroomlog);
                    break;

                }
            }



            return new JoinRoomResponse("you have left the room");
        } catch (Exception e) {

            return new JoinRoomResponse("An error has occurred while leaving the room " + e.getMessage());
        }
    }


    public Object userbestthirtycards(GetUserBest30cardsrequest getUserBest30cardsrequest) {

        if (getUserBest30cardsrequest.getUid() == null) {
            return "the uid is null";
        }
        try {
            User user = userrepo.findById(getUserBest30cardsrequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));
            List<Userbestcard> userbestthirtycardsList = userbestcardrepo.findAllByUser(user);
            if (userbestthirtycardsList.isEmpty()) {
                return ResponseEntity.ok(new Userbestthirtycards("you have no best cards"));
            } else {
//               return ResponseEntity.ok(userbestthirtycardsList);
                return userbestthirtycardsList;
            }

        } catch (Exception e) {
            return new Userbestthirtycards("an error has occurred while getting user bes cards " + e.getMessage());
        }

    }
    public Object allusersbestthirtycards() {


        try {
            List<Userbestcard> allusersbestthirtycardsList=userbestcardrepo.findAll();
            if (allusersbestthirtycardsList.isEmpty()){
                return ResponseEntity.ok(new Userbestthirtycards("there are no best cards"));
            }else{
//               return ResponseEntity.ok(userbestthirtycardsList);
                return allusersbestthirtycardsList;
            }

        }catch (Exception e){
            return new Userbestthirtycards("an error has occurred while getting user bes cards "+e.getMessage());
        }
}

    public PagedResponse<GameRoomResponse> gamerooms(int page, int size) {
          cardService.validatePagenumberandSize(page,size);
          try {
              Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
              Page<Gameroommaster> gameRooms = gameroommasterrepo.findAllByStatusEquals(true,pageable);
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


    public Getuserbuyinresponse getuserbuyin(UserBuyInRequest userBuyInRequest) {

        if (userBuyInRequest.getUserid()==null||userBuyInRequest.getGameroomid()==null){
            return new Getuserbuyinresponse("provide enough details");

        }
        try {

            User user= userrepo.findById(userBuyInRequest.getUserid()).orElseThrow(() -> new UserNotFoundException("user not foun"));
            Gameroommaster room=gameroommasterrepo.findById(userBuyInRequest.getGameroomid()).orElseThrow(() -> new GameNotFoundException("Game room not found"));
            boolean existsbyuseridandgameroomid=buyinrepo.existsByUserAndGameroommaster(user,room);
            if (existsbyuseridandgameroomid){
                BuyIn buyIn=buyinrepo.findByUserAndGameroommaster(user,room).orElseThrow(() -> new GameNotFoundException("BUy in not found"));
                return new Getuserbuyinresponse(buyIn.getUser().getId(),buyIn.getGameroommaster().getId(),buyIn.getAmount(),buyIn.getId());
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

//        log.info("the create game room request   {}", creategameroomrequest.getMinimumamount());
        Gameroommaster gameRoom = new Gameroommaster();
        if (!(creategameroomrequest.getGameroomid() == null || creategameroomrequest.getGameroomid() == 0)) {
        boolean existsbygameid = gameroommasterrepo.existsById(creategameroomrequest.getGameroomid());
        if (existsbygameid) {
            Gameroommaster existinggameroom = gameroommasterrepo.findById(creategameroomrequest.getGameroomid()).orElseThrow(() -> new GameNotFoundException("Game room not found"));
            existinggameroom.setParticipants(existinggameroom.getParticipants());
            existinggameroom.setAmount(creategameroomrequest.getMinimumamount());
            gameroommasterrepo.save(existinggameroom);
        }else{

        }
    }else{
            gameRoom.setCreateddate(Instant.now());
            gameRoom.setStatus(false);
            gameRoom.setParticipants(0);
            gameRoom.setAmount(creategameroomrequest.getMinimumamount());
            return gameroommasterrepo.save(gameRoom);
        }

        return "game room saved";
    }

    public Object getgameroombyid(Gameroommasterrequest gameroommasterrequest) {

          if (gameroommasterrequest.getMasterroomid()==null){
              return "Invalid request body";

          }
          try {
       Gameroommaster gameroommaster=gameroommasterrepo.findById(gameroommasterrequest.getMasterroomid()).orElseThrow(() -> new GameNotFoundException("Game room master not found"));
         return gameroommaster;
          }catch (Exception e){
              return "Am exception has occurred while fetching game room";
          }

    }

    public BUyinresponse buyin(Buyinrequest buyinrequest) {

        if (buyinrequest.getRoomid()==null||buyinrequest.getAmount()==null|| buyinrequest.getUid()==null|| buyinrequest.getMastertableid()==null){
            return new BUyinresponse("One of the request boy values is null","false");

        }
        log.info("BUy in request getuid {} amount {} mastertable {} roomid {}  ",buyinrequest.getUid(),buyinrequest.getAmount(),buyinrequest.getMastertableid(),buyinrequest.getRoomid());
//        System.out.println("The buy in request "+buyinrequest.getUid());
        try {

            User user=userrepo.findById(buyinrequest.getUid()).orElseThrow(() -> new UserNotFoundException("User not found"));
            Gameroommaster gameroommaster=gameroommasterrepo.findById(buyinrequest.getMastertableid()).orElseThrow(() -> new GameNotFoundException("Game room master table not found"));
           GameRoomTable gameRoomTable=gameroomtablerepo.findById(buyinrequest.getRoomid()).orElseThrow(() -> new GameNotFoundException("room not found"));
            Userwallet userwallet=userwalletrepo.findByUser(user).orElseThrow(() -> new UserWalletNotFoundException("Your wallet could not be found"));
            if (userwallet.getTotalwalletbalance().compareTo(buyinrequest.getAmount()) > 0){
            BuyIn buyIn=new BuyIn();
            buyIn.setUser(user);
            buyIn.setGameRoomTable(gameRoomTable);
            buyIn.setAmount(buyinrequest.getAmount());
            buyIn.setCreatedDate(Instant.now());
            buyIn.setGameroommaster(gameroommaster);

            buyinrepo.save(buyIn);
            return new BUyinresponse("Buy in saved successfully! ","true");
            }else{
                return new BUyinresponse("You do not have enoug money in your wallet ","false");
            }

        }catch (Exception e){

            return new BUyinresponse("An exception has occurred during buy in. "+e.getMessage(),"false");
        }

    }

    public PagedResponse<RoomTableusersresponse> getusersforagameroomtable( Getusersforroomtablerequest getusersforroomtablerequest,int page,int size){
        if (getusersforroomtablerequest.getUid()==null||getusersforroomtablerequest.getRoomid()==null){
            throw new RuntimeException("Invalid room id provided");
        }
        cardService.validatePagenumberandSize(page,size);

        try {
            GameRoomTable gameRoomTable = gameroomtablerepo.findById(getusersforroomtablerequest.getRoomid()).orElseThrow(() -> new GameNotFoundException("Game room not found with the given id " + getusersforroomtablerequest.getRoomid()));
            List<Long> userids = new ArrayList<>(0);
            if (gameRoomTable.getUidone() != null) {
                userids.add(gameRoomTable.getUidone());
            }
            if (gameRoomTable.getUidtwo() != null) {
                userids.add(gameRoomTable.getUidtwo());
            }
            if (gameRoomTable.getUidthree() != null) {
                userids.add(gameRoomTable.getUidthree());
            }
            if (gameRoomTable.getUidfour() != null) {
                userids.add(gameRoomTable.getUidfour());
            }
            if (gameRoomTable.getUidfive() != null) {
                userids.add(gameRoomTable.getUidfive());
            }

            if (!userids.isEmpty()) {
//                userids.remove(getusersforroomtablerequest.getUid());
//                List<User> users = userrepo.findAllByUIDIn(userids);
                log.info("the uids {}",userids);


                String ids=userids.stream().map(Object::toString).collect(Collectors.joining(","));
                Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
                Page<User> users = userrepo.findAllByUIDIn(userids,pageable);
//                Page<User> users = userrepo.findAllByUIDIn(userids,pageable);
                log.info("users {}",users);


                if (users.getNumberOfElements() == 0) {
                    return new PagedResponse<>(Collections.emptyList(), users.getNumber(), users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
                }
//                List<RoomTableusersresponse> roomtableusersresponse = users.map(ModelMapper::maptableuserstotableuserresponse).getContent();
                List<RoomTableusersresponse> roomtableusersresponse = users.map(user -> ModelMapper.maptableuserstotableuserresponse(user,getusersforroomtablerequest.getRoomid())).getContent();
                log.info("logging roomtableusersresponse {}", roomtableusersresponse);
//              roomtableusersresponse.forEach(roomTableusersresponse -> );
                return new PagedResponse<>(roomtableusersresponse, users.getNumber(), users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());



            }else {
                throw new RuntimeException("THere are no other players in the game room");
            }


        }catch (Exception e){
            throw new RuntimeException("an exception has occurred while fetching  user profiles "+e.getMessage());
        }
    }
    public List<RoomTableusersresponse> getusersforagameroomtable( Getusersforroomtablerequest getusersforroomtablerequest){
        if (getusersforroomtablerequest.getUid()==null||getusersforroomtablerequest.getRoomid()==null){
            throw new RuntimeException("Invalid room id provided");
        }

        try {
            GameRoomTable gameRoomTable = gameroomtablerepo.findById(getusersforroomtablerequest.getRoomid()).orElseThrow(() -> new GameNotFoundException("Game room not found with the given id " + getusersforroomtablerequest.getRoomid()));
            List<Long> userids = new ArrayList<>(0);
            if (gameRoomTable.getUidone() != null) {
                userids.add(gameRoomTable.getUidone());
            }
            if (gameRoomTable.getUidtwo() != null) {
                userids.add(gameRoomTable.getUidtwo());
            }
            if (gameRoomTable.getUidthree() != null) {
                userids.add(gameRoomTable.getUidthree());
            }
            if (gameRoomTable.getUidfour() != null) {
                userids.add(gameRoomTable.getUidfour());
            }
            if (gameRoomTable.getUidfive() != null) {
                userids.add(gameRoomTable.getUidfive());
            }

            if (!userids.isEmpty()) {
//                userids.remove(getusersforroomtablerequest.getUid());
                log.info("the uids {}",userids);



                String ids=userids.stream().map(Object::toString).collect(Collectors.joining(","));
              log.info("uid string {}",ids);
                List<User> userList=userrepo.findAllByUIDInFindInSet(userids,ids);
                if (userList.isEmpty()){
                    return new ArrayList<>(Collections.emptyList());
                }
                List<RoomTableusersresponse> roomTableusersresponses= userList.stream().map(user -> ModelMapper.maptableuserstotableuserresponse(user, getusersforroomtablerequest.getRoomid())).toList();
                return  roomTableusersresponses;
            }else {
                throw new RuntimeException("THere are no other players in the game room");
            }


        }catch (Exception e){
            throw new RuntimeException("an exception has occurred while fetching  user profiles "+e.getMessage());
        }
    }

    public CurrentUserroomdetailsresponse currentUserroomdetailsresponse(Currentuserroomrequest currentuserroomrequest){
        if (currentuserroomrequest.getUid()==null|| currentuserroomrequest.getRoomid()==null){
            return new CurrentUserroomdetailsresponse(null,null,null,"Invalid request body");
        }
        try {
            User user=userrepo.findById(currentuserroomrequest.getUid()).orElseThrow(() -> new UserNotFoundException("user not found"));
            GameRoomTable gameRoomTable=gameroomtablerepo.findById(currentuserroomrequest.getRoomid()).orElseThrow(() -> new GameNotFoundException("game room could not be found"));

//            CurrentUserroomdetailsresponse currentUserroomdetailsresponse=new CurrentUserroomdetailsresponse();
//            currentUserroomdetailsresponse.setBuyIn(user.getBuyIns().stream().filter(buyIn -> buyIn.getGameRoomTable().getId()== currentuserroomrequest.getRoomid()).findFirst().orElse(null));

            return new CurrentUserroomdetailsresponse(user.getBuyIns().stream().filter(buyIn -> buyIn.getGameRoomTable().getId()==currentuserroomrequest.getRoomid()).findFirst().orElse(null),user.getUserwallet(),user.getUserbestcards(),null);

        }catch (Exception e){
            return new CurrentUserroomdetailsresponse(null,null,null,"An exception has occurred while getting current ser details "+e.getMessage());
        }
    }

    public Updateplayerbuyinresponse updateplayerbuyin(Updateplayerbuyinrequest updateplayerbuyinrequest) {

        if (updateplayerbuyinrequest.getBuyin()==null||updateplayerbuyinrequest.getUid()==null || updateplayerbuyinrequest.getBuyinid()==null){
            return new Updateplayerbuyinresponse("One of the request body parameters is null","false");
        }
        log.info("the request body {}",updateplayerbuyinrequest);
        try {
            User user=userrepo.findById(updateplayerbuyinrequest.getUid()).orElseThrow(() -> new UserNotFoundException("User with the provided id could not be found"));
            BuyIn buyin=buyinrepo.findByIdAndUser(updateplayerbuyinrequest.getBuyinid(),user).orElseThrow(() -> new BuyinNotFoundException("Buyin amunt with the given user "+user.getUID()+" and given id "+updateplayerbuyinrequest.getBuyinid()+" not found."));;
             if (updateplayerbuyinrequest.getStatus().equals("updateexisting")){
                 log.info("updateexisting {}",updateplayerbuyinrequest.getStatus());
                 buyin.setAmount(buyin.getAmount().add(updateplayerbuyinrequest.getBuyin()));
                 BuyIn SavedbuyIn=buyinrepo.save(buyin);
                 return new Updateplayerbuyinresponse("Buyin updated ",SavedbuyIn.getUser().getUID(),SavedbuyIn.getAmount(),SavedbuyIn.getId(),"true");

             }
             if (updateplayerbuyinrequest.getStatus().equals("raiseamount") || updateplayerbuyinrequest.getStatus().equals("matchopponentamount")){
                 log.info("raiseamount {}",updateplayerbuyinrequest.getStatus());

                 buyin.setAmount(updateplayerbuyinrequest.getBuyin());
                 BuyIn SavedbuyIn=buyinrepo.save(buyin);
                 return new Updateplayerbuyinresponse("Buyin raised ",SavedbuyIn.getUser().getUID(),SavedbuyIn.getAmount(),SavedbuyIn.getId(),"true");

             }
             else{
                 buyin.setAmount(updateplayerbuyinrequest.getBuyin());
                 BuyIn SavedbuyIn= buyinrepo.save(buyin);
                 return new Updateplayerbuyinresponse("Buyin updated ",SavedbuyIn.getUser().getUID(),SavedbuyIn.getAmount(),SavedbuyIn.getId(),"true");

             }
        }catch (Exception e){
            return new Updateplayerbuyinresponse("An exception has occurred while updating the buy in for the user "+e.getMessage(),updateplayerbuyinrequest.getUid(),updateplayerbuyinrequest.getBuyin(),null,"false");
        }
    }

    public List<Buyintableresponse> updateallplayersbuyin(Updateallplayerbuyinsrequest updateallplayerbuyinsrequest) {



        try {

            GameRoomTable gameRoomTable=gameroomtablerepo.findById(updateallplayerbuyinsrequest.getGameroomtableid()).orElseThrow(() -> new GameNotFoundException("Game room tabl not found "));
            Gameroommaster gameroommaster=gameroommasterrepo.findById(gameRoomTable.getGameroommaster().getId()).orElseThrow(() -> new GameNotFoundException("Game room master not found"));
            BigDecimal bigDecimal=gameroommaster.getAmount();
            List<BuyIn> buyIn=buyinrepo.findAllByGameRoomTable(gameRoomTable);
            buyIn.forEach(buyIn1 -> {
                buyIn1.setAmount(buyIn1.getAmount().subtract(bigDecimal));
                buyinrepo.save(buyIn1);
            });
             buyIn.forEach(buyIn1 -> {
                 log.info("buyin repo {}",buyIn1.getAmount());
             });
            List<BuyIn> buyIns=buyinrepo.findAllByGameRoomTable(gameRoomTable);
            List<Buyintableresponse> roomTableusersresponses=buyIns.stream().map(ModelMapper::mapbuyinstobuyinresponse).toList();
//                    List<RoomTableusersresponse> roomTableusersresponses= userList.stream().map(user -> ModelMapper.maptableuserstotableuserresponse(user, getusersforroomtablerequest.getRoomid())).toList();

            return roomTableusersresponses;



        }catch (Exception e){
            throw new BuyinNotFoundException("An exception has occurred while updating the buyin "+e.getMessage());
//            return new ResponseEntity<>("An exception has occurred while updating the buyin "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    public Map<Integer,Map<Integer,Integer>> thewinninghands(){
        int x,y,z,w,v;
        x=v=y=z=w=0;
        int[] twogoldpairs1={13,13,12,12};/**||*/ int[] twogoldpairs2={13,13,11,11};/*||*/int[] twogoldpairs3={12,12,11,11};

        int[] threeGoldstraight={13,12,11,10,9};

        int[] twoGoldStraight= {12,11, 10, 9, 8};

        int[] SilverHighFullHouseone={13,13,10,10,10}; /**||*/ int[] SilverHighFullHousetwo={13,13,8,8,8};/**||*/int[] SilverHighFullHousethrree={12,12,6,6,6};

        int[] SilverFullHousefour={10,10,10,9,9};

        int[] TwoSilverPair={10, 10, 9, 9};

        int[]  GoldPair={13,13};

        int[] oneGoldstraight={ 11,10,9,8,7};

        int[] AllSilverStraight={10,9,8,7,6};

        int[] fourSilverStraight={9,8,7,6,5};

        int[] threeSilverStraight={8,7,6,5,4};

        int[] SilverHighFullHousethreesilver2bronze={10,10,10,5,5};

        int[] Bronzefullhouse={5,5,5,4,4};

        int[]  Silver3ofaKind={10,10,10};

        int[] Bronze4ofaKind={5,5,5,5};

        int[] twoSilverStraight={7,6,5,4,3};

        int[] BronzeHighFullHouse={10,10,5,5,5};

        int[]  onebronzepair1silverpair={10, 10, 2, 2};

        int[] SilverPair={10,10};
        int[] TwoBronzePair={5,5,2,2};

        int[] oneSilverStraight={6,5,4,3,2};

        int[] AllBronzeStraight={5,4,3,2,1};

        int[] Bronze3ofaKind={5,5,5};

        int[] BronzePair={5,5};
//        int[] HighCard={v,w,x,y,z};


        Map<Integer,Integer> twogoldpairmap1=new HashMap<>(0);
       /* Map<Integer,Integer> twogoldpairmap2=new HashMap<>(0);
        Map<Integer,Integer> twogoldpairmap3=new HashMap<>(0);
*/
        Map<Integer,Integer> threeGoldstraightmap=new HashMap<>(0);
        Map<Integer,Integer> twoGoldStraightmap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouseonemap=new HashMap<>(0);
       /* Map<Integer,Integer> SilverHighFullHousetwomap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHousethrreemap=new HashMap<>(0);*/
        Map<Integer,Integer> SilverFullHousefourmap=new HashMap<>(0);
        Map<Integer,Integer> TwoSilverPairmap=new HashMap<>(0);
        Map<Integer,Integer> GoldPairmap=new HashMap<>(0);
        Map<Integer,Integer> oneGoldstraightmap=new HashMap<>(0);
        Map<Integer,Integer> AllSilverStraightmap=new HashMap<>(0);

        Map<Integer,Integer> fourSilverStraightmap=new HashMap<>(0);
        Map<Integer,Integer> threeSilverStraightmap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHousethreesilver2bronzemap=new HashMap<>(0);
        Map<Integer,Integer> Bronzefullhousemap=new HashMap<>(0);
        Map<Integer,Integer> Silver3ofaKindmap=new HashMap<>(0);
        Map<Integer,Integer> Bronze4ofaKindmap=new HashMap<>(0);
        Map<Integer,Integer> twoSilverStraightmap=new HashMap<>(0);
        Map<Integer,Integer> BronzeHighFullHousemap=new HashMap<>(0);
        Map<Integer,Integer> onebronzepair1silverpairmap=new HashMap<>(0);

        Map<Integer,Integer> SilverPairmap=new HashMap<>(0);
        Map<Integer,Integer> TwoBronzePairmap=new HashMap<>(0);
        Map<Integer,Integer> oneSilverStraightmap=new HashMap<>(0);

        Map<Integer,Integer> AllBronzeStraightmap=new HashMap<>(0);
        Map<Integer,Integer> Bronze3ofaKindmap=new HashMap<>(0);
        Map<Integer,Integer> BronzePairmap=new HashMap<>(0);
//        Map<Integer,Integer> HighCardmap=new HashMap<>(0);




        Arrays.stream(twogoldpairs1).forEach(value -> twogoldpairmap1.put(value,twogoldpairmap1.getOrDefault(value,0)+1));
       /** Arrays.stream(twogoldpairs2).forEach(value -> twogoldpairmap2.put(value,twogoldpairmap2.getOrDefault(value,0)+1));
        Arrays.stream(twogoldpairs3).forEach(value -> twogoldpairmap3.put(value,twogoldpairmap3.getOrDefault(value,0)+1));*/

        Arrays.stream(threeGoldstraight).forEach(value -> threeGoldstraightmap.put(value,threeGoldstraightmap.getOrDefault(value,0)+1));
        Arrays.stream(twoGoldStraight).forEach(value -> twoGoldStraightmap.put(value,twoGoldStraightmap.getOrDefault(value,0)+1));
        Arrays.stream(SilverHighFullHouseone).forEach(value -> SilverHighFullHouseonemap.put(value,SilverHighFullHouseonemap.getOrDefault(value,0)+1));
       /** Arrays.stream(SilverHighFullHousetwo).forEach(value -> SilverHighFullHousetwomap.put(value,SilverHighFullHousetwomap.getOrDefault(value,0)+1));
        Arrays.stream(SilverHighFullHousethrree).forEach(value -> SilverHighFullHousethrreemap.put(value,SilverHighFullHousethrreemap.getOrDefault(value,0)+1));*/
        Arrays.stream(SilverFullHousefour).forEach(value -> SilverFullHousefourmap.put(value,SilverFullHousefourmap.getOrDefault(value,0)+1));
        Arrays.stream(TwoSilverPair).forEach(value -> TwoSilverPairmap.put(value,TwoSilverPairmap.getOrDefault(value,0)+1));
        Arrays.stream(GoldPair).forEach(value -> GoldPairmap.put(value,GoldPairmap.getOrDefault(value,0)+1));
        Arrays.stream(oneGoldstraight).forEach(value -> oneGoldstraightmap.put(value,oneGoldstraightmap.getOrDefault(value,0)+1));
        Arrays.stream(AllSilverStraight).forEach(value -> AllSilverStraightmap.put(value,AllSilverStraightmap.getOrDefault(value,0)+1));

        Arrays.stream(fourSilverStraight).forEach(value -> fourSilverStraightmap.put(value,fourSilverStraightmap.getOrDefault(value,0)+1));
        Arrays.stream(threeSilverStraight).forEach(value -> threeSilverStraightmap.put(value,threeSilverStraightmap.getOrDefault(value,0)+1));
        Arrays.stream(SilverHighFullHousethreesilver2bronze).forEach(value -> SilverHighFullHousethreesilver2bronzemap.put(value,SilverHighFullHousethreesilver2bronzemap.getOrDefault(value,0)+1));
        Arrays.stream(Bronzefullhouse).forEach(value -> Bronzefullhousemap.put(value,Bronzefullhousemap.getOrDefault(value,0)+1));
        Arrays.stream(Silver3ofaKind).forEach(value -> Silver3ofaKindmap.put(value,Silver3ofaKindmap.getOrDefault(value,0)+1));
        Arrays.stream(Bronze4ofaKind).forEach(value -> Bronze4ofaKindmap.put(value,Bronze4ofaKindmap.getOrDefault(value,0)+1));
        Arrays.stream(twoSilverStraight).forEach(value -> twoSilverStraightmap.put(value,twoSilverStraightmap.getOrDefault(value,0)+1));
        Arrays.stream(BronzeHighFullHouse).forEach(value -> BronzeHighFullHousemap.put(value,BronzeHighFullHousemap.getOrDefault(value,0)+1));
        Arrays.stream(onebronzepair1silverpair).forEach(value -> onebronzepair1silverpairmap.put(value,onebronzepair1silverpairmap.getOrDefault(value,0)+1));
        Arrays.stream(SilverPair).forEach(value -> SilverPairmap.put(value,SilverPairmap.getOrDefault(value,0)+1));
        Arrays.stream(TwoBronzePair).forEach(value -> TwoBronzePairmap.put(value,TwoBronzePairmap.getOrDefault(value,0)+1));
        Arrays.stream(oneSilverStraight).forEach(value -> oneSilverStraightmap.put(value,oneSilverStraightmap.getOrDefault(value,0)+1));

        Arrays.stream(AllBronzeStraight).forEach(value -> AllBronzeStraightmap.put(value,AllBronzeStraightmap.getOrDefault(value,0)+1));
        Arrays.stream(Bronze3ofaKind).forEach(value -> Bronze3ofaKindmap.put(value,Bronze3ofaKindmap.getOrDefault(value,0)+1));
        Arrays.stream(BronzePair).forEach(value -> BronzePairmap.put(value,BronzePairmap.getOrDefault(value,0)+1));
//        Arrays.stream(HighCard).forEach(value -> HighCardmap.put(value,HighCardmap.getOrDefault(value,0)+1));



        Map<Integer,Map<Integer,Integer>> mapMap=new HashMap<>();
        mapMap.put(1,twogoldpairmap1);
//        mapMap.put(2,twogoldpairmap2);
//        mapMap.put(3,twogoldpairmap3);

        mapMap.put(2,threeGoldstraightmap);
        mapMap.put(3,twoGoldStraightmap);
        mapMap.put(4,SilverHighFullHouseonemap);
       /** mapMap.put(5,SilverHighFullHousetwomap);
        mapMap.put(6,SilverHighFullHousethrreemap);*/
        mapMap.put(5,SilverFullHousefourmap);
        mapMap.put(6,TwoSilverPairmap);
        mapMap.put(7,GoldPairmap);
        mapMap.put(8,oneGoldstraightmap);
        mapMap.put(9,AllSilverStraightmap);

        mapMap.put(10,fourSilverStraightmap);
        mapMap.put(11,threeSilverStraightmap);

        mapMap.put(12,SilverHighFullHousethreesilver2bronzemap);
        mapMap.put(13,Bronzefullhousemap);
        mapMap.put(14,Silver3ofaKindmap);

        mapMap.put(15,Bronze4ofaKindmap);
        mapMap.put(16,twoSilverStraightmap);
        mapMap.put(17,BronzeHighFullHousemap);

        mapMap.put(18,onebronzepair1silverpairmap);
        mapMap.put(19,SilverPairmap);
        mapMap.put(20,TwoBronzePairmap);

        mapMap.put(21,oneSilverStraightmap);

        mapMap.put(22,AllBronzeStraightmap);
        mapMap.put(23,Bronze3ofaKindmap);
        mapMap.put(24,BronzePairmap);
//        mapMap.put(27,HighCardmap);

//        System.out.println(mapMap);


        return mapMap;
    }




    public Object winninghand(ArrayList<ArrayList<Winninghandrequest>> winninghandrequests) {

//        System.out.println(otherwinninghandcominations.twogoldpairfunctiontwo().en);
//        System.out.println(otherwinninghandcominations.twogoldpairfunctionthree());

        AtomicInteger autoincrement= new AtomicInteger();
        AtomicInteger usercardincrement= new AtomicInteger();

       /**Map<Integer,Integer> firstplayerusercardslist=new HashMap<>();
       Map<Integer,Integer> secondplayerusercardslist=new HashMap<>();

        int[] frtsttheusercards=new int[5];
        AtomicInteger f= new AtomicInteger();
        int[] sectheusercards=new int[5];
        AtomicInteger s= new AtomicInteger();*/


        Map<Integer,Integer> uid140layerusercardslist=new HashMap<>();
        Map<Integer,Integer> uid199payerusercardslist=new HashMap<>();

        int[] uid140usercards=new int[5];
        AtomicInteger f= new AtomicInteger();
        int[] uid199usercards=new int[5];
        AtomicInteger s= new AtomicInteger();

        List<Integer> uid140matchlist=new ArrayList<>();
        List<Integer> uid199matchlist=new ArrayList<>();


       winninghandrequests.forEach(winninghandrequest -> {
        System.out.println("Start of ----------"+autoincrement+"----- index");
         winninghandrequest.forEach(winninghandrequest1 -> {
            System.out.println("inside index-------- "+usercardincrement);

            boolean userbestcardexists=userbestcardrepo.existsById(winninghandrequest1.getId());
           if (userbestcardexists){
               Userbestcard userbestcard=userbestcardrepo.findById(winninghandrequest1.getId()).orElseThrow(() -> new UserCardNotFoundException("The best user card could not be found"));
//               UserCard userCard=userCardRepo.findById(userbestcard.getUserbestcard()).orElseThrow(() -> new UserCardNotFoundException("User card not found "));
//               Integer thecardid=userCard.getCardduplicate().getCard().getCardNumber();
               Integer thecardid=userbestcard.getUserCard().getCardduplicate().getCard().getCardNumber();
               System.out.println("thecardid "+thecardid);
               //               Integer thecardid=userbestcard.getUserCard().getCardduplicate().getCard().getCardNumber();
//               Integer thecardid=thecardi.intValue();
               
                if (autoincrement.intValue()==0) {
                    uid140layerusercardslist.put(thecardid,uid140layerusercardslist.getOrDefault(thecardid, 0)+1);
                    uid140usercards[f.getAndIncrement()]=thecardid;
                }
                if(autoincrement.intValue()==1){
                    uid199payerusercardslist.put(thecardid, uid199payerusercardslist.getOrDefault(thecardid, 0)+1);
                     uid199usercards[s.getAndIncrement()]=thecardid;
                }
            }
            System.out.println("End OF inside Index ---------"+usercardincrement);
           usercardincrement.getAndIncrement();
        });
        System.out.println("End of ----"+autoincrement+"------ Index");
        autoincrement.getAndIncrement();

    });

       /** Integer[] playerone={13,12,13,10,12};
//        Integer[] playerone={13,11,13,10,11};
        Integer[] playertwo={7,8,9,11,10};


        Arrays.stream(playerone).forEach(integer -> firstplayerusercardslist.put(integer,firstplayerusercardslist.getOrDefault(integer,0)+1));
        Arrays.stream(playertwo).forEach(integer -> secondplayerusercardslist.put(integer,secondplayerusercardslist.getOrDefault(integer,0)+1));

        Arrays.stream(playerone).forEach(integer ->frtsttheusercards[f.getAndIncrement()]=integer);
        Arrays.stream(playertwo).forEach(integer ->sectheusercards[s.getAndIncrement()]=integer);
*/
        List<Integer> playeronematchlist=new ArrayList<>();
        List<Integer> playertwomatchlist=new ArrayList<>();

        System.out.println("frtsttheusercards+ "+Arrays.toString(uid140usercards));
        System.out.println("sectheusercards+ "+Arrays.toString(uid199usercards));
        System.out.println("firstplayerusercardslist "+uid140layerusercardslist);
        System.out.println("secondplayerusercardslist "+uid199payerusercardslist);
//  check if the first player has any winning hand

/**first player*/
        RoomService.Otherwinninghandcominations otherwinninghandcominations=new RoomService.Otherwinninghandcominations();

        thewinninghands().forEach((integer, integerIntegerMap) -> {
            System.out.println("Integer index second player "+integer);

            if (integer==1){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twogoldpairfunctiontwo().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twogoldpairfunctionthree().entrySet())
                ){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==2){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==3){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }

            if(integer==4){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousetwomapfunction().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousethreemapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousefourmapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousefivemapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousesixmapfunction().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousesevenmapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouseeightmapfunction().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouseninemapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousetenmapfunction().entrySet())   ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouseelevenmapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse12mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse13mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse14mapfunction().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse15mapfunction().entrySet())
                ){

                    uid140matchlist.add(integer);
                }
            }
            if (integer==5){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse1mapfunction().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse2mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse41mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse6mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse8mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse9mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse10mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse11mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse122mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse133mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse144mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse155mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse166mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse177mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse188mapfunction().entrySet())
                ){
                    uid140matchlist.add(integer);


                }


            }
            if (integer==6){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair2mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair4mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair6mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair8mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair9mapfunction().entrySet())
                ){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==7){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.GoldPair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.GoldPair2mapfunction().entrySet())){
                    uid140matchlist.add(integer);
                }
            }

            if (integer==8){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==9){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }            }
            if (integer==10){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }            }
            if (integer==11){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }            }


            if (integer==12){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair2mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair4mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair6mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair8mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair9mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair10mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair11mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair12mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair13mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair14mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair14mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair15mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair16mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair17mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair18mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair19mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair20mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair21mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair22mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair23mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair24mapfunction().entrySet())
                ){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==13){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair2mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair4mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair6mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair8mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair9mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair10mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair11mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair12mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair13mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair14mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair15mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair16mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair17mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair18mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair19mapfunction().entrySet())

                ){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==14){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.silverthreeofakind1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.silverthreeofakind2mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.silverthreeofakind3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.silverthreeofakind4mapfunction().entrySet())){

                    uid140matchlist.add(integer);

                }
            }
            if (integer==15){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzefourofakind1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzefourofakind2mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzefourofakind3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzefourofakind4mapfunction().entrySet())
                ){
                    uid140matchlist.add(integer);

                }

            }
            if (integer==16){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==17){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair2mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair4mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair6mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair8mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair9mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair10mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair11mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair12mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair13mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair14mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair15mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair16mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair17mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair18mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair19mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair20mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair21mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair22mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair23mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair24mapfunction().entrySet())) {
                    uid140matchlist.add(integer);
                }
            }
            if(integer==18){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair2mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair4mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair6mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair8mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair9mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair10mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair11mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair12mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair13mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair14mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair15mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair16mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair17mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair18mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair19mapfunction().entrySet())

                ){
                    uid140matchlist.add(integer);

                }
            }
            if (integer==19){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpair2mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpair3mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpair4mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpair5mapfunction().entrySet())

                ){
                    uid140matchlist.add(integer);

                }
            }
            if (integer==20){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair1mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair2mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair3mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair4mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair5mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair6mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair7mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair8mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair9mapfunction().entrySet())){
                    uid140matchlist.add(integer);
                }



            }
            if (integer==21){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==22){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==23){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzethreeofakind1mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzethreeofakind2mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzethreeofakind3mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzethreeofakind4mapfunction().entrySet())
                ){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==24){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzepair1map1function().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzepair2map2function().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzepair3map3function().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzepair4map4function().entrySet())){

                    uid140matchlist.add(integer);

                }

            }




            System.out.println(uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()));
            System.out.println("contains uid140 "+integerIntegerMap);
        });
/**end of first player*/




//  check if the second player has any winning hand
/**second player*/
        RoomService.Otherwinninghandcominations otherwinninghandcominationss=new RoomService.Otherwinninghandcominations();

        System.out.println("uid 199 card list is here "+uid199payerusercardslist);
        System.out.println("uid 140 card list is here "+uid140layerusercardslist);
        thewinninghands().forEach((integer, integerIntegerMap) -> {
            System.out.println("Integer indexuid199 "+integer);

            if (integer==1){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twogoldpairfunctiontwo().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twogoldpairfunctionthree().entrySet())
                ){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==2){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==3){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }

            if(integer==4){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousetwomapfunction().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousethreemapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousefourmapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousefivemapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousesixmapfunction().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousesevenmapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouseeightmapfunction().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouseninemapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousetenmapfunction().entrySet())   ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouseelevenmapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse12mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse13mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse14mapfunction().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse15mapfunction().entrySet())


                ){

                    uid199matchlist.add(integer);
                }
            }
            if (integer==5){
                System.out.println("Herereer5 "+integer+"fff "+integerIntegerMap);
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse1mapfunction().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse2mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse41mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse6mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse8mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse9mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse10mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse11mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse122mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse133mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse144mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse155mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse166mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse177mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse188mapfunction().entrySet())
                ){
                    uid199matchlist.add(integer);
                    System.out.println();


                }


            }
            if (integer==6){
                System.out.println("Herereer6 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair2mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair4mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair6mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair8mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair9mapfunction().entrySet())
                ){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==7){
                System.out.println("Herereer7 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.GoldPair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.GoldPair2mapfunction().entrySet())){
                    uid199matchlist.add(integer);
                }
            }

            if (integer==8){
                System.out.println("Herereer8 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }

            }
            if (integer==9){
                System.out.println("Herereer9 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==10){
                System.out.println("Herereer10 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==11){
                System.out.println("Herereer11 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }


            if (integer==12){
                System.out.println("Herereer12 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair2mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair4mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair6mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair8mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair9mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair10mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair11mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair12mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair13mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair14mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair14mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair15mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair16mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair17mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair18mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair19mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair20mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair21mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair22mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair23mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair24mapfunction().entrySet())
                ){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==13){
                System.out.println("Herereer13 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair2mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair4mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair6mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair8mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair9mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair10mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair11mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair12mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair13mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair14mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair15mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair16mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair17mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair18mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair19mapfunction().entrySet())

                ){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==14){
                System.out.println("Herereer14 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.silverthreeofakind1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.silverthreeofakind2mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.silverthreeofakind3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.silverthreeofakind4mapfunction().entrySet())){

                    uid199matchlist.add(integer);

                }
            }
            if (integer==15){
                System.out.println("Herereer15 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzefourofakind1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzefourofakind2mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzefourofakind3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzefourofakind4mapfunction().entrySet())
                ){
                    uid199matchlist.add(integer);

                }

            }
            if (integer==16){
                System.out.println("Herereer16 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }


            }
            if (integer==17){
                System.out.println("Herereer17 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair2mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair4mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair6mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair8mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair9mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair10mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair11mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair12mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair13mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair14mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair15mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair16mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair17mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair18mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair19mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair20mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair21mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair22mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair23mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair24mapfunction().entrySet())) {
                    uid199matchlist.add(integer);
                }
            }
            if(integer==18){
                System.out.println("Herereer18 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair2mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair4mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair6mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair8mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair9mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair10mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair11mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair12mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair13mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair14mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair15mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair16mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair17mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair18mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair19mapfunction().entrySet())

                ){
                    uid199matchlist.add(integer);

                }
            }
            if (integer==19){
                System.out.println("Herereer19 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpair2mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpair3mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpair4mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpair5mapfunction().entrySet())

                ){
                    uid199matchlist.add(integer);

                }
            }
            if (integer==20){
                System.out.println("Herereer20 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair1mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair2mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair3mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair4mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair5mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair6mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair7mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair8mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair9mapfunction().entrySet())){
                    uid199matchlist.add(integer);
                }



            }
            if (integer==21){
                System.out.println("Herereer21 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==22){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==23){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzethreeofakind1mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzethreeofakind2mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzethreeofakind3mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzethreeofakind4mapfunction().entrySet())
                ){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==24){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzepair1map1function().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzepair2map2function().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzepair3map3function().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzepair4map4function().entrySet())){

                    uid199matchlist.add(integer);

                }

            }



//            if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
//                uid199matchlist.add(integer);
//            }
            System.out.println(uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()));
            System.out.println("contains uid199 "+integerIntegerMap);
        });
        /**end of the second player*/


//        check if all elements in list is high card
        System.out.println("uid140matchlistnnn "+uid140matchlist);
        System.out.println("uid199matchlisthhhgh "+uid199matchlist);
        Collections.sort(uid140matchlist);
        Collections.sort(uid199matchlist);
        System.out.println("uid140layerusercardslist "+uid140layerusercardslist+" and uid140matchlist "+uid140matchlist);
        System.out.println("uid199payerusercardslist "+uid199payerusercardslist+ " and uid199matchlist "+uid199matchlist);

        if (uid140matchlist.isEmpty() && uid199matchlist.isEmpty()){
            int max = IntStream
                    .concat(IntStream.of(uid140usercards), IntStream.of(uid199usercards))
                    .max()
                    .getAsInt();
            System.out.println("the max"+max);
            if (Arrays.stream(uid140usercards).anyMatch(value ->value==max)){
                System.out.println("The uid 140 the game ");
                return new Winninghandresponse("The uid 140 has won the game "+uid140matchlist+" against uid 199 "+uid199matchlist,PLAYER_ONE+"The winning hand idex "+uid140matchlist);
            }
            if (Arrays.stream(uid199usercards).anyMatch(value -> value==max )){
                System.out.println("THe uid 199 player has won the game ");
                return new Winninghandresponse("THe uid 199  has won the game "+uid199matchlist+ " against uid 140 "+uid140matchlist,PLAYER_TWO+"The winning hand idex "+uid199matchlist);
            }

            System.out.println("uid 140"+uid140matchlist);
            System.out.println("uid 199 "+uid199matchlist);
//            return winninghandrequests.size();

//            return "They did not match any winning hand";
        }
        System.out.println("May be one is empty and the other is not ");

        System.out.println("uid 140 player"+uid140matchlist);
        System.out.println("uid 199 player "+uid199matchlist);


        if (uid140matchlist.isEmpty() && !uid199matchlist.isEmpty()){
            System.out.println("THe uid 140 PLayer cards array do not match with any winning hand while the uid 199 player  does");

            System.out.println("The uid 140 cards"+uid140matchlist);
            System.out.println("The uid 199 cards"+uid199matchlist);

            return new Winninghandresponse("PLayer two uid 199 has won the game "+uid199matchlist+" against player one uid 140 "+uid140matchlist,PLAYER_TWO+"The winning hand idex "+uid199matchlist);
        }
        if (!uid140matchlist.isEmpty() && uid199matchlist.isEmpty() ){
            System.out.println("The first player uid 140 matches cards array with any of the winning hand while player  two uid 199 does not  ");

            System.out.println("uid 140 "+uid140matchlist);
            System.out.println("uid 199 "+uid199matchlist);

            return new Winninghandresponse("PLayer one  uid 140 has won the game "+uid140matchlist+" against player two"+uid199matchlist,PLAYER_ONE+"The winning hand idex "+uid140matchlist);
        }
        if (uid140matchlist.get(0)<uid199matchlist.get(0)){
//            System.out.println(winninghandrequests.get(0).get(0).getUser().getUID());
            System.out.println("uid 140 won playeruid140matchlist"+uid140matchlist);
            System.out.println("uid 199 lost player uid199matchlist "+uid199matchlist);
            System.out.println("PLayer One uid 140 has won the game");
            return new Winninghandresponse("Player one uid 140 has won "+uid140matchlist+" against player two uid 199 "+uid199matchlist,PLAYER_ONE+"The winning hand idex "+uid140matchlist);

        }else if(uid199matchlist.get(0)<uid140matchlist.get(0)){
            System.out.println("Player two uid 199 has won the game");
            System.out.println("uid 140 lost playeruid140matchlist"+uid140matchlist);
            System.out.println("uid 199 won player uid199matchlist "+uid199matchlist);
            return new Winninghandresponse("PLayer two uid 199 has won "+uid199matchlist+" against uid 140 "+uid140matchlist,PLAYER_TWO+"The winning hand idex "+uid199matchlist);

        }


        if ( uid140matchlist.get(0).equals(uid199matchlist.get(0))){
            AtomicReference<Integer> uid40sum= new AtomicReference<>(0);
            AtomicReference<Integer> uid199sum= new AtomicReference<>(0);
            Arrays.stream(uid140usercards).forEach(value -> uid40sum.updateAndGet(v -> v + value));
            Arrays.stream(uid199usercards).forEach(value -> uid199sum.updateAndGet(v -> v + value));
            System.out.println("uid40sum "+uid40sum);
            System.out.println("uid199sum "+uid199sum);

//            return null;
            if (uid40sum.get()>uid199sum.get()){
                System.out.println("The first player uid 140 has won the game when both cards are equal ");
                return new Winninghandresponse("The first player  uid 140 has won the game:This is when both cards are equal "+uid140matchlist+" and uid 140 usercards are=>+"+Arrays.toString(uid140usercards)+"+ against  uid 199 "+uid199matchlist+" whose usecards are "+Arrays.toString(uid199usercards),PLAYER_ONE+"The winning hand idex "+uid140matchlist);
            }
            if (uid199sum.get()>uid40sum.get()){
                System.out.println("THe second player uid 199 has won the game:This is when both cards are equal ");
                return new Winninghandresponse("THe second player uid 199 has won the game:This is when both cards are equal "+uid199matchlist+" and uid 199 cards are "+Arrays.toString(uid199usercards)+" against uid 140 "+uid140matchlist+" whose usercards are "+Arrays.toString(uid140usercards),PLAYER_TWO+"The winning hand idex "+uid199matchlist);
            }
        }
        return null;


//        return winninghandrequests.size();
    }
    private void helper(List<int[]> combinations, int data[], int start, int end, int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinations.add(combination);
        } else if (start <= end) {
            data[index] = start;
            helper(combinations, data, start + 1, end, index + 1);
            helper(combinations, data, start + 1, end, index);
        }
    }
    public List<int[]> generate(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        helper(combinations, new int[r], 0, n-1, 0);
        return combinations;
    }


    public Object cardpairingtest(CardPairingtest cardPairingtest) {


        log.info("The player one cards {}",cardPairingtest);

        log.info("The player 140 cards {} player 199 {}",cardPairingtest.getUid140cards(),cardPairingtest.getUid199cards());
        Integer[] uid140=new Integer[5];
        Integer[] uid199=new Integer[5];
        AtomicInteger a= new AtomicInteger();
        AtomicInteger b= new AtomicInteger();
        String[] uid140cards=cardPairingtest.getUid140cards().split(",");
        String[] uid199cards=cardPairingtest.getUid199cards().split(",");
        Arrays.stream(uid140cards).forEach(s -> uid140[a.getAndIncrement()]= Integer.valueOf(s));
        Arrays.stream(uid199cards).forEach(s -> uid199[b.getAndIncrement()]= Integer.valueOf(s));
//        log.info("The INteger arrays 140 {} 199 {}",uid);






        Map<Integer,Integer> uid140layerusercardslist=new HashMap<>();
        Map<Integer,Integer> uid199payerusercardslist=new HashMap<>();

        int[] uid140usercards=new int[5];
        AtomicInteger f= new AtomicInteger();
        int[] uid199usercards=new int[5];
        AtomicInteger s= new AtomicInteger();



        /**Integer[] uid140={13,12,13,10,12};
        //        Integer[] uid140={13,11,13,10,11};
        Integer[] uid199={7,8,9,11,10};*/


        Arrays.stream(uid140).forEach(integer -> uid140layerusercardslist.put(integer,uid140layerusercardslist.getOrDefault(integer,0)+1));
        Arrays.stream(uid199).forEach(integer -> uid199payerusercardslist.put(integer,uid199payerusercardslist.getOrDefault(integer,0)+1));

        Arrays.stream(uid140).forEach(integer ->uid140usercards[f.getAndIncrement()]=integer);
        Arrays.stream(uid199).forEach(integer ->uid199usercards[s.getAndIncrement()]=integer);

        List<Integer> uid140matchlist=new ArrayList<>();
        List<Integer> uid199matchlist=new ArrayList<>();

        System.out.println("uid140usercards+ "+Arrays.toString(uid140usercards));
        System.out.println("uid199usercards+ "+Arrays.toString(uid199usercards));
        System.out.println("uid140layerusercardslist "+uid140layerusercardslist);
        System.out.println("uid199payerusercardslist "+uid199payerusercardslist);

/**first player*/
        RoomService.Otherwinninghandcominations otherwinninghandcominations=new RoomService.Otherwinninghandcominations();

        thewinninghands().forEach((integer, integerIntegerMap) -> {
            System.out.println("Integer index second player "+integer);

            if (integer==1){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twogoldpairfunctiontwo().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twogoldpairfunctionthree().entrySet())
                ){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==2){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==3){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }

            if(integer==4){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousetwomapfunction().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousethreemapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousefourmapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousefivemapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousesixmapfunction().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousesevenmapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouseeightmapfunction().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouseninemapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHousetenmapfunction().entrySet())   ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouseelevenmapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse12mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse13mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse14mapfunction().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse15mapfunction().entrySet())
                ){

                    uid140matchlist.add(integer);
                }
            }
            if (integer==5){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse1mapfunction().entrySet()) ||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse2mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse41mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse6mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse8mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse9mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse10mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse11mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse122mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse133mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse144mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse155mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse166mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse177mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.SilverHighFullHouse188mapfunction().entrySet())
                ){
                    uid140matchlist.add(integer);


                }


            }
            if (integer==6){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair2mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair4mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair6mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair8mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.TwoSilverPair9mapfunction().entrySet())
                ){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==7){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.GoldPair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.GoldPair2mapfunction().entrySet())){
                    uid140matchlist.add(integer);
                }
            }

            if (integer==8){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==9){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }            }
            if (integer==10){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }            }
            if (integer==11){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }            }


            if (integer==12){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair2mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair4mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair6mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair8mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair9mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair10mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair11mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair12mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair13mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair14mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair14mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair15mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair16mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair17mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair18mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair19mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair20mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair21mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair22mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair23mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threesilverpairtwobronzepair24mapfunction().entrySet())
                ){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==13){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair2mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair4mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair6mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair8mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair9mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair10mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair11mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair12mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair13mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair14mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair15mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair16mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair17mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair18mapfunction().entrySet())||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.threebronzepairtwobronzepair19mapfunction().entrySet())

                ){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==14){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.silverthreeofakind1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.silverthreeofakind2mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.silverthreeofakind3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.silverthreeofakind4mapfunction().entrySet())){

                    uid140matchlist.add(integer);

                }
            }
            if (integer==15){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzefourofakind1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzefourofakind2mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzefourofakind3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzefourofakind4mapfunction().entrySet())
                ){
                    uid140matchlist.add(integer);

                }

            }
            if (integer==16){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==17){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair2mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair4mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair6mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair8mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair9mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair10mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair11mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair12mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair13mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair14mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair15mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair16mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair17mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair18mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair19mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair20mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair21mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair22mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair23mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpairthreebronzepair24mapfunction().entrySet())) {
                    uid140matchlist.add(integer);
                }
            }
            if(integer==18){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair1mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair2mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair3mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair4mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair5mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair6mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair7mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair8mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair9mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair10mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair11mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair12mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair13mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair14mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair15mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair16mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair17mapfunction().entrySet())||
                        uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair18mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.onebronzepaironesilverpair19mapfunction().entrySet())

                ){
                    uid140matchlist.add(integer);

                }
            }
            if (integer==19){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpair2mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpair3mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpair4mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twosilverpair5mapfunction().entrySet())

                ){
                    uid140matchlist.add(integer);

                }
            }
            if (integer==20){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair1mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair2mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair3mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair4mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair5mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair6mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair7mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair8mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.twobronzepair2bronzepair9mapfunction().entrySet())){
                    uid140matchlist.add(integer);
                }



            }
            if (integer==21){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==22){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==23){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzethreeofakind1mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzethreeofakind2mapfunction().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzethreeofakind3mapfunction().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzethreeofakind4mapfunction().entrySet())
                ){
                    uid140matchlist.add(integer);
                }
            }
            if (integer==24){
                if (uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzepair1map1function().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzepair2map2function().entrySet())
                        ||uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzepair3map3function().entrySet())|| uid140layerusercardslist.entrySet().containsAll(otherwinninghandcominations.bronzepair4map4function().entrySet())){

                    uid140matchlist.add(integer);

                }

            }




            System.out.println(uid140layerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()));
            System.out.println("contains uid140 "+integerIntegerMap);
        });
/**end of first player*/




//  check if the second player has any winning hand
/**second player*/
        RoomService.Otherwinninghandcominations otherwinninghandcominationss=new RoomService.Otherwinninghandcominations();

        System.out.println("uid 199 card list is here "+uid199payerusercardslist);
        System.out.println("uid 140 card list is here "+uid140layerusercardslist);
        thewinninghands().forEach((integer, integerIntegerMap) -> {
            System.out.println("Integer indexuid199 "+integer);

            if (integer==1){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twogoldpairfunctiontwo().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twogoldpairfunctionthree().entrySet())
                ){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==2){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==3){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }

            if(integer==4){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousetwomapfunction().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousethreemapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousefourmapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousefivemapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousesixmapfunction().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousesevenmapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouseeightmapfunction().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouseninemapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHousetenmapfunction().entrySet())   ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouseelevenmapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse12mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse13mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse14mapfunction().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse15mapfunction().entrySet())


                ){

                    uid199matchlist.add(integer);
                }
            }
            if (integer==5){
                System.out.println("Herereer5 "+integer+"fff "+integerIntegerMap);
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()) ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse1mapfunction().entrySet()) ||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse2mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse41mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse6mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse8mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse9mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse10mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse11mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse122mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse133mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse144mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse155mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse166mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse177mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.SilverHighFullHouse188mapfunction().entrySet())
                ){
                    uid199matchlist.add(integer);
                    System.out.println();


                }


            }
            if (integer==6){
                System.out.println("Herereer6 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair2mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair4mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair6mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair8mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.TwoSilverPair9mapfunction().entrySet())
                ){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==7){
                System.out.println("Herereer7 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.GoldPair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.GoldPair2mapfunction().entrySet())){
                    uid199matchlist.add(integer);
                }
            }

            if (integer==8){
                System.out.println("Herereer8 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }

            }
            if (integer==9){
                System.out.println("Herereer9 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==10){
                System.out.println("Herereer10 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==11){
                System.out.println("Herereer11 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }


            if (integer==12){
                System.out.println("Herereer12 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair2mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair4mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair6mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair8mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair9mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair10mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair11mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair12mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair13mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair14mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair14mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair15mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair16mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair17mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair18mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair19mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair20mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair21mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair22mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair23mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threesilverpairtwobronzepair24mapfunction().entrySet())
                ){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==13){
                System.out.println("Herereer13 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair2mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair4mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair6mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair8mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair9mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair10mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair11mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair12mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair13mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair14mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair15mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair16mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair17mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair18mapfunction().entrySet())||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.threebronzepairtwobronzepair19mapfunction().entrySet())

                ){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==14){
                System.out.println("Herereer14 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.silverthreeofakind1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.silverthreeofakind2mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.silverthreeofakind3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.silverthreeofakind4mapfunction().entrySet())){

                    uid199matchlist.add(integer);

                }
            }
            if (integer==15){
                System.out.println("Herereer15 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzefourofakind1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzefourofakind2mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzefourofakind3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzefourofakind4mapfunction().entrySet())
                ){
                    uid199matchlist.add(integer);

                }

            }
            if (integer==16){
                System.out.println("Herereer16 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }


            }
            if (integer==17){
                System.out.println("Herereer17 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair2mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair4mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair6mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair8mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair9mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair10mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair11mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair12mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair13mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair14mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair15mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair16mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair17mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair18mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair19mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair20mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair21mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair22mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair23mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpairthreebronzepair24mapfunction().entrySet())) {
                    uid199matchlist.add(integer);
                }
            }
            if(integer==18){
                System.out.println("Herereer18 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair1mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair2mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair3mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair4mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair5mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair6mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair7mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair8mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair9mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair10mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair11mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair12mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair13mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair14mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair15mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair16mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair17mapfunction().entrySet())||
                        uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair18mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.onebronzepaironesilverpair19mapfunction().entrySet())

                ){
                    uid199matchlist.add(integer);

                }
            }
            if (integer==19){
                System.out.println("Herereer19 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpair2mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpair3mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpair4mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twosilverpair5mapfunction().entrySet())

                ){
                    uid199matchlist.add(integer);

                }
            }
            if (integer==20){
                System.out.println("Herereer20 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair1mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair2mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair3mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair4mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair5mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair6mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair7mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair8mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.twobronzepair2bronzepair9mapfunction().entrySet())){
                    uid199matchlist.add(integer);
                }



            }
            if (integer==21){
                System.out.println("Herereer21 "+integer+"fff "+integerIntegerMap);

                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==22){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==23){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzethreeofakind1mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzethreeofakind2mapfunction().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzethreeofakind3mapfunction().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzethreeofakind4mapfunction().entrySet())
                ){
                    uid199matchlist.add(integer);
                }
            }
            if (integer==24){
                if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzepair1map1function().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzepair2map2function().entrySet())
                        ||uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzepair3map3function().entrySet())|| uid199payerusercardslist.entrySet().containsAll(otherwinninghandcominationss.bronzepair4map4function().entrySet())){

                    uid199matchlist.add(integer);

                }

            }



//            if (uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet())){
//                uid199matchlist.add(integer);
//            }
            System.out.println(uid199payerusercardslist.entrySet().containsAll(integerIntegerMap.entrySet()));
            System.out.println("contains uid199 "+integerIntegerMap);
        });
        /**end of the second player*/


//        check if all elements in list is high card
        System.out.println("uid140matchlistnnn "+uid140matchlist);
        System.out.println("uid199matchlisthhhgh "+uid199matchlist);
        Collections.sort(uid140matchlist);
        Collections.sort(uid199matchlist);
        System.out.println("uid140layerusercardslist "+uid140layerusercardslist+" and uid140matchlist "+uid140matchlist);
        System.out.println("uid199payerusercardslist "+uid199payerusercardslist+ " and uid199matchlist "+uid199matchlist);

        if (uid140matchlist.isEmpty() && uid199matchlist.isEmpty()){
            int max = IntStream
                    .concat(IntStream.of(uid140usercards), IntStream.of(uid199usercards))
                    .max()
                    .getAsInt();
            System.out.println("the max"+max);
            if (Arrays.stream(uid140usercards).anyMatch(value ->value==max)){
                System.out.println("The uid 140 the game ");
                return new Winninghandresponse("The uid 140 has won the game "+uid140matchlist+" against uid 199 "+uid199matchlist,PLAYER_ONE+" The winning hand idex "+uid140matchlist);
            }
            if (Arrays.stream(uid199usercards).anyMatch(value -> value==max )){
                System.out.println("THe uid 199 player has won the game ");
                return new Winninghandresponse("THe uid 199  has won the game "+uid199matchlist+ " against uid 140 "+uid140matchlist,PLAYER_TWO+" The winning hand idex "+uid199matchlist);
            }

            System.out.println("uid 140"+uid140matchlist);
            System.out.println("uid 199 "+uid199matchlist);
//            return winninghandrequests.size();

//            return "They did not match any winning hand";
        }
        System.out.println("May be one is empty and the other is not ");

        System.out.println("uid 140 player"+uid140matchlist);
        System.out.println("uid 199 player "+uid199matchlist);


        if (uid140matchlist.isEmpty() && !uid199matchlist.isEmpty()){
            System.out.println("THe uid 140 PLayer cards array do not match with any winning hand while the uid 199 player  does");

            System.out.println("The uid 140 cards"+uid140matchlist);
            System.out.println("The uid 199 cards"+uid199matchlist);

            return new Winninghandresponse("PLayer two uid 199 has won the game "+uid199matchlist+" against player one uid 140 "+uid140matchlist,PLAYER_TWO+" The winning hand idex "+uid199matchlist);
        }
        if (!uid140matchlist.isEmpty() && uid199matchlist.isEmpty() ){
            System.out.println("The first player uid 140 matches cards array with any of the winning hand while player  two uid 199 does not  ");

            System.out.println("uid 140 "+uid140matchlist);
            System.out.println("uid 199 "+uid199matchlist);

            return new Winninghandresponse("PLayer one  uid 140 has won the game "+uid140matchlist+" against player two"+uid199matchlist,PLAYER_ONE+" The winning hand idex "+uid140matchlist);
        }
        if (uid140matchlist.get(0)<uid199matchlist.get(0)){
//            System.out.println(winninghandrequests.get(0).get(0).getUser().getUID());
            System.out.println("uid 140 won playeruid140matchlist"+uid140matchlist);
            System.out.println("uid 199 lost player uid199matchlist "+uid199matchlist);
            System.out.println("PLayer One uid 140 has won the game");
            return new Winninghandresponse("Player one uid 140 has won "+uid140matchlist+" against player two uid 199 "+uid199matchlist,PLAYER_ONE+" The winning hand idex "+uid140matchlist);

        }else if(uid199matchlist.get(0)<uid140matchlist.get(0)){
            System.out.println("Player two uid 199 has won the game");
            System.out.println("uid 140 lost playeruid140matchlist"+uid140matchlist);
            System.out.println("uid 199 won player uid199matchlist "+uid199matchlist);
            return new Winninghandresponse("PLayer two uid 199 has won "+uid199matchlist+" against uid 140 "+uid140matchlist,PLAYER_TWO+" The winning hand idex "+uid199matchlist);

        }


        if ( uid140matchlist.get(0).equals(uid199matchlist.get(0))){
            AtomicReference<Integer> uid40sum= new AtomicReference<>(0);
            AtomicReference<Integer> uid199sum= new AtomicReference<>(0);
            Arrays.stream(uid140usercards).forEach(value -> uid40sum.updateAndGet(v -> v + value));
            Arrays.stream(uid199usercards).forEach(value -> uid199sum.updateAndGet(v -> v + value));
            System.out.println("uid40sum "+uid40sum);
            System.out.println("uid199sum "+uid199sum);

//            return null;
            if (uid40sum.get()>uid199sum.get()){
                System.out.println("The first player uid 140 has won the game when both cards are equal ");
                return new Winninghandresponse("The first player  uid 140 has won the game:This is when both cards are equal "+uid140matchlist+" and uid 140 usercards are=>+"+Arrays.toString(uid140usercards)+"+ against  uid 199 "+uid199matchlist+" whose usecards are "+Arrays.toString(uid199usercards),PLAYER_ONE+" The winning hand idex "+uid140matchlist);
            }
            if (uid199sum.get()>uid40sum.get()){
                System.out.println("THe second player uid 199 has won the game:This is when both cards are equal ");
                return new Winninghandresponse("THe second player uid 199 has won the game:This is when both cards are equal "+uid199matchlist+" and uid 199 cards are "+Arrays.toString(uid199usercards)+" against uid 140 "+uid140matchlist+" whose usercards are "+Arrays.toString(uid140usercards),PLAYER_TWO+" The winning hand idex "+uid199matchlist);
            }
        }
        return null;
//        return winninghandrequests.size();
    }



    class  Otherwinninghandcominations{



//        Two Gold Pair: 13,13,12,12,x | 13,13,11,11,x | 12,12,11,11,x
        /** Gold = 13,12,11
         Silver = 10,9,8,7,6
         Bronze = 5,4,3,2,1*/
        int[] twogoldpairs2={13,13,11,11};
        int[] twogoldpairs3={12,12,11,11};


        /** Gold = 13,12,11
         Silver = 10,9,8,7,6
         Bronze = 5,4,3,2,1*/
//        Silver High Full House (2 gold pair and 3 silvers pair): 13,13,10,10,10 | 13 13 9 9 9 | 13,13,8,8,8 | 13 13 7 7 7 | 13,13, 6,6,6 | 11 11 6 6 6 | 11 11 7 7 7  | `11 11 8 8 8 | 11 11 9
        int[] SilverHighFullHousetwo={13,13,9,9,9};
        int[] SilverHighFullHousethree={13,13,8,8,8};
        int[] SilverHighFullHousefour={13,13,7,7,7};
        int[] SilverHighFullHousefive={13,13, 6,6,6 };
        int[] SilverHighFullHousesix={11, 11, 6, 6, 6 };
        int[] SilverHighFullHouseseven={11,11,7,7,7};
        int[] SilverHighFullHouseeight={11,11,8,8,8};
        int[] SilverHighFullHousenine={11,11,9,9,9};
        int[] SilverHighFullHouseten={11,11,10,10,10};
        int[] SilverHighFullHouseeleven={12,12,6,6,6};
        int[] SilverHighFullHouse12={12,12,7,7,7};
        int[] SilverHighFullHouse13={12,12, 8, 8, 8 };
        int[] SilverHighFullHouse14={12,12,9,9,9};
        int[] SilverHighFullHouse15={12,12,10,10,10};


//        Silver Full House (3 silver pair, 2 silver pair)
        /** Gold = 13,12,11
         Silver = 10,9,8,7,6
         Bronze = 5,4,3,2,1*/
        /**editedsilverHighfullhouse*/
        int[] SilverHighFullHouse1={10,10,10,9,9};
        int[] SilverHighFullHouse2={10,10,10,8,8};
        int[] SilverHighFullHouse3={10,10,10,7,7};
        int[] SilverHighFullHouse41={10,10,10,6,6};
        int[] SilverHighFullHouse4={9,9,9,10,10};
        int[] SilverHighFullHouse5={9,9,9,8,8};
        int[] SilverHighFullHouse6={9,9,9,7,7};
        int[] SilverHighFullHouse7={9,9,9,6,6};
        int[] SilverHighFullHouse8={8,8,8,10,10};
        int[] SilverHighFullHouse9={8,8,8,9,9};
        int[] SilverHighFullHouse10={8,8,8,7,7};
        int[] SilverHighFullHouse11={8,8,8,6,6};
        int[] SilverHighFullHouse122={7,7,7,10,10};
        int[] SilverHighFullHouse133={7,7,7,9,9};
        int[] SilverHighFullHouse144={7,7,7,8,8};
        int[] SilverHighFullHouse155={7,7,7,6,6};
        int[] SilverHighFullHouse166={6,6,6,10,10};
        int[] SilverHighFullHouse177={6,6,6,9,9};
        int[] SilverHighFullHouse188={6,6,6,8,8};
        int[] SilverHighFullHouse199={6,6,6,7,7};

        /**end of edited silverhighfulllhouse**/
       /** int[] SilverHighFullHouse16={10,10,10,8,8};
        int[] SilverHighFullHouse17={10,10,10,7,7};
        int[] SilverHighFullHouse18={10,10,10,6,6};
        int[] SilverHighFullHouse19={10,10,10,5,5};
        int[] SilverHighFullHouse20={9,9,9,10,10};
        int[] SilverHighFullHouse21={9,9,9,8,8};
        int[] SilverHighFullHouse22={9,9,9,7,7};
        int[] SilverHighFullHouse23={9,9,9,6,6};
        int[] SilverHighFullHouse24={9,9,9,5,5};*/


        /** Gold = 13,12,11
         Silver = 10,9,8,7,6
         Bronze = 5,4,3,2,1*/
//        Two Silver Pair (2 silver pair, 2 silver pair): 10, 10, 9, 9, x

        int[] TwoSilverPair1={10, 10, 8, 8};
        int[] TwoSilverPair2={10, 10, 7, 7};
        int[] TwoSilverPair3={10, 10, 6, 6};
        int[] TwoSilverPair4={9, 9, 8, 8};
        int[] TwoSilverPair5={9, 9, 7, 7};
        int[] TwoSilverPair6={9, 9, 6, 6};
        int[] TwoSilverPair7={8, 8, 7, 7};
        int[] TwoSilverPair8={8, 8, 6, 6};
        int[] TwoSilverPair9={7, 7, 6, 6};


        int[]  GoldPair1={12,12};
        int[]  GoldPair2={11,11};

        int[] threesilverpairtwobronzepair1={10,10,10,4,4};
        int[] threesilverpairtwobronzepair2={10,10,10,3,3};
        int[] threesilverpairtwobronzepair3={10,10,10,2,2};
        int[] threesilverpairtwobronzepair4={10,10,10,1,1};

        int[] threesilverpairtwobronzepair5={9,9,9,5,5};
        int[] threesilverpairtwobronzepair6={9,9,9,4,4};
        int[] threesilverpairtwobronzepair7={9,9,9,3,3};
        int[] threesilverpairtwobronzepair8={9,9,9,2,2};
        int[] threesilverpairtwobronzepair9={9,9,9,1,1};

        int[] threesilverpairtwobronzepair10={8,8,8,5,5};
        int[] threesilverpairtwobronzepair11={8,8,8,4,4};
        int[] threesilverpairtwobronzepair12={8,8,8,3,3};
        int[] threesilverpairtwobronzepair13={8,8,8,2,2};
        int[] threesilverpairtwobronzepair14={8,8,8,1,1};

        int[] threesilverpairtwobronzepair15={7,7,7,5,5};
        int[] threesilverpairtwobronzepair16={7,7,7,4,4};
        int[] threesilverpairtwobronzepair17={7,7,7,3,3};
        int[] threesilverpairtwobronzepair18={7,7,7,2,2};
        int[] threesilverpairtwobronzepair19={7,7,7,1,1};

        int[] threesilverpairtwobronzepair20={6,6,6,5,5};
        int[] threesilverpairtwobronzepair21={6,6,6,4,4};
        int[] threesilverpairtwobronzepair22={6,6,6,3,3};
        int[] threesilverpairtwobronzepair23={6,6,6,2,2};
        int[] threesilverpairtwobronzepair24={6,6,6,1,1};



        int[] threebronzepairtwobronzepair1={5,5,5,3,3};
        int[] threebronzepairtwobronzepair2={5,5,5,2,2};
        int[] threebronzepairtwobronzepair3={5,5,5,1,1};
        int[] threebronzepairtwobronzepair4={4,4,4,5,5};
        int[] threebronzepairtwobronzepair5={4,4,4,3,3};
        int[] threebronzepairtwobronzepair6={4,4,4,2,2};
        int[] threebronzepairtwobronzepair7={4,4,4,1,1};
        int[] threebronzepairtwobronzepair8={3,3,3,5,5};
        int[] threebronzepairtwobronzepair9={3,3,3,4,4};
        int[] threebronzepairtwobronzepair10={3,3,3,2,2};
        int[] threebronzepairtwobronzepair11={3,3,3,1,1};
        int[] threebronzepairtwobronzepair12={2,2,2,5,5};
        int[] threebronzepairtwobronzepair13={2,2,2,4,4};
        int[] threebronzepairtwobronzepair14={2,2,2,3,3};
        int[] threebronzepairtwobronzepair15={2,2,2,1,1};
        int[] threebronzepairtwobronzepair16={1,1,1,5,5};
        int[] threebronzepairtwobronzepair17={1,1,1,4,4};
        int[] threebronzepairtwobronzepair18={1,1,1,3,3};
        int[] threebronzepairtwobronzepair19={1,1,1,2,2};


        int[] silverthreeofakind1={9,9,9};
        int[] silverthreeofakind2={8,8,8};
        int[] silverthreeofakind3={7,7,7};
        int[] silverthreeofakind4={6,6,6};


        int[] bronzefourofakind1={4,4,4,4};
        int[] bronzefourofakind2={3,3,3,3};
        int[] bronzefourofakind3={2,2,2,2};
        int[] bronzefourofakind4={1,1,1,1};


//        Bronze High Full House (Two silver pair, 3 bronze pair): 10,10,5,5,5
        int[] twosilverpairthreebronzepair1={10,10,4,4,4};
        int[] twosilverpairthreebronzepair2={10,10,3,3,3};
        int[] twosilverpairthreebronzepair3={10,10,2,2,2};
        int[] twosilverpairthreebronzepair4={10,10,1,1,1};

        int[] twosilverpairthreebronzepair5={9,9,5,5,5};
        int[] twosilverpairthreebronzepair6={9,9,4,4,4};
        int[] twosilverpairthreebronzepair7={9,9,3,3,3};
        int[] twosilverpairthreebronzepair8={9,9,2,2,2};
        int[] twosilverpairthreebronzepair9={9,9,1,1,1};

        int[] twosilverpairthreebronzepair10={8,8,5,5,5};
        int[] twosilverpairthreebronzepair11={8,8,4,4,4};
        int[] twosilverpairthreebronzepair12={8,8,3,3,3};
        int[] twosilverpairthreebronzepair13={8,8,2,2,2};
        int[] twosilverpairthreebronzepair14={8,8,1,1,1};

        int[] twosilverpairthreebronzepair15={7,7,5,5,5};
        int[] twosilverpairthreebronzepair16={7,7,4,4,4};
        int[] twosilverpairthreebronzepair17={7,7,3,3,3};
        int[] twosilverpairthreebronzepair18={7,7,2,2,2};
        int[] twosilverpairthreebronzepair19={7,7,1,1,1};

        int[] twosilverpairthreebronzepair20={6,6,5,5,5};
        int[] twosilverpairthreebronzepair21={6,6,4,4,4};
        int[] twosilverpairthreebronzepair22={6,6,3,3,3};
        int[] twosilverpairthreebronzepair23={6,6,2,2,2};
        int[] twosilverpairthreebronzepair24={6,6,1,1,1};

        /** Gold = 13,12,11
         Silver = 10,9,8,7,6
         Bronze = 5,4,3,2,1*/
//        1 bronze pair, 1 silver pair: 10,10, 2, 2, x
        int[] onebronzepaironesilverpair1={5,5,10,10};
        int[] onebronzepaironesilverpair2={5,5,9,9};
        int[] onebronzepaironesilverpair3={5,5,8,8};
        int[] onebronzepaironesilverpair4={5,5,7,7};
        int[] onebronzepaironesilverpair5={5,5,6,6};

        int[] onebronzepaironesilverpair6={4,4,10,10};
        int[] onebronzepaironesilverpair7={4,4,9,9};
        int[] onebronzepaironesilverpair8={4,4,8,8};
        int[] onebronzepaironesilverpair9={4,4,7,7};
        int[] onebronzepaironesilverpair10={4,4,6,6};

        int[] onebronzepaironesilverpair11={3,3,10,10};
        int[] onebronzepaironesilverpair12={3,3,9,9};
        int[] onebronzepaironesilverpair13={3,3,8,8};
        int[] onebronzepaironesilverpair14={3,3,7,7};
        int[] onebronzepaironesilverpair15={3,3,6,6};

//        int[] onebronzepaironesilverpair16={2,2,10,10};
        int[] onebronzepaironesilverpair16={2,2,9,9};
        int[] onebronzepaironesilverpair17={2,2,8,8};
        int[] onebronzepaironesilverpair18={2,2,7,7};
        int[] onebronzepaironesilverpair19={2,2,6,6};

        /** Gold = 13,12,11
         Silver = 10,9,8,7,6
         Bronze = 5,4,3,2,1*/
//        Silver Pair (two silver pair): 10,10, x, y, z
//        int[] twosilverpair1={10,10};
        int[] twosilverpair2={9,9};
        int[] twosilverpair3={8,8};
        int[] twosilverpair4={7,7};
        int[] twosilverpair5={6,6};


//        Two Bronze Pair (2 bronze pair, 2 bronze pair): 5,5,2,2,x
        /** Gold = 13,12,11
         Silver = 10,9,8,7,6
         Bronze = 5,4,3,2,1*/


        int[] twobronzepair2bronzepair1={5,5,4,4};
        int[] twobronzepair2bronzepair2={5,5,3,3};
//        int[] twobronzepair2bronzepair3={5,5,2,2};
        int[] twobronzepair2bronzepair3={5,5,1,1};
        int[] twobronzepair2bronzepair4={4,4,3,3};
        int[] twobronzepair2bronzepair5={4,4,2,2};
        int[] twobronzepair2bronzepair6={4,4,1,1};
        int[] twobronzepair2bronzepair7={3,3,2,2};
        int[] twobronzepair2bronzepair8={3,3,1,1};
        int[] twobronzepair2bronzepair9={2,2,1,1};


        /** Gold = 13,12,11
         Silver = 10,9,8,7,6
         Bronze = 5,4,3,2,1*/
//        Bronze 3 of a Kind (Three bronze pair): 5,5,5,x,y
        int[] bronzethreeofakind1={4,4,4};
        int[] bronzethreeofakind2={3,3,3};
        int[] bronzethreeofakind3={2,2,2};
        int[] bronzethreeofakind4={1,1,1};

//        Bronze Pair (Two bronze pair): 5,5,x,y,z
        /** Gold = 13,12,11
         Silver = 10,9,8,7,6
         Bronze = 5,4,3,2,1*/

        int[] bronzepair1={4,4};
        int[] bronzepair2={3,3};
        int[] bronzepair3={2,2};
        int[] bronzepair4={1,1};















        Map<Integer,Integer> twogoldpairmap2=new HashMap<>(0);
        Map<Integer,Integer> twogoldpairmap3=new HashMap<>(0);

        Map<Integer,Integer> SilverHighFullHousetwomap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHousethreemap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHousefourmap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHousefivemap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHousesixmap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHousesevenmap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouseeightmap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouseninemap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHousetenmap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouseelevenmap=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse12map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse13map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse14map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse15map=new HashMap<>(0);

       /** Map<Integer,Integer> SilverHighFullHouse16map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse17map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse18map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse19map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse20map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse21map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse22map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse23map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse24map=new HashMap<>(0);*/
       /**new code to replace the above*/

        Map<Integer,Integer> SilverHighFullHouse1map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse2map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse3map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse41map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse4map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse5map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse6map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse7map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse8map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse9map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse10map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse11map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse122map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse133map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse144map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse155map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse166map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse177map=new HashMap<>(0);
        Map<Integer,Integer> SilverHighFullHouse188map=new HashMap<>(0);

        /**end of new code to replace the commented block above**/

        Map<Integer,Integer> TwoSilverPair1map=new HashMap<>(0);
        Map<Integer,Integer> TwoSilverPair2map=new HashMap<>(0);
        Map<Integer,Integer> TwoSilverPair3map=new HashMap<>(0);
        Map<Integer,Integer> TwoSilverPair4map=new HashMap<>(0);
        Map<Integer,Integer> TwoSilverPair5map=new HashMap<>(0);
        Map<Integer,Integer> TwoSilverPair6map=new HashMap<>(0);
        Map<Integer,Integer> TwoSilverPair7map=new HashMap<>(0);
        Map<Integer,Integer> TwoSilverPair8map=new HashMap<>(0);
        Map<Integer,Integer> TwoSilverPair9map=new HashMap<>(0);

        Map<Integer,Integer> GoldPair1map=new HashMap<>(0);
        Map<Integer,Integer> GoldPair2map=new HashMap<>(0);

        Map<Integer,Integer> threesilverpairtwobronzepair1map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair2map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair3map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair4map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair5map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair6map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair7map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair8map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair9map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair10map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair11map=new HashMap<>(0);
//        Map<Integer,Integer> threeSilverpairtwobronzepair11map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair12map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair13map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair14map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair15map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair16map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair17map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair18map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair19map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair20map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair21map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair22map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair23map=new HashMap<>(0);
        Map<Integer,Integer> threesilverpairtwobronzepair24map=new HashMap<>(0);




        Map<Integer,Integer> threebronzepairtwobronzepair1map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair2map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair3map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair4map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair5map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair6map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair7map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair8map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair9map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair10map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair11map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair12map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair13map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair14map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair15map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair16map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair17map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair18map=new HashMap<>(0);
        Map<Integer,Integer> threebronzepairtwobronzepair19map=new HashMap<>(0);



        /** Gold = 13,12,11
         Silver = 10,9,8,7,6
         Bronze = 5,4,3,2,1*/
        Map<Integer,Integer> silverthreeofakind1map=new HashMap<>(0);
        Map<Integer,Integer> silverthreeofakind2map=new HashMap<>(0);
        Map<Integer,Integer> silverthreeofakind3map=new HashMap<>(0);
        Map<Integer,Integer> silverthreeofakind4map=new HashMap<>(0);

        Map<Integer,Integer> bronzefourofakind1map=new HashMap<>(0);
        Map<Integer,Integer> bronzefourofakind2map=new HashMap<>(0);
        Map<Integer,Integer> bronzefourofakind3map=new HashMap<>(0);
        Map<Integer,Integer> bronzefourofakind4map=new HashMap<>(0);


        Map<Integer,Integer> twosilverpairthreebronzepair1map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair2map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair3map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair4map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair5map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair6map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair7map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair8map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair9map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair10map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair11map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair12map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair13map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair14map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair15map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair16map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair17map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair18map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair19map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair20map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair21map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair22map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair23map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpairthreebronzepair24map=new HashMap<>(0);



        Map<Integer,Integer> onebronzepaironesilverpair1map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair2map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair3map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair4map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair5map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair6map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair7map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair8map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair9map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair10map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair11map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair12map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair13map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair14map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair15map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair16map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair17map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair18map=new HashMap<>(0);
        Map<Integer,Integer> onebronzepaironesilverpair19map=new HashMap<>(0);


        Map<Integer,Integer> twosilverpair2map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpair3map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpair4map=new HashMap<>(0);
        Map<Integer,Integer> twosilverpair5map=new HashMap<>(0);


        Map<Integer,Integer> twobronzepair2bronzepair1map=new HashMap<>(0);
        Map<Integer,Integer> twobronzepair2bronzepair2map=new HashMap<>(0);
        Map<Integer,Integer> twobronzepair2bronzepair3map=new HashMap<>(0);
        Map<Integer,Integer> twobronzepair2bronzepair4map=new HashMap<>(0);
        Map<Integer,Integer> twobronzepair2bronzepair5map=new HashMap<>(0);
        Map<Integer,Integer> twobronzepair2bronzepair6map=new HashMap<>(0);
        Map<Integer,Integer> twobronzepair2bronzepair7map=new HashMap<>(0);
        Map<Integer,Integer> twobronzepair2bronzepair8map=new HashMap<>(0);
        Map<Integer,Integer> twobronzepair2bronzepair9map=new HashMap<>(0);

        Map<Integer,Integer> bronzethreeofakind1map=new HashMap<>(0);
        Map<Integer,Integer> bronzethreeofakind2map=new HashMap<>(0);
        Map<Integer,Integer> bronzethreeofakind3map=new HashMap<>(0);
        Map<Integer,Integer> bronzethreeofakind4map=new HashMap<>(0);



        Map<Integer,Integer> bronzepair1map1=new HashMap<>(0);
        Map<Integer,Integer> bronzepair2map2=new HashMap<>(0);
        Map<Integer,Integer> bronzepair3map3=new HashMap<>(0);
        Map<Integer,Integer> bronzepair4map4=new HashMap<>(0);













        public Map<Integer,Integer> twogoldpairfunctiontwo(){
            Arrays.stream(twogoldpairs2).forEach(value -> twogoldpairmap2.put(value,twogoldpairmap2.getOrDefault(value,0)+1));
            return twogoldpairmap2;
        }
        public Map<Integer,Integer> twogoldpairfunctionthree(){
            Arrays.stream(twogoldpairs3).forEach(value -> twogoldpairmap3.put(value,twogoldpairmap3.getOrDefault(twogoldpairmap3,0)+1));
            return twogoldpairmap3;
        }
        public Map<Integer,Integer> SilverHighFullHousetwomapfunction(){
            Arrays.stream(SilverHighFullHousetwo).forEach(value -> SilverHighFullHousetwomap.put(value,SilverHighFullHousetwomap.getOrDefault(value,0)+1));
            return SilverHighFullHousetwomap;
        }
        public Map<Integer,Integer> SilverHighFullHousethreemapfunction(){
            Arrays.stream(SilverHighFullHousethree).forEach(value -> SilverHighFullHousethreemap.put(value,SilverHighFullHousethreemap.getOrDefault(value,0)+1));
            return SilverHighFullHousethreemap;
        }
        public Map<Integer,Integer> SilverHighFullHousefourmapfunction(){
            Arrays.stream(SilverHighFullHousefour).forEach(value -> SilverHighFullHousefourmap.put(value,SilverHighFullHousefourmap.getOrDefault(value,0)+1));
            return SilverHighFullHousefourmap;
        }
        public Map<Integer,Integer> SilverHighFullHousefivemapfunction(){
            Arrays.stream(SilverHighFullHousefive).forEach(value -> SilverHighFullHousefivemap.put(value,SilverHighFullHousefivemap.getOrDefault(value,0)+1));
            return SilverHighFullHousefivemap;
        }
        public Map<Integer,Integer> SilverHighFullHousesixmapfunction(){
            Arrays.stream(SilverHighFullHousesix).forEach(value -> SilverHighFullHousesixmap.put(value,SilverHighFullHousesixmap.getOrDefault(value,0)+1));
            return SilverHighFullHousesixmap;
        }
        public Map<Integer,Integer> SilverHighFullHousesevenmapfunction(){
            Arrays.stream(SilverHighFullHouseseven).forEach(value -> SilverHighFullHousesevenmap.put(value,SilverHighFullHousesevenmap.getOrDefault(value,0)+1));
            return SilverHighFullHousesevenmap;
        }
        public Map<Integer,Integer> SilverHighFullHouseeightmapfunction(){
            Arrays.stream(SilverHighFullHouseeight).forEach(value -> SilverHighFullHouseeightmap.put(value,SilverHighFullHouseeightmap.getOrDefault(value,0)+1));
            return SilverHighFullHouseeightmap;
        }
        public Map<Integer,Integer> SilverHighFullHouseninemapfunction(){
            Arrays.stream(SilverHighFullHousenine).forEach(value -> SilverHighFullHouseninemap.put(value,SilverHighFullHouseninemap.getOrDefault(value,0)+1));
            return SilverHighFullHouseninemap;
        }
        public Map<Integer,Integer> SilverHighFullHousetenmapfunction(){
            Arrays.stream(SilverHighFullHouseten).forEach(value -> SilverHighFullHousetenmap.put(value,SilverHighFullHousetenmap.getOrDefault(value,0)+1));
            return SilverHighFullHousetenmap;
        }
        public Map<Integer,Integer> SilverHighFullHouseelevenmapfunction(){
            Arrays.stream(SilverHighFullHouseeleven).forEach(value -> SilverHighFullHouseelevenmap.put(value,SilverHighFullHouseelevenmap.getOrDefault(value,0)+1));
            return SilverHighFullHouseelevenmap;
        }
        public Map<Integer,Integer> SilverHighFullHouse12mapfunction(){
            Arrays.stream(SilverHighFullHouse12).forEach(value -> SilverHighFullHouse12map.put(value,SilverHighFullHouse12map.getOrDefault(value,0)+1));
            return SilverHighFullHouse12map;
        }
        public Map<Integer,Integer> SilverHighFullHouse13mapfunction(){
            Arrays.stream(SilverHighFullHouse13).forEach(value -> SilverHighFullHouse13map.put(value,SilverHighFullHouse13map.getOrDefault(value,0)+1));
            return SilverHighFullHouse13map;
        }
        public Map<Integer,Integer> SilverHighFullHouse14mapfunction(){
            Arrays.stream(SilverHighFullHouse14).forEach(value -> SilverHighFullHouse14map.put(value,SilverHighFullHouse14map.getOrDefault(value,0)+1));
            return SilverHighFullHouse14map;
        }
        public Map<Integer,Integer> SilverHighFullHouse15mapfunction(){
            Arrays.stream(SilverHighFullHouse15).forEach(value -> SilverHighFullHouse15map.put(value,SilverHighFullHouse15map.getOrDefault(value,0)+1));
            return SilverHighFullHouse15map;
        }
       /** public Map<Integer,Integer> SilverHighFullHouse16mapfunction(){
            Arrays.stream(SilverHighFullHouse16).forEach(value -> SilverHighFullHouse16map.put(value,SilverHighFullHouse16map.getOrDefault(value,0)+1));
            return SilverHighFullHouse16map;
        }
        public Map<Integer,Integer> SilverHighFullHouse17mapfunction(){
            Arrays.stream(SilverHighFullHouse17).forEach(value -> SilverHighFullHouse17map.put(value,SilverHighFullHouse17map.getOrDefault(value,0)+1));
            return SilverHighFullHouse17map;
        }
        public Map<Integer,Integer> SilverHighFullHouse18mapfunction(){
            Arrays.stream(SilverHighFullHouse18).forEach(value -> SilverHighFullHouse18map.put(value,SilverHighFullHouse18map.getOrDefault(value,0)+1));
            return SilverHighFullHouse18map;
        }
        public Map<Integer,Integer> SilverHighFullHouse19mapfunction(){
            Arrays.stream(SilverHighFullHouse19).forEach(value -> SilverHighFullHouse19map.put(value,SilverHighFullHouse19map.getOrDefault(value,0)+1));
            return SilverHighFullHouse19map;
        }
        public Map<Integer,Integer> SilverHighFullHouse20mapfunction(){
            Arrays.stream(SilverHighFullHouse20).forEach(value -> SilverHighFullHouse20map.put(value,SilverHighFullHouse20map.getOrDefault(value,0)+1));
            return SilverHighFullHouse20map;
        }
        public Map<Integer,Integer> SilverHighFullHouse21mapfunction(){
            Arrays.stream(SilverHighFullHouse21).forEach(value -> SilverHighFullHouse21map.put(value,SilverHighFullHouse21map.getOrDefault(value,0)+1));
            return SilverHighFullHouse21map;
        }
        public Map<Integer,Integer> SilverHighFullHouse22mapfunction(){
            Arrays.stream(SilverHighFullHouse22).forEach(value -> SilverHighFullHouse22map.put(value,SilverHighFullHouse22map.getOrDefault(value,0)+1));
            return SilverHighFullHouse22map;
        }
        public Map<Integer,Integer> SilverHighFullHouse23mapfunction(){
            Arrays.stream(SilverHighFullHouse23).forEach(value -> SilverHighFullHouse23map.put(value,SilverHighFullHouse23map.getOrDefault(value,0)+1));
            return SilverHighFullHouse23map;
        }
        public Map<Integer,Integer> SilverHighFullHouse24mapfunction(){
            Arrays.stream(SilverHighFullHouse24).forEach(value -> SilverHighFullHouse24map.put(value,SilverHighFullHouse24map.getOrDefault(value,0)+1));
            return SilverHighFullHouse24map;
        }*/
       /**new code to replace the block above**/
       public Map<Integer,Integer> SilverHighFullHouse1mapfunction(){
           Arrays.stream(SilverHighFullHouse1).forEach(value -> SilverHighFullHouse1map.put(value,SilverHighFullHouse1map.getOrDefault(value,0)+1));
           return SilverHighFullHouse1map;
       }
       public Map<Integer,Integer> SilverHighFullHouse2mapfunction(){
           Arrays.stream(SilverHighFullHouse2).forEach(value -> SilverHighFullHouse2map.put(value,SilverHighFullHouse2map.getOrDefault(value,0)+1));
           return SilverHighFullHouse2map;
       }
       public Map<Integer,Integer> SilverHighFullHouse3mapfunction(){
           Arrays.stream(SilverHighFullHouse3).forEach(value -> SilverHighFullHouse3map.put(value,SilverHighFullHouse3map.getOrDefault(value,0)+1));
           return SilverHighFullHouse3map;
       }
       public Map<Integer,Integer> SilverHighFullHouse41mapfunction(){
           Arrays.stream(SilverHighFullHouse41).forEach(value -> SilverHighFullHouse41map.put(value,SilverHighFullHouse41map.getOrDefault(value,0)+1));
           return SilverHighFullHouse41map;
       }
       public Map<Integer,Integer> SilverHighFullHouse4mapfunction(){
           Arrays.stream(SilverHighFullHouse4).forEach(value -> SilverHighFullHouse4map.put(value,SilverHighFullHouse4map.getOrDefault(value,0)+1));
           return SilverHighFullHouse4map;
       }
       public Map<Integer,Integer> SilverHighFullHouse5mapfunction(){
           Arrays.stream(SilverHighFullHouse5).forEach(value -> SilverHighFullHouse5map.put(value,SilverHighFullHouse5map.getOrDefault(value,0)+1));
           return SilverHighFullHouse5map;
       }
       public Map<Integer,Integer> SilverHighFullHouse6mapfunction(){
           Arrays.stream(SilverHighFullHouse6).forEach(value -> SilverHighFullHouse6map.put(value,SilverHighFullHouse6map.getOrDefault(value,0)+1));
           return SilverHighFullHouse6map;
       }
       public Map<Integer,Integer> SilverHighFullHouse7mapfunction(){
           Arrays.stream(SilverHighFullHouse7).forEach(value -> SilverHighFullHouse7map.put(value,SilverHighFullHouse7map.getOrDefault(value,0)+1));
           return SilverHighFullHouse7map;
       }
       public Map<Integer,Integer> SilverHighFullHouse8mapfunction(){
           Arrays.stream(SilverHighFullHouse8).forEach(value -> SilverHighFullHouse8map.put(value,SilverHighFullHouse8map.getOrDefault(value,0)+1));
           return SilverHighFullHouse8map;
       }
       public Map<Integer,Integer> SilverHighFullHouse9mapfunction(){
           Arrays.stream(SilverHighFullHouse9).forEach(value -> SilverHighFullHouse9map.put(value,SilverHighFullHouse9map.getOrDefault(value,0)+1));
           return SilverHighFullHouse9map;
       }
       public Map<Integer,Integer> SilverHighFullHouse10mapfunction(){
           Arrays.stream(SilverHighFullHouse10).forEach(value -> SilverHighFullHouse10map.put(value,SilverHighFullHouse10map.getOrDefault(value,0)+1));
           return SilverHighFullHouse10map;
       }
       public Map<Integer,Integer> SilverHighFullHouse11mapfunction(){
           Arrays.stream(SilverHighFullHouse11).forEach(value -> SilverHighFullHouse11map.put(value,SilverHighFullHouse11map.getOrDefault(value,0)+1));
           return SilverHighFullHouse11map;
       }
       public Map<Integer,Integer> SilverHighFullHouse122mapfunction(){
           Arrays.stream(SilverHighFullHouse122).forEach(value -> SilverHighFullHouse122map.put(value,SilverHighFullHouse122map.getOrDefault(value,0)+1));
           return SilverHighFullHouse122map;
       }
       public Map<Integer,Integer> SilverHighFullHouse133mapfunction(){
           Arrays.stream(SilverHighFullHouse133).forEach(value -> SilverHighFullHouse133map.put(value,SilverHighFullHouse133map.getOrDefault(value,0)+1));
           return SilverHighFullHouse133map;
       }
       public Map<Integer,Integer> SilverHighFullHouse144mapfunction(){
           Arrays.stream(SilverHighFullHouse144).forEach(value -> SilverHighFullHouse144map.put(value,SilverHighFullHouse144map.getOrDefault(value,0)+1));
           return SilverHighFullHouse144map;
       }
       public Map<Integer,Integer> SilverHighFullHouse155mapfunction(){
           Arrays.stream(SilverHighFullHouse155).forEach(value -> SilverHighFullHouse155map.put(value,SilverHighFullHouse155map.getOrDefault(value,0)+1));
           return SilverHighFullHouse155map;
       }
       public Map<Integer,Integer> SilverHighFullHouse166mapfunction(){
           Arrays.stream(SilverHighFullHouse166).forEach(value -> SilverHighFullHouse166map.put(value,SilverHighFullHouse166map.getOrDefault(value,0)+1));
           return SilverHighFullHouse166map;
       }
       public Map<Integer,Integer> SilverHighFullHouse177mapfunction(){
           Arrays.stream(SilverHighFullHouse177).forEach(value -> SilverHighFullHouse177map.put(value,SilverHighFullHouse177map.getOrDefault(value,0)+1));
           return SilverHighFullHouse177map;
       }
       public Map<Integer,Integer> SilverHighFullHouse188mapfunction(){
           Arrays.stream(SilverHighFullHouse188).forEach(value -> SilverHighFullHouse188map.put(value,SilverHighFullHouse188map.getOrDefault(value,0)+1));
           return SilverHighFullHouse188map;
       }

       /**End of new code to replace the block above**/

        public Map<Integer,Integer> TwoSilverPair1mapfunction(){
            Arrays.stream(TwoSilverPair1).forEach(value ->TwoSilverPair1map.put(value,TwoSilverPair1map.getOrDefault(value,0)+1));
            return TwoSilverPair1map;
        }
        public Map<Integer,Integer> TwoSilverPair2mapfunction(){
            Arrays.stream(TwoSilverPair2).forEach(value ->TwoSilverPair2map.put(value,TwoSilverPair2map.getOrDefault(value,0)+1));
            return TwoSilverPair2map;
        }
        public Map<Integer,Integer> TwoSilverPair3mapfunction(){
            Arrays.stream(TwoSilverPair3).forEach(value ->TwoSilverPair3map.put(value,TwoSilverPair3map.getOrDefault(value,0)+1));
            return TwoSilverPair3map;
        }
        public Map<Integer,Integer> TwoSilverPair4mapfunction(){
            Arrays.stream(TwoSilverPair4).forEach(value ->TwoSilverPair4map.put(value,TwoSilverPair4map.getOrDefault(value,0)+1));
            return TwoSilverPair4map;
        }
        public Map<Integer,Integer> TwoSilverPair5mapfunction(){
            Arrays.stream(TwoSilverPair5).forEach(value ->TwoSilverPair5map.put(value,TwoSilverPair5map.getOrDefault(value,0)+1));
            return TwoSilverPair5map;
        }
        public Map<Integer,Integer> TwoSilverPair6mapfunction(){
            Arrays.stream(TwoSilverPair6).forEach(value ->TwoSilverPair6map.put(value,TwoSilverPair6map.getOrDefault(value,0)+1));
            return TwoSilverPair6map;
        }
        public Map<Integer,Integer> TwoSilverPair7mapfunction(){
            Arrays.stream(TwoSilverPair7).forEach(value ->TwoSilverPair7map.put(value,TwoSilverPair7map.getOrDefault(value,0)+1));
            return TwoSilverPair7map;
        }
        public Map<Integer,Integer> TwoSilverPair8mapfunction(){
            Arrays.stream(TwoSilverPair8).forEach(value ->TwoSilverPair8map.put(value,TwoSilverPair8map.getOrDefault(value,0)+1));
            return TwoSilverPair8map;
        }
        public Map<Integer,Integer> TwoSilverPair9mapfunction(){
            Arrays.stream(TwoSilverPair9).forEach(value ->TwoSilverPair9map.put(value,TwoSilverPair9map.getOrDefault(value,0)+1));
            return TwoSilverPair9map;
        }

        public Map<Integer,Integer> GoldPair1mapfunction(){
            Arrays.stream(GoldPair1).forEach(value ->GoldPair1map.put(value,GoldPair1map.getOrDefault(value,0)+1));
            return GoldPair1map;
        }
        public Map<Integer,Integer> GoldPair2mapfunction(){
            Arrays.stream(GoldPair2).forEach(value ->GoldPair2map.put(value,GoldPair2map.getOrDefault(value,0)+1));
            return GoldPair2map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair1mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair1).forEach(value ->threesilverpairtwobronzepair1map.put(value,threesilverpairtwobronzepair1map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair1map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair2mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair2).forEach(value ->threesilverpairtwobronzepair2map.put(value,threesilverpairtwobronzepair2map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair2map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair3mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair3).forEach(value ->threesilverpairtwobronzepair3map.put(value,threesilverpairtwobronzepair3map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair3map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair4mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair4).forEach(value ->threesilverpairtwobronzepair4map.put(value,threesilverpairtwobronzepair4map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair4map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair5mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair5).forEach(value ->threesilverpairtwobronzepair5map.put(value,threesilverpairtwobronzepair5map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair5map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair6mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair6).forEach(value ->threesilverpairtwobronzepair6map.put(value,threesilverpairtwobronzepair6map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair6map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair7mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair7).forEach(value ->threesilverpairtwobronzepair7map.put(value,threesilverpairtwobronzepair7map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair7map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair8mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair8).forEach(value ->threesilverpairtwobronzepair8map.put(value,threesilverpairtwobronzepair8map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair8map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair9mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair9).forEach(value ->threesilverpairtwobronzepair9map.put(value,threesilverpairtwobronzepair9map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair9map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair10mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair10).forEach(value ->threesilverpairtwobronzepair10map.put(value,threesilverpairtwobronzepair10map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair10map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair11mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair11).forEach(value ->threesilverpairtwobronzepair11map.put(value,threesilverpairtwobronzepair11map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair11map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair12mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair12).forEach(value ->threesilverpairtwobronzepair12map.put(value,threesilverpairtwobronzepair12map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair12map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair13mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair13).forEach(value ->threesilverpairtwobronzepair13map.put(value,threesilverpairtwobronzepair13map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair13map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair14mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair14).forEach(value ->threesilverpairtwobronzepair14map.put(value,threesilverpairtwobronzepair14map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair14map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair15mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair15).forEach(value ->threesilverpairtwobronzepair15map.put(value,threesilverpairtwobronzepair15map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair15map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair16mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair16).forEach(value ->threesilverpairtwobronzepair16map.put(value,threesilverpairtwobronzepair16map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair16map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair17mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair17).forEach(value ->threesilverpairtwobronzepair17map.put(value,threesilverpairtwobronzepair17map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair17map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair18mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair18).forEach(value ->threesilverpairtwobronzepair18map.put(value,threesilverpairtwobronzepair18map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair18map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair19mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair19).forEach(value ->threesilverpairtwobronzepair19map.put(value,threesilverpairtwobronzepair19map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair19map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair20mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair20).forEach(value ->threesilverpairtwobronzepair20map.put(value,threesilverpairtwobronzepair20map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair20map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair21mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair21).forEach(value ->threesilverpairtwobronzepair21map.put(value,threesilverpairtwobronzepair21map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair21map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair22mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair22).forEach(value ->threesilverpairtwobronzepair22map.put(value,threesilverpairtwobronzepair22map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair22map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair23mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair23).forEach(value ->threesilverpairtwobronzepair23map.put(value,threesilverpairtwobronzepair23map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair23map;
        }
        public Map<Integer,Integer> threesilverpairtwobronzepair24mapfunction(){
            Arrays.stream(threesilverpairtwobronzepair24).forEach(value ->threesilverpairtwobronzepair24map.put(value,threesilverpairtwobronzepair24map.getOrDefault(value,0)+1));
            return threesilverpairtwobronzepair24map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair1mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair1).forEach(value ->threebronzepairtwobronzepair1map.put(value,threebronzepairtwobronzepair1map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair1map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair2mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair2).forEach(value ->threebronzepairtwobronzepair2map.put(value,threebronzepairtwobronzepair2map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair2map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair3mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair3).forEach(value ->threebronzepairtwobronzepair3map.put(value,threebronzepairtwobronzepair3map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair3map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair4mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair4).forEach(value ->threebronzepairtwobronzepair4map.put(value,threebronzepairtwobronzepair4map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair4map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair5mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair5).forEach(value ->threebronzepairtwobronzepair5map.put(value,threebronzepairtwobronzepair5map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair5map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair6mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair6).forEach(value ->threebronzepairtwobronzepair6map.put(value,threebronzepairtwobronzepair6map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair6map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair7mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair7).forEach(value ->threebronzepairtwobronzepair7map.put(value,threebronzepairtwobronzepair7map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair7map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair8mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair8).forEach(value ->threebronzepairtwobronzepair8map.put(value,threebronzepairtwobronzepair8map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair8map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair9mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair9).forEach(value ->threebronzepairtwobronzepair9map.put(value,threebronzepairtwobronzepair9map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair9map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair10mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair10).forEach(value ->threebronzepairtwobronzepair10map.put(value,threebronzepairtwobronzepair10map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair10map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair11mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair11).forEach(value ->threebronzepairtwobronzepair11map.put(value,threebronzepairtwobronzepair11map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair11map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair12mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair12).forEach(value ->threebronzepairtwobronzepair12map.put(value,threebronzepairtwobronzepair12map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair12map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair13mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair13).forEach(value ->threebronzepairtwobronzepair13map.put(value,threebronzepairtwobronzepair13map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair13map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair14mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair14).forEach(value ->threebronzepairtwobronzepair14map.put(value,threebronzepairtwobronzepair14map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair14map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair15mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair15).forEach(value ->threebronzepairtwobronzepair15map.put(value,threebronzepairtwobronzepair15map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair15map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair16mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair16).forEach(value ->threebronzepairtwobronzepair16map.put(value,threebronzepairtwobronzepair16map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair16map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair17mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair17).forEach(value ->threebronzepairtwobronzepair17map.put(value,threebronzepairtwobronzepair17map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair17map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair18mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair18).forEach(value ->threebronzepairtwobronzepair18map.put(value,threebronzepairtwobronzepair18map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair18map;
        }

        public Map<Integer,Integer> threebronzepairtwobronzepair19mapfunction(){
            Arrays.stream(threebronzepairtwobronzepair19).forEach(value ->threebronzepairtwobronzepair19map.put(value,threebronzepairtwobronzepair19map.getOrDefault(value,0)+1));
            return threebronzepairtwobronzepair19map;
        }
        public Map<Integer,Integer> silverthreeofakind1mapfunction(){
            Arrays.stream(silverthreeofakind1).forEach(value ->silverthreeofakind1map.put(value,silverthreeofakind1map.getOrDefault(value,0)+1));
            return silverthreeofakind1map;
        }
        public Map<Integer,Integer> silverthreeofakind2mapfunction(){
            Arrays.stream(silverthreeofakind2).forEach(value ->silverthreeofakind2map.put(value,silverthreeofakind2map.getOrDefault(value,0)+1));
            return silverthreeofakind2map;
        }
        public Map<Integer,Integer> silverthreeofakind3mapfunction(){
            Arrays.stream(silverthreeofakind3).forEach(value ->silverthreeofakind3map.put(value,silverthreeofakind3map.getOrDefault(value,0)+1));
            return silverthreeofakind3map;
        }
        public Map<Integer,Integer> silverthreeofakind4mapfunction(){
            Arrays.stream(silverthreeofakind4).forEach(value ->silverthreeofakind4map.put(value,silverthreeofakind4map.getOrDefault(value,0)+1));
            return silverthreeofakind4map;
        }
        public Map<Integer,Integer> bronzefourofakind1mapfunction(){
            Arrays.stream(bronzefourofakind1).forEach(value ->bronzefourofakind1map.put(value,bronzefourofakind1map.getOrDefault(value,0)+1));
            return bronzefourofakind1map;
        }
        public Map<Integer,Integer> bronzefourofakind2mapfunction(){
            Arrays.stream(bronzefourofakind2).forEach(value ->bronzefourofakind2map.put(value,bronzefourofakind2map.getOrDefault(value,0)+1));
            return bronzefourofakind2map;
        }
        public Map<Integer,Integer> bronzefourofakind3mapfunction(){
            Arrays.stream(bronzefourofakind3).forEach(value ->bronzefourofakind3map.put(value,bronzefourofakind3map.getOrDefault(value,0)+1));
            return bronzefourofakind3map;
        }
        public Map<Integer,Integer> bronzefourofakind4mapfunction(){
            Arrays.stream(bronzefourofakind4).forEach(value ->bronzefourofakind4map.put(value,bronzefourofakind4map.getOrDefault(value,0)+1));
            return bronzefourofakind4map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair1mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair1).forEach(value ->twosilverpairthreebronzepair1map.put(value,twosilverpairthreebronzepair1map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair1map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair2mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair2).forEach(value ->twosilverpairthreebronzepair2map.put(value,twosilverpairthreebronzepair2map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair2map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair3mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair3).forEach(value ->twosilverpairthreebronzepair3map.put(value,twosilverpairthreebronzepair3map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair3map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair4mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair4).forEach(value ->twosilverpairthreebronzepair4map.put(value,twosilverpairthreebronzepair4map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair4map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair5mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair5).forEach(value ->twosilverpairthreebronzepair5map.put(value,twosilverpairthreebronzepair5map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair5map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair6mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair6).forEach(value ->twosilverpairthreebronzepair6map.put(value,twosilverpairthreebronzepair6map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair6map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair7mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair7).forEach(value ->twosilverpairthreebronzepair7map.put(value,twosilverpairthreebronzepair7map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair7map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair8mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair8).forEach(value ->twosilverpairthreebronzepair8map.put(value,twosilverpairthreebronzepair8map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair8map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair9mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair9).forEach(value ->twosilverpairthreebronzepair9map.put(value,twosilverpairthreebronzepair9map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair9map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair10mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair10).forEach(value ->twosilverpairthreebronzepair10map.put(value,twosilverpairthreebronzepair10map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair10map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair11mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair11).forEach(value ->twosilverpairthreebronzepair11map.put(value,twosilverpairthreebronzepair11map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair11map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair12mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair12).forEach(value ->twosilverpairthreebronzepair12map.put(value,twosilverpairthreebronzepair12map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair12map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair13mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair13).forEach(value ->twosilverpairthreebronzepair13map.put(value,twosilverpairthreebronzepair13map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair13map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair14mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair14).forEach(value ->twosilverpairthreebronzepair14map.put(value,twosilverpairthreebronzepair14map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair14map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair15mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair15).forEach(value ->twosilverpairthreebronzepair15map.put(value,twosilverpairthreebronzepair15map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair15map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair16mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair16).forEach(value ->twosilverpairthreebronzepair16map.put(value,twosilverpairthreebronzepair16map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair16map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair17mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair17).forEach(value ->twosilverpairthreebronzepair17map.put(value,twosilverpairthreebronzepair17map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair17map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair18mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair18).forEach(value ->twosilverpairthreebronzepair18map.put(value,twosilverpairthreebronzepair18map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair18map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair19mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair19).forEach(value ->twosilverpairthreebronzepair19map.put(value,twosilverpairthreebronzepair19map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair19map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair20mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair20).forEach(value ->twosilverpairthreebronzepair20map.put(value,twosilverpairthreebronzepair20map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair20map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair21mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair21).forEach(value ->twosilverpairthreebronzepair21map.put(value,twosilverpairthreebronzepair21map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair21map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair22mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair22).forEach(value ->twosilverpairthreebronzepair22map.put(value,twosilverpairthreebronzepair22map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair22map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair23mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair23).forEach(value ->twosilverpairthreebronzepair23map.put(value,twosilverpairthreebronzepair23map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair23map;
        }
        public Map<Integer,Integer> twosilverpairthreebronzepair24mapfunction(){
            Arrays.stream(twosilverpairthreebronzepair24).forEach(value ->twosilverpairthreebronzepair24map.put(value,twosilverpairthreebronzepair24map.getOrDefault(value,0)+1));
            return twosilverpairthreebronzepair24map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair1mapfunction(){
            Arrays.stream(onebronzepaironesilverpair1).forEach(value ->onebronzepaironesilverpair1map.put(value,onebronzepaironesilverpair1map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair1map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair2mapfunction(){
            Arrays.stream(onebronzepaironesilverpair2).forEach(value ->onebronzepaironesilverpair2map.put(value,onebronzepaironesilverpair2map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair2map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair3mapfunction(){
            Arrays.stream(onebronzepaironesilverpair3).forEach(value ->onebronzepaironesilverpair3map.put(value,onebronzepaironesilverpair3map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair3map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair4mapfunction(){
            Arrays.stream(onebronzepaironesilverpair4).forEach(value ->onebronzepaironesilverpair4map.put(value,onebronzepaironesilverpair4map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair4map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair5mapfunction(){
            Arrays.stream(onebronzepaironesilverpair5).forEach(value ->onebronzepaironesilverpair5map.put(value,onebronzepaironesilverpair5map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair5map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair6mapfunction(){
            Arrays.stream(onebronzepaironesilverpair6).forEach(value ->onebronzepaironesilverpair6map.put(value,onebronzepaironesilverpair6map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair6map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair7mapfunction(){
            Arrays.stream(onebronzepaironesilverpair7).forEach(value ->onebronzepaironesilverpair7map.put(value,onebronzepaironesilverpair7map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair7map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair8mapfunction(){
            Arrays.stream(onebronzepaironesilverpair8).forEach(value ->onebronzepaironesilverpair8map.put(value,onebronzepaironesilverpair8map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair8map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair9mapfunction(){
            Arrays.stream(onebronzepaironesilverpair9).forEach(value ->onebronzepaironesilverpair9map.put(value,onebronzepaironesilverpair9map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair9map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair10mapfunction(){
            Arrays.stream(onebronzepaironesilverpair10).forEach(value ->onebronzepaironesilverpair10map.put(value,onebronzepaironesilverpair10map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair10map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair11mapfunction(){
            Arrays.stream(onebronzepaironesilverpair11).forEach(value ->onebronzepaironesilverpair11map.put(value,onebronzepaironesilverpair11map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair11map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair12mapfunction(){
            Arrays.stream(onebronzepaironesilverpair12).forEach(value ->onebronzepaironesilverpair12map.put(value,onebronzepaironesilverpair12map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair12map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair13mapfunction(){
            Arrays.stream(onebronzepaironesilverpair13).forEach(value ->onebronzepaironesilverpair13map.put(value,onebronzepaironesilverpair13map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair13map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair14mapfunction(){
            Arrays.stream(onebronzepaironesilverpair14).forEach(value ->onebronzepaironesilverpair14map.put(value,onebronzepaironesilverpair14map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair14map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair15mapfunction(){
            Arrays.stream(onebronzepaironesilverpair15).forEach(value ->onebronzepaironesilverpair15map.put(value,onebronzepaironesilverpair15map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair15map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair16mapfunction(){
            Arrays.stream(onebronzepaironesilverpair16).forEach(value ->onebronzepaironesilverpair16map.put(value,onebronzepaironesilverpair16map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair16map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair17mapfunction(){
            Arrays.stream(onebronzepaironesilverpair17).forEach(value ->onebronzepaironesilverpair17map.put(value,onebronzepaironesilverpair17map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair17map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair18mapfunction(){
            Arrays.stream(onebronzepaironesilverpair18).forEach(value ->onebronzepaironesilverpair18map.put(value,onebronzepaironesilverpair18map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair18map;
        }

        public Map<Integer,Integer> onebronzepaironesilverpair19mapfunction(){
            Arrays.stream(onebronzepaironesilverpair19).forEach(value ->onebronzepaironesilverpair19map.put(value,onebronzepaironesilverpair19map.getOrDefault(value,0)+1));
            return onebronzepaironesilverpair19map;
        }
        public Map<Integer,Integer> twosilverpair2mapfunction(){
            Arrays.stream(twosilverpair2).forEach(value ->twosilverpair2map.put(value,twosilverpair2map.getOrDefault(value,0)+1));
            return twosilverpair2map;
        }
     public Map<Integer,Integer> twosilverpair3mapfunction(){
            Arrays.stream(twosilverpair3).forEach(value ->twosilverpair3map.put(value,twosilverpair3map.getOrDefault(value,0)+1));
            return twosilverpair3map;
        }
     public Map<Integer,Integer> twosilverpair4mapfunction(){
            Arrays.stream(twosilverpair4).forEach(value ->twosilverpair4map.put(value,twosilverpair4map.getOrDefault(value,0)+1));
            return twosilverpair4map;
        }
        public Map<Integer,Integer> twosilverpair5mapfunction(){
            Arrays.stream(twosilverpair5).forEach(value ->twosilverpair5map.put(value,twosilverpair5map.getOrDefault(value,0)+1));
            return twosilverpair5map;
        }


        public Map<Integer,Integer> twobronzepair2bronzepair1mapfunction(){
            Arrays.stream(twobronzepair2bronzepair1).forEach(value ->twobronzepair2bronzepair1map.put(value,twobronzepair2bronzepair1map.getOrDefault(value,0)+1));
            return twobronzepair2bronzepair1map;
        }
        public Map<Integer,Integer> twobronzepair2bronzepair2mapfunction(){
            Arrays.stream(twobronzepair2bronzepair2).forEach(value ->twobronzepair2bronzepair2map.put(value,twobronzepair2bronzepair2map.getOrDefault(value,0)+1));
            return twobronzepair2bronzepair2map;
        }
        public Map<Integer,Integer> twobronzepair2bronzepair3mapfunction(){
            Arrays.stream(twobronzepair2bronzepair3).forEach(value ->twobronzepair2bronzepair3map.put(value,twobronzepair2bronzepair3map.getOrDefault(value,0)+1));
            return twobronzepair2bronzepair3map;
        }
        public Map<Integer,Integer> twobronzepair2bronzepair4mapfunction(){
            Arrays.stream(twobronzepair2bronzepair4).forEach(value ->twobronzepair2bronzepair4map.put(value,twobronzepair2bronzepair4map.getOrDefault(value,0)+1));
            return twobronzepair2bronzepair4map;
        }
        public Map<Integer,Integer> twobronzepair2bronzepair5mapfunction(){
            Arrays.stream(twobronzepair2bronzepair5).forEach(value ->twobronzepair2bronzepair5map.put(value,twobronzepair2bronzepair5map.getOrDefault(value,0)+1));
            return twobronzepair2bronzepair5map;
        }
        public Map<Integer,Integer> twobronzepair2bronzepair6mapfunction(){
            Arrays.stream(twobronzepair2bronzepair6).forEach(value ->twobronzepair2bronzepair6map.put(value,twobronzepair2bronzepair6map.getOrDefault(value,0)+1));
            return twobronzepair2bronzepair6map;
        }
        public Map<Integer,Integer> twobronzepair2bronzepair7mapfunction(){
            Arrays.stream(twobronzepair2bronzepair7).forEach(value ->twobronzepair2bronzepair7map.put(value,twobronzepair2bronzepair7map.getOrDefault(value,0)+1));
            return twobronzepair2bronzepair7map;
        }
        public Map<Integer,Integer> twobronzepair2bronzepair8mapfunction(){
            Arrays.stream(twobronzepair2bronzepair8).forEach(value ->twobronzepair2bronzepair8map.put(value,twobronzepair2bronzepair8map.getOrDefault(value,0)+1));
            return twobronzepair2bronzepair8map;
        }
        public Map<Integer,Integer> twobronzepair2bronzepair9mapfunction(){
            Arrays.stream(twobronzepair2bronzepair9).forEach(value ->twobronzepair2bronzepair9map.put(value,twobronzepair2bronzepair9map.getOrDefault(value,0)+1));
            return twobronzepair2bronzepair9map;
        }
        public Map<Integer,Integer> bronzethreeofakind1mapfunction(){
            Arrays.stream(bronzethreeofakind1).forEach(value ->bronzethreeofakind1map.put(value,bronzethreeofakind1map.getOrDefault(value,0)+1));
            return bronzethreeofakind1map;
        }
  public Map<Integer,Integer> bronzethreeofakind2mapfunction(){
            Arrays.stream(bronzethreeofakind2).forEach(value ->bronzethreeofakind2map.put(value,bronzethreeofakind2map.getOrDefault(value,0)+1));
            return bronzethreeofakind2map;
        }
  public Map<Integer,Integer> bronzethreeofakind3mapfunction(){
            Arrays.stream(bronzethreeofakind3).forEach(value ->bronzethreeofakind3map.put(value,bronzethreeofakind3map.getOrDefault(value,0)+1));
            return bronzethreeofakind3map;
        }
  public Map<Integer,Integer> bronzethreeofakind4mapfunction(){
            Arrays.stream(bronzethreeofakind4).forEach(value ->bronzethreeofakind4map.put(value,bronzethreeofakind4map.getOrDefault(value,0)+1));
            return bronzethreeofakind4map;
        }
 public Map<Integer,Integer> bronzepair1map1function(){
            Arrays.stream(bronzepair1).forEach(value ->bronzepair1map1.put(value,bronzepair1map1.getOrDefault(value,0)+1));
            return bronzepair1map1;
        }
public Map<Integer,Integer> bronzepair2map2function(){
            Arrays.stream(bronzepair2).forEach(value ->bronzepair2map2.put(value,bronzepair2map2.getOrDefault(value,0)+1));
            return bronzepair2map2;
        }
public Map<Integer,Integer> bronzepair3map3function(){
            Arrays.stream(bronzepair3).forEach(value ->bronzepair3map3.put(value,bronzepair3map3.getOrDefault(value,0)+1));
            return bronzepair3map3;
        }
public Map<Integer,Integer> bronzepair4map4function(){
            Arrays.stream(bronzepair4).forEach(value ->bronzepair4map4.put(value,bronzepair4map4.getOrDefault(value,0)+1));
            return bronzepair4map4;
        }




    }




    public void extradata(){
        /** if (playeronematchlist.equals(playertwomatchlist)){
         if (playeronematchlist.stream().allMatch(integer -> integer.equals(25)) && playertwomatchlist.stream().allMatch(integer -> integer.equals(25))){
         System.out.println("similar ");
         System.out.println(playeronematchlist);
         System.out.println(playertwomatchlist);
         int max = IntStream
         .concat(IntStream.of(frtsttheusercards), IntStream.of(sectheusercards))
         .max()
         .getAsInt();
         System.out.println(""+max);

         }

         }*/


        /** Map<Integer,Integer> firstplayerusercardslist=new HashMap<>();
         Map<Integer,Integer> secondplayerusercardslist=new HashMap<>();
         int[] frtsttheusercards={11,11,13,13,10};
         int[] sectheusercards={11,11,12,12,10};

         //        int[] frtsttheusercards={11,11,13,12,10};
         //        int[] sectheusercards={11,13,12,12,10};
         Arrays.stream(frtsttheusercards).forEach(value -> firstplayerusercardslist.put(value,firstplayerusercardslist.getOrDefault(value,0)+1));
         Arrays.stream(sectheusercards).forEach(value -> secondplayerusercardslist.put(value,secondplayerusercardslist.getOrDefault(value,0)+1));
         */
    }


}

