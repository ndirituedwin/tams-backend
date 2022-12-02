package com.tamsbeauty.Service;



import com.tamsbeauty.Dto.Request.Auth.RefreshTokenRequest;
import com.tamsbeauty.Dto.Response.Auth.LogoutResponse;
import com.tamsbeauty.Entity.RefreshToken;
import com.tamsbeauty.Exceptions.NotFoundException;
import com.tamsbeauty.Repo.RefreshTokenRepo;
import com.tamsbeauty.Security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepo refreshTokenRepository;



    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken =new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshtoken(String token){
        refreshTokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("token not found"));
    }
    public LogoutResponse deleteToken(UserPrincipal currentUser, RefreshTokenRequest token){
         System.out.println("THE DATA for current player "+currentUser.getMobile());
        if (currentUser==null){
            return new LogoutResponse("You are not authenticated");
        }

        try{
            refreshTokenRepository.deleteByToken(token.getRefreshToken());

            return new LogoutResponse("Logged out successfully");

        }catch(Exception e)
        {
            throw new RuntimeException("an exception has occurred while logiig out the user "+e.getMessage());
        }
    }
}
