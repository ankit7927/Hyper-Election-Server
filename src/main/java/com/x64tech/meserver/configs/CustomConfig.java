package com.x64tech.meserver.configs;

import com.x64tech.meserver.models.Role;
import com.x64tech.meserver.services.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class CustomConfig extends WebSecurityConfigurerAdapter {

    @Value("${admin.username}")
    private String adminUname;

    @Value("${admin.password}")
    private String adminPass;

    @Bean
    public UserDetailsService getUserDetailService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authentication = new DaoAuthenticationProvider();
        authentication.setPasswordEncoder(passwordEncoder());
        authentication.setUserDetailsService(getUserDetailService());
        return authentication;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        
        auth.inMemoryAuthentication()
            .withUser(adminUname)
            .password(passwordEncoder().encode(adminPass))
            .roles(Role.ADMIN.toString());
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**")
                .hasRole(Role.ADMIN.toString())
                .antMatchers("/profile/**")
                .authenticated()
                .antMatchers("/election/give-vote")
                .authenticated()
                .antMatchers("/election/apply-candidate")
                .authenticated()
                .antMatchers("/**")
                .permitAll()
                .and()

                // authenticate use on form based system
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
                .and()

                // session management to persist user information
                // on page refresh
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }
}
