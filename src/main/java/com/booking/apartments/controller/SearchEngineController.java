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

@RestController
public class SearchEngineController {

    @Autowired
    Session session;

    @Autowired
    BeanFactoryPostProcessor beanFactoryPostProcessor;

    @RequestMapping(value = "/search_engine",method = RequestMethod.GET)
    public ModelAndView getSearchEnginePage(HttpServletRequest request, HttpServletResponse response) throws ApartmentException {
        ModelAndView searchEngineModelAndView = new ModelAndView("/client/search_engine");
        return searchEngineModelAndView;
    }
}
