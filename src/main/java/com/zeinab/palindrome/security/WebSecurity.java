package com.zeinab.palindrome.security;

import com.zeinab.palindrome.service.UserDetailsServiceImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/status").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(jwtAuthorizationFilter())
                .addFilter(new LoginFilter(authenticationManager()))
                .addFilter(new ResourceAccessFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    public LoginFilter jwtAuthorizationFilter() throws Exception {
        LoginFilter jwtAuthenticationFilter = new LoginFilter(authenticationManager());
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");
        return jwtAuthenticationFilter;
    }
}