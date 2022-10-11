//package com.cardgame.config.Auth.security;
//
//import com.cardgame.config.Auth.jwt.JWTConfigurer;
//import com.cardgame.config.Auth.jwt.TokenProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//  private final TokenProvider tokenProvider;
//
//  public SecurityConfig(TokenProvider tokenProvider) {
//    this.tokenProvider = tokenProvider;
//  }
//
//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }
//
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    // @formatter:off
//		http
//		  .csrf()
//		    .disable()
//		  .cors()
//		    .and()
//		  .sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//			.and()
//		//.httpBasic() // optional, if you want to access
//		//  .and()     // the services from a browser
//		  .authorizeRequests()
//		    .antMatchers("/api/auth/**").permitAll()
//		    .antMatchers("/api/cardgame/**").permitAll()
//				.antMatchers("/api/wallet/**").permitAll()
//				.antMatchers("/api/game-room/**").permitAll()
//				.antMatchers("/api/market-place/**").permitAll()
//		    .antMatchers("/auth/checkotp/{mobileno}/{otppass}").permitAll()
//		    .anyRequest().authenticated()
//		    .and()
//		  .apply(new JWTConfigurer(this.tokenProvider));
//		// @formatter:on
//
//  }
//	@Bean
//	public BCryptPasswordEncoder bCryptPasswordEncoder(){
//		return new BCryptPasswordEncoder();
//	}
//
//
//}