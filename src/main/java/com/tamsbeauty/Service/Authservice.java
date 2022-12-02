package com.tamsbeauty.Service;


import com.tamsbeauty.Dto.Request.Auth.Loginrequest;
import com.tamsbeauty.Dto.Request.Auth.RefreshTokenRequest;
import com.tamsbeauty.Dto.Request.Auth.Signuprequest;
import com.tamsbeauty.Dto.Response.ApiResponse;
import com.tamsbeauty.Dto.Response.Nailofweekresponse;
import com.tamsbeauty.Dto.Response.Auth.CurrentUserResponse;
import com.tamsbeauty.Dto.Response.Auth.JwtAuthenticationResponse;
import com.tamsbeauty.Dto.Response.Auth.Signupresponse;
import com.tamsbeauty.Entity.Role;
import com.tamsbeauty.Entity.RoleName;
import com.tamsbeauty.Entity.User;
import com.tamsbeauty.Exceptions.NotFoundException;
import com.tamsbeauty.Repo.Rolerepo;
import com.tamsbeauty.Repo.Userrepo;

import com.tamsbeauty.Security.JwtTokenProvider;
import com.tamsbeauty.Security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.tamsbeauty.config.ApiUtils.TOKEN_TYPE;


@Service
@Slf4j
public class Authservice {

    @Autowired
    private Userrepo userrepo;
    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;


    @Autowired
    private RefreshTokenService refreshTokenservice;
    @Autowired
    private Rolerepo rolerepo;


    public Object registeruser(Signuprequest signupRequest) {
        try {
            

     
        if (userrepo.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username " + signupRequest.getUsername() + " is already taken!", HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        }
        if (userrepo.existsByMobile(signupRequest.getMobile())) {
            return new ResponseEntity(new ApiResponse(false, "email " + signupRequest.getMobile() + " has already been taken",HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        //check if the role is set
        Role role = rolerepo.findByName(RoleName.ROLE_USER).orElseThrow(() -> new NotFoundException("user role not set !"));

        //try to register user
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setName(signupRequest.getName());
        user.setMobile(signupRequest.getMobile());
        user.setRoles(Collections.singleton(role));
        user.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
        log.info("logging the user object before saving {}",user);
        try {
            User saveduser = userrepo.save(user);

            return new ResponseEntity<>(new ApiResponse(true, "user with username " + saveduser.getUsername() + " has been registered",HttpStatus.CREATED), HttpStatus.CREATED);

        } catch (Exception exception) {
            log.error("an error has occurred while trying to save the user {}", exception.getStackTrace());
            return new ResponseEntity(new ApiResponse(false, "An exception has occurred while trying to register user " + exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    } catch (Exception exception) {
        return new ResponseEntity(new ApiResponse(false, "An exception has occurred when registering the new user " + exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }



    @Transactional
    public JwtAuthenticationResponse signin(Loginrequest loginRequest) {

        try {
            User user=userrepo.findByUsernameOrMobile(loginRequest.getMobile(),loginRequest.getMobile()).orElseThrow(() -> new UsernameNotFoundException("user with provided details not found"));

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getMobile(),
                loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

            return JwtAuthenticationResponse.builder()
                    .accessToken(jwt)
                    .refreshToken(refreshTokenservice.generateRefreshToken().getToken())
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



    public JwtAuthenticationResponse refreshToken(UserPrincipal currentUser, RefreshTokenRequest refreshTokenRequest) {
        if (currentUser==null){
            return  JwtAuthenticationResponse.builder().message("You are not authenticated").build();
        }
        refreshTokenservice.validateRefreshtoken(refreshTokenRequest.getRefreshToken());
        String token=tokenProvider.generatetokenwithusername(currentUser.getId());
        return JwtAuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .tokenType(TOKEN_TYPE)
                .expirationTime(Instant.now().plusMillis(tokenProvider.jwtExpirationInMs()))
                .username(currentUser.getUsername())
                .message("Log in successful!")
                .build();
    }
    public CurrentUserResponse getuserprofile(UserPrincipal currentUser) {
        System.out.println("currentUser "+currentUser);

        if (currentUser==null){
            return  CurrentUserResponse.builder().message("You are not authenticated").build();

        }
        try {
          User user=userrepo.findById(currentUser.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found "));
            return CurrentUserResponse.builder().id(currentUser.getId()).booking(user.getBookingList()).name(currentUser.getName()).username(currentUser.getUsername()).mobilenumber(currentUser.getMobile()).roles(user.getRoles()).build();
        }catch (Exception e){
            return  CurrentUserResponse.builder().message("An exception has occurred while fetching the user profile "+e.getMessage()).build();
        }
    }



    public Nailofweekresponse fetchallusersurl(UserPrincipal currentUser){
               if (currentUser==null){
                   return Nailofweekresponse.builder().message("You are not authenticated").status(false).build();
               }
                try {
                    List<User> users=userrepo.findAll();
                     return Nailofweekresponse.builder().status(true).weekNailList(users).build();
                }catch (Exception e){
                    return Nailofweekresponse.builder().status(false).description("An error").build();
        
                }
        
            }

}
