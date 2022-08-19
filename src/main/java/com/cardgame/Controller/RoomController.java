package com.cardgame.Controller;

import com.cardgame.Dto.requests.*;

import com.cardgame.Dto.responses.GameRoomResponse;
import com.cardgame.Dto.responses.Page.PagedResponse;
import com.cardgame.Service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.cardgame.config.ApiUtils.DEFAULT_PAGE_NUMBER;
import static com.cardgame.config.ApiUtils.DEFAULT_PAGE_SIZE;


@RestController
@RequestMapping("/api/game-room")
public class RoomController {

  private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
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

    @GetMapping("/get/userbestcards/{uid}")
//    public ResponseEntity<?> userbestthirtycards(){
    public Object userbestthirtycards(@PathVariable("uid") Long uid){

//        return ResponseEntity.ok(roomService.userbestthirtycards());
        return roomService.userbestthirtycards(uid);
    }

    @GetMapping("/gamerooms")
    public PagedResponse<GameRoomResponse> gamerooms(@RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                     @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size){

        return roomService.gamerooms(page, size);

    }
    @PostMapping("/buy-in")
    public ResponseEntity<?> buyin(@Valid @RequestBody Buyinrequest buyinrequest, BindingResult result){

        return new ResponseEntity<>(roomService.buyin(buyinrequest), HttpStatus.CREATED);
    }

    @GetMapping("/get-user-buy-in")
    public ResponseEntity<?> getuserbuyin(@Valid @RequestBody UserBuyInRequest userBuyInRequest){

        return ResponseEntity.ok(roomService.getuserbuyin(userBuyInRequest));
    }

}
