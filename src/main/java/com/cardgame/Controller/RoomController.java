package com.cardgame.Controller;

import com.cardgame.Dto.requests.*;

import com.cardgame.Dto.requests.gamelogic.*;

import com.cardgame.Dto.requests.gamelogic.gameactions.Gameactionrequest;
import com.cardgame.Dto.responses.Buyintableresponse;
import com.cardgame.Dto.responses.GameRoomResponse;
import com.cardgame.Dto.responses.Page.PagedResponse;
import com.cardgame.Dto.responses.RoomTableusersresponse;
import com.cardgame.Service.Gameactionsservice;
import com.cardgame.Service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static com.cardgame.config.ApiUtils.DEFAULT_PAGE_NUMBER;
import static com.cardgame.config.ApiUtils.DEFAULT_PAGE_SIZE;


@RestController
@RequestMapping("/api/game-room")
//@RequestMapping("/game")
public class RoomController {

  private final RoomService roomService;
  private final Gameactionsservice gameactionsservice;

    public RoomController(RoomService roomService, Gameactionsservice gameactionsservice) {
        this.roomService = roomService;
        this.gameactionsservice = gameactionsservice;
    }







    @PostMapping("/create")
    public ResponseEntity<?> creategameroom(@Valid @RequestBody Creategameroomrequest creategameroomrequest, BindingResult result){

            return new ResponseEntity<>(roomService.creategameroom(creategameroomrequest), HttpStatus.CREATED);

    }

    @PostMapping("/join")
    public ResponseEntity<?> jointhegameroom(@Valid @RequestBody Roomjoinrequest roomjoinrequest, BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(roomService.joinRoom(roomjoinrequest), HttpStatus.CREATED);
        }
    }
    @PostMapping("/leave-room")
    public ResponseEntity<?> leaveroom(@Valid @RequestBody LeaveRoomRequest leaveRoomRequest){
        return ResponseEntity.ok(roomService.leaveroom(leaveRoomRequest));
    }

    @PostMapping("/get/userbestcards")
//    public ResponseEntity<?> userbestthirtycards(){
    public Object userbestthirtycards(@Valid @RequestBody GetUserBest30cardsrequest getUserBest30cardsrequest){

//        return ResponseEntity.ok(roomService.userbestthirtycards());
        return roomService.userbestthirtycards(getUserBest30cardsrequest);
    }
    @PostMapping("/get/alluserbest30cards")
    public Object allusersbest30cards(){
        return roomService.allusersbestthirtycards();
    }
//    @PostMapping("/get/best30cardsforeveryuserinthegameroom")
//    public Object gameroomsersbest30cards(){
//        return roomService.gameroomsersbest30cards();
//    }

    @GetMapping("/gamerooms")
    public PagedResponse<GameRoomResponse> gamerooms(@RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                     @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size){

        return roomService.gamerooms(page, size);

    }
    @PostMapping("/getbyid")
    public Object getmasterroombyid(@Valid @RequestBody Gameroommasterrequest gameroommasterrequest){

        return roomService.getgameroombyid(gameroommasterrequest);
    }


    @GetMapping("/get-user-buy-in")
    public ResponseEntity<?> getuserbuyin(@Valid @RequestBody UserBuyInRequest userBuyInRequest){

        return ResponseEntity.ok(roomService.getuserbuyin(userBuyInRequest));
    }
    @PostMapping("/buy-in")
    public ResponseEntity<?> savebuyin(@Valid @RequestBody Buyinrequest buyinrequest){

        return new ResponseEntity<>(roomService.buyin(buyinrequest),HttpStatus.CREATED);

    }
//    @PostMapping("/getusersforagameroomtable")
//        public PagedResponse<RoomTableusersresponse> getusersforagameroomtable(@RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
//        @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size,@Valid @RequestBody Getusersforroomtablerequest getusersforroomtablerequest){
//        return  roomService.getusersforagameroomtable(getusersforroomtablerequest,page,size);
//    }
@PostMapping("/getusersforagameroomtable")
public List<RoomTableusersresponse> getusersforagameroomtable(@Valid @RequestBody Getusersforroomtablerequest getusersforroomtablerequest){

    return  roomService.getusersforagameroomtable(getusersforroomtablerequest);
}
    @PostMapping("/currentUserroom")
    public ResponseEntity<?> currentUserroomdetailsrespon(@Valid @RequestBody Currentuserroomrequest currentuserroomrequest ){
        return new ResponseEntity<>(roomService.currentUserroomdetailsresponse(currentuserroomrequest),HttpStatus.OK);
    }

    @PostMapping("/updateplayerbuyin")
    public ResponseEntity<?> updateplayeronebuyin(@Valid @RequestBody Updateplayerbuyinrequest updateplayerbuyinrequest){
        return new ResponseEntity<>(roomService.updateplayerbuyin(updateplayerbuyinrequest),HttpStatus.OK);
    }
    @PostMapping("/updateallplayerbuyin")
    public List<Buyintableresponse> updateallplayerbuyin(@Valid @RequestBody Updateallplayerbuyinsrequest updateallplayerbuyinsrequest){
        return roomService.updateallplayersbuyin(updateallplayerbuyinsrequest);
    }

    @PostMapping("/winninghand")
        public Object winninghand(@Valid @RequestBody ArrayList<ArrayList<Winninghandrequest>> winninghandrequests){
//        public Object winninghand(@Valid @RequestBody ArrayList<Winningdata> winninghandrequests){
//        System.out.println("The request body "+gamewinnerequest.getAmount());
        return roomService.winninghand(winninghandrequests);
    }


    @PostMapping("/testcardpairings")
    public Object pairingtest(@Valid @RequestBody CardPairingtest cardPairingtest){

        return roomService.cardpairingtest(cardPairingtest);
    }
    @PostMapping("/game_action")
    public Object game_action(@Valid @RequestBody Gameactionrequest gameactionrequest){

        return gameactionsservice.savegameaction(gameactionrequest);
    }








}
