package com.cardgame.Service;

import com.cardgame.Dto.requests.gamelogic.gameactions.Gameactionrequest;
import com.cardgame.Dto.requests.gamelogic.gameactions.Gameactionresponse;
import com.cardgame.Entity.Gameplaylog;
import com.cardgame.Repo.Gameplaylogrepo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
@Transactional
public class Gameactionsservice {

    private final Gameplaylogrepo gameplaylogrepo;

    public Gameactionsservice(Gameplaylogrepo gameplaylogrepo) {
        this.gameplaylogrepo = gameplaylogrepo;
    }

    public Object savegameaction(Gameactionrequest gameactionrequest) {
         System.out.println(gameactionrequest.getAction());
        Gameplaylog gameplaylog=new Gameplaylog();
        gameplaylog.setAction(gameactionrequest.getAction());
        gameplaylog.setCreateddate(Instant.now());
        gameplaylog.setAmount(gameactionrequest.getAmount());
        gameplaylog.setSecond(gameactionrequest.getSecond());
        gameplaylog.setGameRoomTableuid(gameactionrequest.getGameRoomTableuid());
        gameplaylog.setPlayer_uid(gameactionrequest.getPlayer_uid());
        Gameplaylog gameplaylog1 =gameplaylogrepo.save(gameplaylog);

         return new Gameactionresponse("action "+gameplaylog1.getAction()+" saved! by the player with uid "+gameplaylog1.getPlayer_uid());

    }
}
