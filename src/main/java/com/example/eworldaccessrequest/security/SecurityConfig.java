package com.example.eworldaccessrequest.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
//
    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http
//                .formLogin().permitAll().and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/api/v1/access/**").hasAnyAuthority("ROLE_ADMIN2")
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic();

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));


        //GOOD ONE!
//        http
//                .formLogin()
//                .permitAll()
//                .and().csrf();
//        http
//                .authorizeRequests((authorizeRequests) ->
//                        authorizeRequests
//                                .antMatchers("/**").hasRole("ADMIN2")
//                )
//                .authorizeRequests()
////                .antMatchers(GET, "/api/v1/access/employee/**").hasAnyAuthority("ROLE_ADMIN2")
//                .anyRequest().authenticated()
        ;
//        http.authorizeRequests((requests) -> {
//            ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)requests.anyRequest()).authenticated();
//        });
//        http.formLogin();
//        http.httpBasic();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
