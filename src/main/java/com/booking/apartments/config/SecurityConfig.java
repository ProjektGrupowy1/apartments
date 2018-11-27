package com.booking.apartments.config;

import com.booking.apartments.repository.UserRepository;
import com.booking.apartments.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.SessionAttributes;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@SessionAttributes({"email","profile"})
//@SessionAttributes("email")
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EnableAspectJAutoProxy
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationService authenticationService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/", "/h2-console/**", "/sign_in", "/registration", "/search_engine", "/css/**", "/img/**", "/error", "/login_error")
                .permitAll()
                .antMatchers("/details_of_the_apartment", "/user_reservations")
                .hasAuthority("Client")
                .antMatchers("/manage_hotels", "/add_hotel", "/hotel_modification", "/remove_hotel", "/details_of_the_hotel", "/reserved_apartments")
                .hasAuthority("Owner")
                .antMatchers("/manage_hotels_admin")
                .hasAuthority("Admin")
                .antMatchers("/manage_users")
                .hasAuthority("Admin")
                .antMatchers("/user_profile", "/select_the_page")
                .hasAuthority("User")

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
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select email, password, enabled from user where email=?")
//                .authoritiesByUsernameQuery("select user.email, profile.name from user join profile on profile.id_profile = user.id_profile where user.email=?")
//                .passwordEncoder(passwordEncoder());

        auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
