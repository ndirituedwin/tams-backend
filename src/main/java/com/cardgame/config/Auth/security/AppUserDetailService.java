//package com.cardgame.config.Auth.security;
//
//import com.cardgame.Entity.User;
//import com.cardgame.Repo.Userrepo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//import com.cardgame.Service.Authservice;
//import org.springframework.stereotype.Service;
//
//
//@Component
//@Slf4j
//public class AppUserDetailService implements UserDetailsService {
//
//  @Autowired
//  Authservice loginService1;
//
//  private final Userrepo userrepo;
//
//
//  private final Authservice loginService;
//
//
//  public AppUserDetailService(Userrepo userrepo, Authservice loginService) {
//	  this.userrepo = userrepo;
//	  this.loginService = loginService;
//  }
//
//
//	@Override
//	public final UserDetails loadUserByUsername(String usernameoremail)
//			throws UsernameNotFoundException {
//		User user=userrepo.findByUsernameOrMobilenumber(usernameoremail,usernameoremail)
//				.orElseThrow(() -> new UsernameNotFoundException("User with the given username or email could not be found "+usernameoremail));
//
//		log.info("logging the user credentials  {}",user.getUsername());
//		log.info("logging the user principal {}",UserPrincipal.class);
//		return UserPrincipal.create(user);
//	}
//
///**
//  @Override
//  public final UserDetails loadUserByUsername(String uid)
//      throws UsernameNotFoundException {
//	  System.out.println("Login Type"+this.loginService1.loginType);
//	  if(this.loginService1.loginType.equals("normal"))
//	  {
//		  final LoginModel loginModel = this.loginService.lookup(uid);
//		  if (loginModel == null) {
//		      throw new UsernameNotFoundException("User '" + uid + "' not found");
//		    }
//
//		    return org.springframework.security.core.userdetails.User.withUsername(uid)
//		        .password(loginModel.getPassword()).authorities(Collections.emptyList())
//		        .accountExpired(false).accountLocked(false).credentialsExpired(false)
//		        .disabled(false).build();
//	  }
//	  else if(this.loginService1.loginType.equals("circle"))
//	  {
//		  final CirclePatientLoginModel loginModel = this.loginService.getcircleUserDetail(uid);
//		  if (loginModel == null) {
//		      throw new UsernameNotFoundException("User '" + uid + "' not found");
//		    }
//
//		    return org.springframework.security.core.userdetails.User.withUsername(uid)
//		        .password(loginModel.getPassword()).authorities(Collections.emptyList())
//		        .accountExpired(false).accountLocked(false).credentialsExpired(false)
//		        .disabled(false).build();
//	  }
//	  else if(this.loginService1.loginType.equals("followapp"))
//	  {
//		  final LoginModel loginModel = this.loginService.getLoginUserDetailMobileno(uid);
//		  if (loginModel == null) {
//		      throw new UsernameNotFoundException("User '" + uid + "' not found");
//		    }
//
//		    return org.springframework.security.core.userdetails.User.withUsername(uid)
//		        .password(loginModel.getPassword()).authorities(Collections.emptyList())
//		        .accountExpired(false).accountLocked(false).credentialsExpired(false)
//		        .disabled(false).build();
//	  }
//	  else if(this.loginService1.loginType.equals("ptfollowapp"))
//	  {
//		  final CirclePatientLoginModel loginModel = this.loginService.getcircleUserDetail(uid);
//		  System.out.println("aptUser id===="+uid);
//		  if (loginModel == null) {
//		      throw new UsernameNotFoundException("User '" + uid + "' not found");
//		    }
//
//		    return org.springframework.security.core.userdetails.User.withUsername(uid)
//		        .password(loginModel.getPtOtpPass()).authorities(Collections.emptyList())
//		        .accountExpired(false).accountLocked(false).credentialsExpired(false)
//		        .disabled(false).build();
//	  }
//	  else
//	  {
//		  final CirclePatientLoginModel loginModel = this.loginService.getcircleCorporateUserDetail(uid);
//		  if (loginModel == null) {
//		      throw new UsernameNotFoundException("User '" + uid + "' not found");
//		    }
//
//		    return org.springframework.security.core.userdetails.User.withUsername(uid)
//		        .password(loginModel.getPassword()).authorities(Collections.emptyList())
//		        .accountExpired(false).accountLocked(false).credentialsExpired(false)
//		        .disabled(false).build();
//	  }
//
//
//  }*/
//
//
//}