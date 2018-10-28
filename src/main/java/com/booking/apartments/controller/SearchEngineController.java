package com.booking.apartments.controller;

import com.booking.apartments.utility.ApartmentException;
import com.booking.apartments.utility.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class SearchEngineController {

    @Autowired
    Session session;

    @Autowired
    BeanFactoryPostProcessor beanFactoryPostProcessor;

    @RequestMapping(value = "/search_engine",method = RequestMethod.GET)
    public ModelAndView getSearchEnginePage(HttpSession session2) throws ApartmentException {
        ModelAndView searchEngineModelAndView = new ModelAndView("/client/search_engine");

//        session.addParam("email", user.getEmail());

        return searchEngineModelAndView;
    }

    @RequestMapping(value = "/details_of_the_apartment",method = RequestMethod.GET)
    public ModelAndView getDetailsOfTheApartmentPage(HttpServletRequest request, HttpServletResponse response) throws ApartmentException {
        ModelAndView DetailsOfTheApartmentModelAndView = new ModelAndView("/client/details_of_the_apartment");
        return DetailsOfTheApartmentModelAndView;
    }

    @RequestMapping(value = "/user_reservations",method = RequestMethod.GET)
    public ModelAndView getUserReservationPage(HttpServletRequest request, HttpServletResponse response) throws ApartmentException {
        ModelAndView UserReservationModelAndView = new ModelAndView("/client/user_reservations");
        return UserReservationModelAndView;
    }
}
