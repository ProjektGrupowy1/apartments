package com.booking.apartments.controller;

import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.utility.ApartmentException;
import com.booking.apartments.utility.Session;
import com.booking.apartments.utility.enums.Role;
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
    public RedirectView loginIn(@PathVariable("username") String username, @PathVariable("password") String password) throws ApartmentException {

        session.addParam("username", username);
        String role = authenticationService.getUserRole(username);

        return redirectUser(role);
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
        private String surname;
        private String username;
        private String password;
        private String rola;
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
        user.setSurname(newUser.getSurname());
        user.setUsername(newUser.getUsername());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setRole(newUser.getRola());
        user.setEnabled(1);

        authenticationService.addNewUser(user);
        session.addParam("username", user.getUsername());

        return redirectUser(user.getRole());
    }

    private RedirectView redirectUser(String role) throws ApartmentException {
        RedirectView redirectView = null;

        if (Role.Client.toString().contains(role)) {
            redirectView = new RedirectView("/search_engine");
        } else if (Role.Owner.toString().contains(role)) {
            redirectView = new RedirectView("/manage_hotels");
        } else if (Role.Admin.toString().contains(role)) {
            redirectView = new RedirectView("/manage_account");
        } else {
            throw new ApartmentException("Błąd biznesowy","W systemie nie ma takie profilu.");
        }
        return redirectView;
    }

    @RequestMapping(value = "/correct_logout", method = RequestMethod.GET)
    public ModelAndView correctLogout() {
        ModelAndView modelAndView = new ModelAndView("logout");

        session.removeParam("username");

        return modelAndView;
    }

    @RequestMapping(value = "/login_error", method = RequestMethod.GET)
    public ModelAndView incorrectLogin() {
        return new ModelAndView("error_login");
    }

}
