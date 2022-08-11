package com.cardgame.Controller;

import com.cardgame.Dto.requests.JoinRoomRequest;
import com.cardgame.Dto.requests.Cardduplicaterequest;
import com.cardgame.Dto.requests.Roomjoinrequest;
import com.cardgame.Dto.responses.Userbestthirtycards;
import com.cardgame.Service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game-room")
public class RoomController {

  private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
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
            return new ResponseEntity<>(roomService.joinRoom1(roomjoinrequest), HttpStatus.CREATED);
        }
    }
    @PostMapping("/leave-room")
    public ResponseEntity<?> leaveroom(){
        return ResponseEntity.ok(roomService.leaveroom());
    }

    @GetMapping("/get/userbestcards")
    public ResponseEntity<?> userbestthirtycards(){

        return ResponseEntity.ok(roomService.userbestthirtycards());
    }

}
