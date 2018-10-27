package com.booking.apartments.controller;

import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.utility.ApartmentException;
import com.booking.apartments.utility.Session;
import com.booking.apartments.utility.enums.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Session session;

    @RequestMapping(value = "/screenname", method = RequestMethod.POST)
    public RedirectView loginIn(@PathVariable("email") String email, @PathVariable("password") String password) throws ApartmentException {

        session.addParam("email", email);
        return redirectUser(authenticationService.getUserProfile(email));
    }

    @RequestMapping(value = "/screenname", method = RequestMethod.GET)
    public ModelAndView getLoginPage() {
        ModelAndView loginModelAndView = new ModelAndView("login");

        return loginModelAndView;
    }

    @Getter
    @AllArgsConstructor
    public class NewUser {
        private String name;
        private String lastname;
        private String street;
        private String city;
        private String phone;
        private String email;
        private String password;
        private String profile;
    }

    @RequestMapping(value = "/sign_in", method = RequestMethod.GET)
    public ModelAndView getSignInPage() {
        return new ModelAndView("registration");
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView signIn(@ModelAttribute("new_user") NewUser newUser) throws ApartmentException {

        UserEntity user = new UserEntity();
        user.setName(newUser.getName());
        user.setLastname(newUser.getLastname());
        user.setStreet(newUser.getStreet());
        user.setCityId(1); // trzeba wyszukać na podstawie nazwy miasta identyfikator
        user.setPhone(newUser.getPhone());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setIdProfile(authenticationService.getProfileId(newUser.getProfile()));
        user.setEnabled(1);

        
        authenticationService.addNewUser(user);
        session.addParam("email", user.getEmail());

        return redirectUser(newUser.getProfile());
    }

    private RedirectView redirectUser(String profile) throws ApartmentException {
        RedirectView redirectView = null;

        if (Profile.Client.toString().contains(profile)) {
            redirectView = new RedirectView("/search_engine");
        } else if (Profile.Owner.toString().contains(profile)) {
            redirectView = new RedirectView("/manage_hotels");
        } else if (Profile.Admin.toString().contains(profile)) {
            redirectView = new RedirectView("/manage_account");
        } else {
            throw new ApartmentException("Błąd biznesowy","W systemie nie ma takie profilu.");
        }
        return redirectView;
    }

    @RequestMapping(value = "/correct_logout", method = RequestMethod.GET)
    public ModelAndView correctLogout() {
        ModelAndView modelAndView = new ModelAndView("logout");

        session.removeParam("email");

        return modelAndView;
    }

    @RequestMapping(value = "/login_error", method = RequestMethod.GET)
    public ModelAndView incorrectLogin() {
        return new ModelAndView("error_login");
    }

}
