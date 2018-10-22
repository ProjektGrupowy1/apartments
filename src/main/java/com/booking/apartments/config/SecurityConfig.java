package com.booking.apartments.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = {DatabaseConfig.class})
@EnableGlobalMethodSecurity(securedEnabled = true)
@SessionAttributes("email")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/h2-console/**", "/sign_in", "/registration", "/css/**", "/img/**", "/error","/login_error").permitAll()
                .antMatchers("/search_engine").hasAuthority("Client")
                .antMatchers("/manage_hotels").hasAuthority("Owner")
                .antMatchers("/manage_account").hasAuthority("Admin")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/screenname/${email}/${password}")
                .usernameParameter("email")
                .passwordParameter("password")
                .failureUrl("/login_error")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/correct_logout")
                .invalidateHttpSession(true)
                .permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Autowired
    protected void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select email, password, enabled from users where email=?")
                .authoritiesByUsernameQuery("select users.email, profiles.profileName from users join profiles on profiles.id_profile = users.id_profile where users.email=?")
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
