package com.booking.apartments.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getHomePage(){
        ModelAndView homePageModelAndView = new ModelAndView("home");
        return homePageModelAndView;
    }

}
