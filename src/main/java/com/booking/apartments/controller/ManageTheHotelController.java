package com.booking.apartments.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ManageTheHotelController {

    @RequestMapping(value = "/manage_hotels",method = RequestMethod.GET)
    public ModelAndView getManageHotelsPage(){
        ModelAndView searchEngineModelAndView = new ModelAndView("/owner/manage_hotels");
        return searchEngineModelAndView;
    }
}
