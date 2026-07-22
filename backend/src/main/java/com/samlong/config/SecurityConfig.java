package com.samlong.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samlong.repository.AdminUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final AdminUserRepository users;
  public SecurityConfig(AdminUserRepository users) { this.users = users; }
  @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(12); }
  @Bean public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5173", "http://localhost:5173", "http://127.0.0.1:3000", "http://localhost:3000"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(Collections.singletonList("*")); config.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**", config); return source;
  }
  @Override protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(username -> users.findByUsername(username).map(user -> User.withUsername(user.getUsername()).password(user.getPassword()).roles(user.getRole()).disabled(!user.isEnabled()).build()).orElseThrow(() -> new UsernameNotFoundException(username))).passwordEncoder(passwordEncoder());
  }
  @Override protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().authorizeRequests()
      .antMatchers("/actuator/health", "/api/auth/csrf", "/uploads/**").permitAll()
      .antMatchers(HttpMethod.POST, "/api/bookings").permitAll()
      .antMatchers(HttpMethod.GET, "/api/products/**", "/api/news/**", "/api/slides/**").permitAll()
      .antMatchers("/api/**").authenticated().anyRequest().permitAll().and()
      .exceptionHandling().authenticationEntryPoint((request,response,exception)->{response.setStatus(401);response.setContentType(MediaType.APPLICATION_JSON_VALUE);response.getWriter().write("{\"error\":\"Unauthorized\"}");}).and()
      .formLogin().loginProcessingUrl("/api/auth/login")
      .successHandler((request,response,authentication)->{response.setContentType(MediaType.APPLICATION_JSON_VALUE);new ObjectMapper().writeValue(response.getWriter(),Collections.singletonMap("username",authentication.getName()));})
      .failureHandler((request,response,exception)->response.sendError(401,"Invalid username or password"))
      .and().logout().logoutUrl("/api/auth/logout").logoutSuccessHandler((request,response,authentication)->response.setStatus(204));
  }
}
