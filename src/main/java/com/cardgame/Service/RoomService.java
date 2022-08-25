package com.cardgame.Service;

import com.cardgame.Dto.requests.*;
import com.cardgame.Dto.responses.*;
import com.cardgame.Dto.responses.Page.PagedResponse;
import com.cardgame.Entity.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static com.cardgame.config.ApiUtils.JOINED_GAME_ROOM;
import static com.cardgame.config.ApiUtils.LEFT_GAME_ROOM;

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

    public RoomService(Userrepo userrepo, Userbestcardrepo userbestcardrepo, CardService cardService, Buyinrepo buyinrepo, Userwalletrepo userwalletrepo, Gameroommasterrepo gameroommasterrepo, Gameroomtablerepo gameroomtablerepo, Gameroomlogrepo gameroomlogrepo) {
        this.userrepo = userrepo;
        this.userbestcardrepo = userbestcardrepo;
        this.cardService = cardService;
        this.buyinrepo = buyinrepo;
        this.userwalletrepo = userwalletrepo;
        this.gameroommasterrepo = gameroommasterrepo;
        this.gameroomtablerepo = gameroomtablerepo;
        this.gameroomlogrepo = gameroomlogrepo;
    }



    @Transactional
    public ResponseEntity<?> joinRoom(Roomjoinrequest roomjoinrequest) {
        if (roomjoinrequest.getMastertableid() == null || roomjoinrequest.getUserid() == null) {
            return ResponseEntity.ok("one of the values of the request body is null");
        }

        try {
            User user = userrepo.findById(roomjoinrequest.getUserid()).orElseThrow(() -> new UserNotFoundException("user with the provided uid not found"));
            Userwallet userwallet = userwalletrepo.findByUserid(user.getUID()).orElseThrow(() -> new UserWalletNotFoundException("user wallet nt found"));
            Gameroommaster gameroommaster=gameroommasterrepo.findById(roomjoinrequest.getMastertableid()).orElseThrow(() -> new GameNotFoundException("Game lobby with the given id could not be found"));

            boolean check=gameroomtablerepo.existsByUidoneOrUidtwoOrUidthreeOrUidfourOrUidfive(roomjoinrequest.getUserid(),roomjoinrequest.getUserid(),roomjoinrequest.getUserid(),roomjoinrequest.getUserid(),roomjoinrequest.getUserid());
           if (userwallet.getTotalwalletbalance().compareTo(gameroommaster.getAmount()) >0){
              if (check){
                   return new ResponseEntity<>("You can not join the room twice",HttpStatus.BAD_REQUEST);
               }else{
                   List<GameRoomTable> gameRoomTables=gameroomtablerepo.findAll();
                   if (gameRoomTables.isEmpty()){
                       GameRoomTable gameRoomTable1=new GameRoomTable();
                       gameRoomTable1.setUidone(user.getUID());
                       gameRoomTable1.setGameroommaster(gameroommaster);
                       gameRoomTable1.setNumberofusers(1);
                       GameRoomTable savedgameRoomTable=gameroomtablerepo.save(gameRoomTable1);

                       Gameroomlog gameroomlog=new Gameroomlog();
                       gameroomlog.setCreateddate(Instant.now());
                       gameroomlog.setUser(user);
                       gameroomlog.setGameroommaster(gameroommaster);
                       gameroomlog.setGameRoomTable(savedgameRoomTable);
                       gameroomlog.setNumberofparticipants(savedgameRoomTable.getNumberofusers());
                       gameroomlog.setAction(JOINED_GAME_ROOM);
                       Gameroomlog savedgameroomlog=gameroomlogrepo.save(gameroomlog);
                       return new ResponseEntity<>("you have joined the room "+savedgameRoomTable.getId(),HttpStatus.OK);

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
                           return new ResponseEntity<>("you have joined the room "+savedgameRoomTable.getId(),HttpStatus.OK);
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


                           Gameroomlog gameroomlog22=new Gameroomlog();
                           gameroomlog22.setCreateddate(Instant.now());
                           gameroomlog22.setUser(user);
                           gameroomlog22.setGameroommaster(gameroommaster);
                           gameroomlog22.setGameRoomTable(gameRoomTable);
                           gameroomlog22.setNumberofparticipants(gameRoomTable.getNumberofusers());
                           gameroomlog22.setAction(JOINED_GAME_ROOM);
                           Gameroomlog savedgameroomlog=gameroomlogrepo.save(gameroomlog22);

                       }


                   }

               }
           }else{
               return new ResponseEntity<>("You do not have eough amount to enter into the game room your wallet balance is "+userwallet.getTotalwalletbalance()+".The minimum amount required to join this room is "+gameroommaster.getAmount(),HttpStatus.BAD_REQUEST);
           }
            return ResponseEntity.ok("game room saved");

        } catch (Exception e) {
            return new ResponseEntity<>("An exception has occurred trying to save game room " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
}

