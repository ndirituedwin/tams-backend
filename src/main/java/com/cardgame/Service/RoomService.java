package com.cardgame.Service;

import com.cardgame.Dto.requests.Roomjoinrequest;
import com.cardgame.Dto.responses.JoinRoomResponse;
import com.cardgame.Dto.responses.Userbestthirtycards;
import com.cardgame.Entity.GameRoom;
import com.cardgame.Entity.Gameroomusers;
import com.cardgame.Entity.User;
import com.cardgame.Entity.Userbestcard;
import com.cardgame.Exceptions.CardNotFoundException;
import com.cardgame.Exceptions.UserNotFoundException;
import com.cardgame.Repo.*;
import lombok.extern.slf4j.Slf4j;
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

    public RoomService( Loginrepo loginrepo, GameRoomrepo gameRoomrepo, Gameroomusersrepo gameroomusersrepo, Userbestcardrepo userbestcardrepo) {
        this.loginrepo = loginrepo;

        this.gameRoomrepo = gameRoomrepo;
        this.gameroomusersrepo = gameroomusersrepo;
        this.userbestcardrepo = userbestcardrepo;
    }


    public JoinRoomResponse joinRoom1(Roomjoinrequest roomjoinrequest){


        BigDecimal bigDecimal=new BigDecimal(roomjoinrequest.getMinimumamount());
        log.info("bigdecima {}",bigDecimal);
        User user=loginrepo.findByUID(1L).orElseThrow(() -> new UserNotFoundException("user with the provided uid not found"));
//      Long userin=new Long(1);
        List<GameRoom> gameroomList=gameRoomrepo.findAll();
        if (gameroomList.isEmpty()){
            GameRoom gameRoom=new GameRoom();
            gameRoom.setStatus(true);
            gameRoom.setMinimumamount(new BigDecimal(roomjoinrequest.getMinimumamount()));
            gameRoom.setCreateddate(Instant.now());

            gameRoom.setNumberofusers(gameRoom.getNumberofusers()+1);
            GameRoom savedgameroom=gameRoomrepo.save(gameRoom);
            Gameroomusers gameroomusers=new Gameroomusers();
            gameroomusers.setGameRoom(savedgameroom);
            gameroomusers.setUserid(user.getUID()+ 1L);
            gameroomusersrepo.save(gameroomusers);
        }else{
            List<GameRoom> gameroomList1=gameRoomrepo.findAll();
            GameRoom agam=gameroomList1.stream()
                    .peek(gameRoom -> log.info("gameroom{} {} {} {}",gameRoom.getCreateddate(),gameRoom.getId(),gameRoom.getNumberofusers(),gameRoom.getMinimumamount()))
                    .filter(gameRoom -> gameRoom.getNumberofusers()<5 && gameRoom.getMinimumamount().toString().split("[.]")[0].equals(roomjoinrequest.getMinimumamount()))
                    .findFirst()
                    .orElse(null);
            if (agam==null){
                GameRoom gameRoom1=new GameRoom();
                gameRoom1.setStatus(true);
                gameRoom1.setMinimumamount(new BigDecimal(roomjoinrequest.getMinimumamount()));
                gameRoom1.setCreateddate(Instant.now());

                gameRoom1.setNumberofusers(gameRoom1.getNumberofusers()+1);
                GameRoom savedgameroom1=gameRoomrepo.save(gameRoom1);
                Gameroomusers gameroomusers1=new Gameroomusers();
                gameroomusers1.setGameRoom(savedgameroom1);
                gameroomusers1.setUserid(user.getUID()+ 1L);
                gameroomusersrepo.save(gameroomusers1);
                log.info("here instead");
            }else {
                Gameroomusers gameroomusers=new Gameroomusers();
                gameroomusers.setUserid(user.getUID()+1L);
                gameroomusers.setGameRoom(agam);
                gameroomusersrepo.save(gameroomusers);
                agam.setNumberofusers(agam.getNumberofusers()+1);
                Integer nou=gameRoomrepo.save(agam).getNumberofusers();
                log.info("agam {} {} {}",agam.getId(),agam.getCreateddate(),agam.getNumberofusers());
            }

        }
        return new JoinRoomResponse("game room saven");
    }



    public JoinRoomResponse leaveroom() {

        long userid=1;
        User user=loginrepo.findByUID(2L).orElseThrow(() -> new UserNotFoundException("user not found"));

        Gameroomusers gameroomusers=gameroomusersrepo.findByUserid(user.getUID()).orElseThrow(() -> new CardNotFoundException("game room not fund"));
        log.info("game room users {}",gameroomusers);
        try {
            GameRoom gameRoom=gameroomusers.getGameRoom();
            log.info("game room {}",gameRoom);
            gameroomusersrepo.deleteByUseridAndGameRoom(user.getUID(),gameRoom);
            gameRoom.setNumberofusers(gameRoom.getNumberofusers()-1);
            gameRoomrepo.save(gameRoom);
            return new JoinRoomResponse("you have left the room");
        }catch (Exception e){

            return new JoinRoomResponse("An error has occurred while leaving the room "+e.getMessage());
        }
    }


    public ResponseEntity<?> userbestthirtycards() {

        User user=loginrepo.findByUID(2L).orElseThrow(() -> new UserNotFoundException("user not found"));
        try {
            List<Userbestcard> userbestthirtycardsList=userbestcardrepo.findAllByUser(user);
            if (userbestthirtycardsList.isEmpty()){
                return ResponseEntity.ok(new Userbestthirtycards("you have no best cards"));
            }else{
               return ResponseEntity.ok(userbestthirtycardsList);
            }

        }catch (Exception e){
            return ResponseEntity.ok( new Userbestthirtycards("an error has occurred while getting user bes cards "+e.getMessage()));
        }

    }
}

