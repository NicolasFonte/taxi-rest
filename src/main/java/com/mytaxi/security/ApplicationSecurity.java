package com.mytaxi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableWebSecurity
@Configuration
public class ApplicationSecurity extends WebSecurityConfigurerAdapter
{
    @Autowired
    private BasicAuthenticationPoint basicAuthenticationPoint;


    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // h2 by default
        http.headers().frameOptions().disable();

        http.csrf().disable()
            .authorizeRequests().anyRequest().authenticated()

            // enable db console on dev
            .antMatchers("/console/**").permitAll()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .httpBasic()
            .authenticationEntryPoint(basicAuthenticationPoint);
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.inMemoryAuthentication().withUser("driver1").password("driver01pw").roles("USER");
        auth.inMemoryAuthentication().withUser("driver2").password("driver02pw").roles("USER");
        auth.inMemoryAuthentication().withUser("driver3").password("driver03pw").roles("USER");
        auth.inMemoryAuthentication().withUser("driver4").password("driver04pw").roles("USER");
        auth.inMemoryAuthentication().withUser("driver5").password("driver05pw").roles("USER");
        auth.inMemoryAuthentication().withUser("driver6").password("driver06pw").roles("USER");
        auth.inMemoryAuthentication().withUser("driver7").password("driver07pw").roles("USER");
        auth.inMemoryAuthentication().withUser("driver8").password("driver08pw").roles("USER");

    }
}
