package com.booking.apartments.controller;

import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.utility.ApartmentException;
import com.booking.apartments.utility.Session;
import com.booking.apartments.utility.enums.Profile;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ModelAndView showLoginPage() {
        ModelAndView loginModelAndView = new ModelAndView("login");

        return loginModelAndView;
    }

    @RequestMapping(value = "/select_the_page", method = RequestMethod.GET)
    public RedirectView selectThePage() throws ApartmentException {
        return redirectUser(authenticationService.getUserProfile(session.getParam("email").toString()));
    }

    @RequestMapping(value = "/sign_in", method = RequestMethod.GET)
    public ModelAndView showSignInPage() {
        return new ModelAndView("registration");
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView signIn(@ModelAttribute("new_user") Mapper.NewUserMapper newUserMapper) throws ApartmentException {

        validationOfUserData(true, newUserMapper.getEmail(), newUserMapper.getPassword(), newUserMapper.getName(), newUserMapper.getLastname(),
                newUserMapper.getPhone(), newUserMapper.getStreet(), newUserMapper.getCity());

        authenticationService.addNewUser(newUserMapper);
        session.addParam("email", newUserMapper.getEmail());
        session.addParam("profile", newUserMapper.getProfile());

        return redirectUser(newUserMapper.getProfile());
    }

    static void validationOfUserData(boolean newUser, String... data) throws ApartmentException {

        String variableMessage = "", statement = "";

        if(newUser){
            variableMessage = " Użytkownik nie został zarejestrowany.";
        }else {
            variableMessage = " Dane użytkownika nie zostały zmienione.";
        }

        if (data[0].length() > 40) {
            statement = "Wprowadzono niepoprawny email." + variableMessage;
        } else if(data[1].length() > 200){
            statement = "Wprowadzono niepoprawne hasło." + variableMessage;
        } else if(data[2].length() > 45){
            statement = "Wprowadzono niepoprawne imie." + variableMessage;
        } else if(data[3].length() > 45){
            statement = "Wprowadzono niepoprawne nazwisko." + variableMessage;
        } else if(data[4].length() > 45){
            statement = "Wprowadzono niepoprawny telefon." + variableMessage;
        }else if(data[5].length() > 200){
            statement = "Wprowadzono niepoprawną ulice." + variableMessage;
        }else if(data[6].length() > 100){
            statement = "Wprowadzono niepoprawne miasto." + variableMessage;
        }

        if(!"".contains(statement)) throw new ApartmentException("Błąd biznesowy", statement);
    }

    private RedirectView redirectUser(String profile) throws ApartmentException {
        RedirectView redirectView = null;

        if (Profile.Client.toString().contains(profile)) {
            redirectView = new RedirectView("/search_engine");
        } else if (Profile.Owner.toString().contains(profile)) {
            redirectView = new RedirectView("/manage_hotels");
        } else if (Profile.Admin.toString().contains(profile)) {
            redirectView = new RedirectView("/manage_users");
        } else {
            throw new ApartmentException("Błąd biznesowy", "W systemie nie ma takie profilu.");
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
    public ModelAndView showUserProfilePage() {
        ModelAndView userProfileModelAndView = new ModelAndView("user_profile");

        userProfileModelAndView.addObject("user", authenticationService.getUserByEmail(session.getParam("email").toString()));

        return userProfileModelAndView;
    }

    @RequestMapping(value = "/modified_user", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView modifiedUser(@ModelAttribute("modified_user") Mapper.UserMapper userMapper) throws ApartmentException {
        RedirectView modifiedUserRedirectView = new RedirectView("/user_profile");

        validationOfUserData(false, userMapper.getEmail(), userMapper.getPassword(), userMapper.getName(), userMapper.getLastname(),
                userMapper.getPhone(), userMapper.getStreet(), userMapper.getCity());

        authenticationService.modifyUser(userMapper);
        session.updateParam("email", userMapper.getEmail());
        session.updateParam("profile", userMapper.getProfile());

        return modifiedUserRedirectView;
    }

}
