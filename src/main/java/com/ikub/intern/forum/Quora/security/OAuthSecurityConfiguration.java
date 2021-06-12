package com.ikub.intern.forum.Quora.security;

import com.ikub.intern.forum.Quora.security.oauth.CustomOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;


@Configuration
public class OAuthSecurityConfiguration extends WebSecurityConfigurerAdapter  {
    @Autowired
    private CustomOauth2UserService customOauth2UserService;
    @Autowired
    ClientRegistrationRepository clientRegistrationRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/oauth2/authorization/google").permitAll()
                    .antMatchers("/users/login").permitAll()
                    .antMatchers("/static/bcg.jpg").permitAll()
                    .antMatchers("/**").authenticated()
                    .antMatchers("/category/save").hasRole("admin")
                    .and()
                    .oauth2Login()
                    .defaultSuccessUrl("/users/postlogin",true)
                    .loginPage("/users/login")
                    .userInfoEndpoint().userService(customOauth2UserService);

            //http.csrf().disable();
    }


}
