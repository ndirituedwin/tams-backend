//package com.cardgame.config.Auth.jwt;
//
//import com.cardgame.Entity.User;
//import com.cardgame.config.Auth.AppConfig;
//import com.cardgame.config.Auth.security.UserPrincipal;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//
//@Component
//public class TokenProvider {
//
//  private final String secretKey;
//
//  private String userID;
//  private String mobileNumber;
//
//  private final long tokenValidityInMilliseconds;
//
//  private final UserDetailsService userService;
//
//  private  Map<String, Object> claims = new HashMap<String, Object>();
//
//  public TokenProvider(AppConfig config, UserDetailsService userService) {
//    this.secretKey = Base64.getEncoder().encodeToString(config.getSecret().getBytes());
//    this.tokenValidityInMilliseconds = 1000 * config.getTokenValidityInSeconds();
//    this.userService = userService;
//  }
//
//    public String createToken(Authentication authentication, User loginUser) {
//        UserPrincipal userPrincipal=(UserPrincipal) authentication.getPrincipal();
//        claims.put("id", userPrincipal.getId());
//       claims.put("UID", userPrincipal.getUid());
//	   claims.put("username", userPrincipal.getUsername());
//	   claims.put("mobile", userPrincipal.getMobilenumber());
//        return Jwts.builder()
//                .setId(UUID.randomUUID().toString())
//                .setSubject(Long.toString(userPrincipal.getId()))
//                .setClaims(claims)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime()+tokenValidityInMilliseconds))
//                .signWith(SignatureAlgorithm.HS512,secretKey)
//                .compact();
//  }
//
////  public String createToken(Authentication authentication, User loginUser) {
////    Date now = new Date();
////    Date validity = new Date(now.getTime() + this.tokenValidityInMilliseconds);
////      UserPrincipal userPrincipal=(UserPrincipal) authentication.getPrincipal();
////      this.userID=String.valueOf(loginUser.getUID());
////      claims.put("id", String.valueOf(loginUser.getId()));
////      claims.put("UID", String.valueOf(loginUser.getUID()));
////	claims.put("username", loginUser.getUsername());
////	claims.put("mobile", loginUser.getMobilenumber());
////
////	System.out.println("claims========="+claims);
////	System.out.println("claims========="+this.userID);
////
////    return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(userID)
////    	.setClaims(claims)
////        .setIssuedAt(now).signWith(SignatureAlgorithm.HS512, this.secretKey)
////        .setExpiration(validity).compact();
////
////     /** log.info("logging authentication before generating token {}",authentication);
////      UserPrincipal userPrincipal=(UserPrincipal) authentication.getPrincipal();
////      return Jwts.builder()
////              .setSubject(Long.toString(userPrincipal.getId()))
////              .setIssuedAt(new Date())
////              .setExpiration(new Date(new Date().getTime()+jwtExpirationInMs))
////              .signWith(SignatureAlgorithm.HS256,jwtSecret)
////              .compact();*/
////  }
//
// public String createCircleToken(User circleLoginUser) {
//	    Date now = new Date();
//	    Date validity = new Date(now.getTime() + this.tokenValidityInMilliseconds);
//
//	    this.mobileNumber=circleLoginUser.getMobilenumber();
//	    claims.put("id", circleLoginUser.getId());
//	    claims.put("UID", circleLoginUser.getUID());
//		claims.put("username", circleLoginUser.getUsername());
//		claims.put("mobile", circleLoginUser.getMobilenumber());
//
//		System.out.println("claims========="+claims);
//		System.out.println("claims========="+this.userID);
//
//	    return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(userID)
//	    	.setClaims(claims)
//	        .setIssuedAt(now).signWith(SignatureAlgorithm.HS512, this.secretKey)
//	        .setExpiration(validity).compact();
//  }
//    public Long jwtExpirationInMs(){
//        return tokenValidityInMilliseconds;
//    }
//  public String createFollowAppToken(User loginModel) {
//    Date now = new Date();
//    Date validity = new Date(now.getTime() + this.tokenValidityInMilliseconds);
//
//    this.mobileNumber=loginModel.getMobilenumber();
//    claims.put("id", loginModel.getId());
//    claims.put("UID", loginModel.getUID());
//	claims.put("username", loginModel.getUsername());
//	claims.put("mobile", loginModel.getMobilenumber());
//
//	System.out.println("claims========="+claims);
//	System.out.println("claims========="+this.mobileNumber);
//
//    return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(mobileNumber)
//    	.setClaims(claims)
//        .setIssuedAt(now).signWith(SignatureAlgorithm.HS512, this.secretKey)
//        .setExpiration(validity).compact();
//  }
//
//  public Authentication getAuthentication(String token) {
//    String uid = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token)
//        .getBody().getSubject();
//    UserDetails userDetails = this.userService.loadUserByUsername(uid);
//
//    return new UsernamePasswordAuthenticationToken(userDetails, "",
//        userDetails.getAuthorities());
//  }
//
//}
