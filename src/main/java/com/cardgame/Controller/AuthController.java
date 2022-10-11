package com.cardgame.Controller;


import com.cardgame.Dto.requests.Auth.Loginrequest;
import com.cardgame.Dto.requests.Cardduplicaterequest;
import com.cardgame.Dto.responses.Auth.JwtAuthenticationResponse;
import com.cardgame.Dto.responses.Auth.Signuprequest;
import com.cardgame.Service.Authservice;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private  Authservice  authservice;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody Signuprequest signuprequest, BindingResult result){
        if (result.hasErrors()){
            Map<String,String> errorMap=new HashMap<>();
            for (FieldError err: result.getFieldErrors()){
                errorMap.put(err.getField(),err.getDefaultMessage());
            }
            return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(authservice.signup(signuprequest), HttpStatus.CREATED);
        }

    }
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateuser(@Valid @RequestBody Loginrequest loginRequest){
        return ResponseEntity.ok(authservice.signin(loginRequest));
    }

    }
