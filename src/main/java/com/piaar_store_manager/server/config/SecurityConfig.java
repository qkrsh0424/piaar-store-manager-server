package com.piaar_store_manager.server.config;

import com.piaar_store_manager.server.config.auth.JwtAuthenticationFilter;
import com.piaar_store_manager.server.config.auth.JwtAuthorizationFilter;
import com.piaar_store_manager.server.config.auth.JwtLogoutSuccessHandler;
import com.piaar_store_manager.server.domain.user.repository.UserRepository;
import com.piaar_store_manager.server.model.refresh_token.repository.RefreshTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${app.jwt.access.secret}")
    private String accessTokenSecret;

    @Value("${app.jwt.refresh.secret}")
    private String refreshTokenSecret;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .cors()
            .and()
                .authorizeRequests()
                .anyRequest().permitAll()
            .and()
                .formLogin().disable()
                .logout()
                    .logoutUrl("/api/v1/logout")
                    .logoutSuccessHandler(new JwtLogoutSuccessHandler())
                    .deleteCookies("piaar_actoken")
                .and()
                .httpBasic().disable()
                .csrf()
                .disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), userRepository, refreshTokenRepository, accessTokenSecret, refreshTokenSecret)) // AuthenticationManager
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, refreshTokenRepository, accessTokenSecret, refreshTokenSecret)) // AuthenticationManager
            ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 setting 시에는 시큐리티를 무시한다.
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
        web.httpFirewall(defaultHttpFirewall());
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }
}
