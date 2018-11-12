package com.booking.apartments.controller;

import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.utility.ApartmentException;
import com.booking.apartments.utility.Session;
import com.booking.apartments.utility.enums.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@SessionAttributes({"email", "profile"})
//@SessionAttributes("email")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private Session session;

    @RequestMapping(value = "/screenname", method = RequestMethod.POST)
    public RedirectView loginIn(@PathVariable("email") String email, @PathVariable("password") String password) throws ApartmentException {
        return redirectUser(authenticationService.getUserProfile(email));
    }

    @RequestMapping(value = "/screenname", method = RequestMethod.GET)
    public ModelAndView getLoginPage() {
        ModelAndView loginModelAndView = new ModelAndView("login");

        return loginModelAndView;
    }

    @RequestMapping(value = "/select_the_page", method = RequestMethod.GET)
    public RedirectView selectThePage() throws ApartmentException {
        return redirectUser(authenticationService.getUserProfile(session.getParam("email").toString()));
    }

    @RequestMapping(value = "/sign_in", method = RequestMethod.GET)
    public ModelAndView getSignInPage() {
        return new ModelAndView("registration");
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView signIn(@ModelAttribute("new_user") Mapper.NewUser newUser) throws ApartmentException {

//        UserEntity user = new UserEntity();
//        user.setName(newUser.getName());
//        user.setLastname(newUser.getLastname());
//        user.setStreet(newUser.getStreet());
////        user.setIdCity(1); // trzeba wyszukać na podstawie nazwy miasta identyfikator
//
//        user.setIdCity(authenticationService.getIdByCityName(newUser.getCity())); // trzeba wyszukać na podstawie nazwy miasta identyfikator
//        user.setPhone(newUser.getPhone());
//        user.setEmail(newUser.getEmail());
//        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
//        user.setIdProfile(authenticationService.getProfileId(newUser.getProfile()));
//        user.setEnabled(1);
        authenticationService.addNewUser(newUser);
        session.addParam("email", newUser.getEmail());
        session.addParam("profile", newUser.getProfile());

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
        ModelAndView correctLogoutModelAndView = new ModelAndView("logout");

        session.removeParam("email");
        session.removeParam("profile");
        return correctLogoutModelAndView;
    }

    @RequestMapping(value = "/login_error", method = RequestMethod.GET)
    public ModelAndView incorrectLogin() {
        return new ModelAndView("error_login");
    }

    @RequestMapping(value = "/user_profile", method = RequestMethod.GET)
    public ModelAndView getUserProfilePage() {
        ModelAndView userProfileModelAndView = new ModelAndView("user_profile");

        userProfileModelAndView.addObject("user",authenticationService.getUserByEmail(session.getParam("email").toString()));

        return userProfileModelAndView;
    }

    @RequestMapping(value = "/modified_user", method = RequestMethod.POST)
    public @ResponseBody RedirectView modifiedUser(@ModelAttribute("modified_user")Mapper.User user) {
        RedirectView modifiedUserRedirectView = new RedirectView("/user_profile");

//        System.out.println("user.getEmail = "+user.getEmail());
//        System.out.println("user.getEmail = "+user.getPassword());
//        System.out.println("user.getEmail = "+user.getLastname());
//        System.out.println("user.getEmail = "+user.getName());
//        System.out.println("user.getEmail = "+user.getProfile());
//        System.out.println("user.getEmail = "+user.getStreet());
//        System.out.println("user.getEmail = "+user);


        authenticationService.modifyUser(user);
        session.updateParam("email", user.getEmail());
        session.updateParam("profile", user.getProfile());

        return modifiedUserRedirectView;
    }

}
