package com.tamsbeauty.Controller;


import com.tamsbeauty.Dto.Request.Auth.Loginrequest;
import com.tamsbeauty.Dto.Request.Auth.RefreshTokenRequest;
import com.tamsbeauty.Dto.Request.Auth.Signuprequest;
import com.tamsbeauty.Dto.Response.Auth.JwtAuthenticationResponse;
import com.tamsbeauty.Dto.Response.Auth.LogoutResponse;
import com.tamsbeauty.Security.CurrentUser;
import com.tamsbeauty.Security.UserPrincipal;
import com.tamsbeauty.Service.Authservice;
import com.tamsbeauty.Service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private Authservice authservice;
    @Autowired
    private RefreshTokenService refreshToken;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody Signuprequest signuprequest, BindingResult result){
            return new ResponseEntity<>(authservice.registeruser(signuprequest), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateuser(@Valid @RequestBody Loginrequest loginRequest){
        return ResponseEntity.ok(authservice.signin(loginRequest));
    }



    @PostMapping("/refreshToken")
    public JwtAuthenticationResponse authenticationResponse(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authservice.refreshToken(currentUser,refreshTokenRequest);
    }
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logoutanddeleterefreshtoken(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody RefreshTokenRequest refreshTokenRequest){

        return  ResponseEntity.ok(refreshToken.deleteToken(currentUser,refreshTokenRequest));
    }
    @PostMapping("/get-current-user")
    public ResponseEntity<?> getcurrentuser(@CurrentUser  UserPrincipal currentUser){

        return ResponseEntity.ok(authservice.getuserprofile(currentUser));

    }

    @GetMapping("/fetchallusersurl")
    @PreAuthorize("hasRole('ROLE_ADMIN')or hasRole('ROLE_SUPER_ADMIN')")
    public  Object fetchallusersurl(@CurrentUser UserPrincipal currentUser){
            return authservice.fetchallusersurl(currentUser);
        }

    }
