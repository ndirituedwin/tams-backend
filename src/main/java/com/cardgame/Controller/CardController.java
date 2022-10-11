package com.cardgame.Controller;

import com.cardgame.Dto.requests.*;
import com.cardgame.Dto.responses.*;
import com.cardgame.Dto.responses.Page.PagedResponse;
import com.cardgame.Service.CardService;
import com.cardgame.config.Auth.security.Securitytwo.Security.CurrentUser;
import com.cardgame.config.Auth.security.Securitytwo.Security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api/cardgame")
public class CardController {

private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    @PostMapping("/usertest/{uid}")
    public Object testcontroller(@PathVariable("uid") Long uid){
        return cardService.testcontroller(uid);
    }
    @PostMapping("/card_duplicate")
    public ResponseEntity<?> duplicatecard(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Cardduplicaterequest cardduplicaterequest, BindingResult result){
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
    public ResponseEntity<?> duplicatecardbyuserratio(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Cardbyratioduplicaterequest cardbyratioduplicaterequest, BindingResult result){
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
    public ResponseEntity<?> cardpack(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Selectpackrequest selectpackrequest, BindingResult result){
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
    @PostMapping("/checkpackstatus")
    public ResponseEntity<?> checkpackstatus(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Selectpackrequest selectpackrequest, BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(cardService.checkpackstatus(selectpackrequest), HttpStatus.CREATED);
        }
    }


    @PostMapping("/postbest30cards_forauser")
    public ResponseEntity<?> postbest30cards_forauser(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Userbestcardrequest userbestcardrequest, BindingResult result ){

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

    @PostMapping("/getuserbest30cards")
    public PagedResponse<GetUserBest30cardsresponse> getallcardsforauser(@Valid  @RequestBody Getuserbestcardrequest getuserbestcardrequest,BindingResult result,
            @RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size){

            return cardService.getUserBest30cardsresponse(page,size,getuserbestcardrequest);

    }
    @PostMapping("resetdeck")
    public UserbestCardResponse resetdeck(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Removebestcardrequest removebestcardrequest){
        return cardService.removeallbestcardsforaplayer(removebestcardrequest);
    }
    @PostMapping("/removecard")
    public ResponseEntity<?> removecard(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Removebestcardrequest removebestcardrequest, BindingResult result ) {
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(cardService.removecard(removebestcardrequest),HttpStatus.OK);
        }
    }


        @PostMapping("/unopened_packs")
    public ResponseEntity<?> unopened_packs(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Unopenedpackrequest unopenedpackrequest, BindingResult result ){

        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(cardService.unopenedpacks(unopenedpackrequest),HttpStatus.OK);
        }
    }

    @PostMapping("/buy-card")
    public ResponseEntity<?> buycard(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BuyCardRequest buyCardRequest, BindingResult result){

        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(cardService.buycardone(buyCardRequest),HttpStatus.OK);
        }
    }
    @GetMapping("/getallusercards")
    public PagedResponse<UserCardResponse> getallcardsforauser(
            @RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size,@CurrentUser UserPrincipal currentUser, @Valid @RequestBody FindAllCardsByUserrequest findAllCardsByUserrequest
    ){
//        return ResponseEntity.ok(cardService.findallcardsbyuser());
        return cardService.findallcardsbyuser(page, size,findAllCardsByUserrequest);
    }
    @PostMapping("/setfeeforusercard")
    public ResponseEntity<?> setcardfeeforauser(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody CardfeeRequest cardfeeRequest){

        return ResponseEntity.ok(cardService.setcardfee(cardfeeRequest));
    }

    @PostMapping("/setfeeforpack")
    public ResponseEntity<?> setpackfeeforauser(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody PackfeeRequest packfeeRequest){

        return ResponseEntity.ok(cardService.setpackfee(packfeeRequest));
    }

    @PostMapping("/updatecardstatus")
    public ResponseEntity<?> changecardstatus(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BoughtpackStatusRequest boughtpackStatusRequest){

        return ResponseEntity.ok(cardService.updateboughtpackstatus(boughtpackStatusRequest));
    }

    @PostMapping("/buy-pack")
    public ResponseEntity<?> buypack(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody BuyPackRequest buyPackRequest){

        return ResponseEntity.ok(cardService.buypack(buyPackRequest));

    }

    @PostMapping("/update-card-fee-status")
    public ResponseEntity<?> updatecardfeestatus(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody CardStatusupdate cardStatusupdate){

        return ResponseEntity.ok(cardService.updatecardfee(cardStatusupdate));

    }
    @PostMapping("/market-place-cards")
    public PagedResponse<CardFeeResponse> cardfeesall(  @RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size){

        return cardService.cardfees(page, size);

    }
    @PostMapping("/update-pack-fee-status")
    public ResponseEntity<?> updatepackfeestatus(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody Packstatusupdate cardStatusupdate){

        return ResponseEntity.ok(cardService.updatepackfee(cardStatusupdate));

    }
    @PostMapping("/market-place-packs")
    public PagedResponse<PackFeeResponse> packfeesall(@RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size){

        return cardService.packfees(page, size);

    }

    @PostMapping("/game-room-users")
    public PagedResponse<GameRoomUsersResponse> gameroomusers(@RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                              @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size,@RequestBody GameRoomUsersRequest gameRoomUsersRequest){
        return cardService.gameroomusers(page, size,gameRoomUsersRequest);
    }

    @PostMapping("/all-game-room-users")
    public PagedResponse<GameRoomUsersResponse> allgameroomusers(@RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                              @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size){
        return cardService.allgameroomusers(page, size);
    }

    @PostMapping("/user-and-user-cards")
    public PagedResponse<Cardsownedbycurrentplayerresponse> Cardsownedbycurrentplayerresponse(@Valid  @RequestBody Userprofilerequest userprofilerequest,
                                                                                          @RequestParam(value = "page",defaultValue =DEFAULT_PAGE_NUMBER) int page,
                                                                                          @RequestParam(value = "size",defaultValue = DEFAULT_PAGE_SIZE)  int size) {
        return cardService.kardsownedbycurrentplayerresponse(userprofilerequest, page, size);

    }

    @GetMapping("/getstarterpack")
    public Object getstarterpack(){
        return cardService.getstarterpack();
    }

}
