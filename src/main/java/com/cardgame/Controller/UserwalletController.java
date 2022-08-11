package com.cardgame.Controller;


import com.cardgame.Dto.requests.Addmoneytowalletrequest;
import com.cardgame.Dto.requests.Withdrawmoneyrequest;
import com.cardgame.Service.UserwalletService;
import org.hibernate.boot.jaxb.spi.Binding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class UserwalletController {

private final UserwalletService userwalletService;

    public UserwalletController(UserwalletService userwalletService) {
        this.userwalletService = userwalletService;
    }

    @PostMapping("/add-money-touser-wallet")
    public ResponseEntity<?> addmoneytouserwallet(@Valid  @RequestBody Addmoneytowalletrequest addmoneytowallet, BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return ResponseEntity.ok(userwalletService.addmoneytowallet(addmoneytowallet));
        }
    }

    @GetMapping("/get-user-total-wallet-balance")
    public ResponseEntity<?> getuserwalletbalance(){
        return ResponseEntity.ok(userwalletService.getwalletservice());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawmoney(@Valid @RequestBody Withdrawmoneyrequest withdrawmoneyrequest,BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return ResponseEntity.ok(userwalletService.withdraw(withdrawmoneyrequest));
        }

    }

}
