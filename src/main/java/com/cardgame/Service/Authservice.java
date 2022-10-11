package com.cardgame.Service;

import com.cardgame.Dto.requests.Auth.Loginrequest;
import com.cardgame.Dto.responses.Auth.JwtAuthenticationResponse;
import com.cardgame.Dto.responses.Auth.Signuprequest;
import com.cardgame.Dto.responses.Auth.Signupresponse;
import com.cardgame.Entity.Boosterpackstatus;
import com.cardgame.Entity.User;
import com.cardgame.Repo.Boosterpackstatusrepo;
import com.cardgame.Repo.Userrepo;
import com.cardgame.config.Auth.security.Securitytwo.Security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Random;

import static com.cardgame.config.ApiUtils.TOKEN_TYPE;

@Service
@Slf4j
public class Authservice {

    @Autowired
    private  Userrepo userrepo;
    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private Boosterpackstatusrepo boosterpackstatusrepo;


    public Signupresponse signup(Signuprequest signuprequest) {
        System.out.println("signuprequest "+signuprequest.getUsername()+"sig"+signuprequest.getPassword()+"password"+signuprequest.getMobile());

        try {

            boolean existsbyusername= userrepo.existsByUsername(signuprequest.getUsername());
            if (existsbyusername){
                return new Signupresponse("THe username "+signuprequest.getUsername()+" has already been taken "+"choose another name", signuprequest.getUsername(), signuprequest.getMobile(),false);
            }
            boolean existsbymobile= userrepo.existsByMobilenumber(signuprequest.getMobile());
            if (existsbymobile){
                return new Signupresponse("The mobile "+signuprequest.getMobile()+" has already been taken "+"choose another mobile number", signuprequest.getUsername(), signuprequest.getMobile(),false);
            }
            Long  uid= this.generateuid(signuprequest.getUsername(),signuprequest.getMobile());
            boolean existsbyuid=userrepo.existsByUID(uid);
            if (existsbyuid){
//               uid=this.generateuid(signuprequest.getUsername(),signuprequest.getMobile());
                return new Signupresponse("The UID "+uid+" has already been taken "+"choose another UID", signuprequest.getUsername(), signuprequest.getMobile(),false);

            }
            User user=new User();
            user.setUID(uid);
            user.setUsername(signuprequest.getUsername());
            user.setMobilenumber(signuprequest.getMobile());
            user.setPassword(bCryptPasswordEncoder.encode(signuprequest.getPassword()));
            User savedplayer=userrepo.save(user);
            Boosterpackstatus boosterpackstatus=new Boosterpackstatus();
            boosterpackstatus.setUser(savedplayer);
            boosterpackstatus.setCreatedAt(Instant.now());
            boosterpackstatus.setPack1(false);
            boosterpackstatus.setPack2(false);
            boosterpackstatus.setPack3(false);
            boosterpackstatusrepo.save(boosterpackstatus);
            return new Signupresponse("User saved! ", savedplayer.getUsername(), savedplayer.getMobilenumber(),true);
        }catch (Exception e){
            System.out.println();
            return new Signupresponse("An exception has occurred while signing in "+e.getMessage(),false);
        }
    }

    private Long generateuid(String username, String mobile) {
        Random random=new Random();
        StringBuilder randomnu=new StringBuilder();
        for (int i=0;i<8; i++){
            randomnu.append(random.nextLong(8));
        }
        System.out.println("phone "+randomnu);
        System.out.println("phone+mobile "+randomnu);

        return Long.valueOf(String.valueOf(randomnu));
    }

    @Transactional
    public JwtAuthenticationResponse signin(Loginrequest loginRequest) {

        try {
        User user=userrepo.findByUsernameOrMobilenumber(loginRequest.getMobilenumber(),loginRequest.getMobilenumber()).orElseThrow(() -> new UsernameNotFoundException("user with provided deails not found"));
        log.info("loginrequest {}",loginRequest);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getMobilenumber(),
                loginRequest.getPassword()
        ));
        log.info("logging authentication {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
//        String jwt = tokenProvider.generateToken(authentication,user);
        log.info("logging authentiction after it is set {}", SecurityContextHolder.getContext().getAuthentication());
        return JwtAuthenticationResponse.builder()
                .accessToken(jwt)
//                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .tokenType(TOKEN_TYPE)
                .username(user.getUsername())
//                .expirationTime(Instant.now().plusMillis(tokenProvider.jwtExpirationInMs()))
                .expirationTime(Instant.now().plusMillis(tokenProvider.jwtExpirationInMs()))
                .message("login successfull")
                .status(true)
                .build();
        }catch (Exception e){
            return JwtAuthenticationResponse.builder().status(false).message("An exception has occurred while sign in "+e.getMessage()).build();
        }
    }

}
