package com.cardgame.Controller;

import com.cardgame.Dto.requests.Cardbyratioduplicaterequest;
import com.cardgame.Dto.requests.Cardduplicaterequest;
import com.cardgame.Dto.requests.Selectpackrequest;
import com.cardgame.Dto.requests.Userbestcardrequest;
import com.cardgame.Service.CardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/cardgame")
@AllArgsConstructor
public class CardController {

private final CardService cardService;

    @PostMapping("/card_duplicate")
    public ResponseEntity<?> duplicatecard(@Valid @RequestBody Cardduplicaterequest cardduplicaterequest, BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(cardService.duplicatecard(cardduplicaterequest), HttpStatus.CREATED);
        }
    }
    @PostMapping("/card_duplicate_byuser_ratio")
    public ResponseEntity<?> duplicatecardbyuserratio(@Valid @RequestBody Cardbyratioduplicaterequest cardbyratioduplicaterequest, BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(cardService.duplicatecardbyratio(cardbyratioduplicaterequest), HttpStatus.CREATED);
        }
    }

    @PostMapping("/card_select_pack")
    public ResponseEntity<?> cardpack(@Valid @RequestBody Selectpackrequest selectpackrequest, BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(cardService.usercards(selectpackrequest), HttpStatus.CREATED);
        }
    }


    @PostMapping("/postbest30cards_forauser")
    public ResponseEntity<?> postbest30cards_forauser(@Valid @RequestBody Userbestcardrequest userbestcardrequest, BindingResult result ){

        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(cardService.userbest30cards(userbestcardrequest),HttpStatus.OK);
        }
    }
}
