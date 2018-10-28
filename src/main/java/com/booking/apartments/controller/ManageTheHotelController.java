package com.booking.apartments.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ManageTheHotelController {

    @RequestMapping(value = "/manage_hotels", method = RequestMethod.GET)
    public ModelAndView getManageHotelsPage() {
        ModelAndView manageHotelsModelAndView = new ModelAndView("/owner/manage_hotels");
        return manageHotelsModelAndView;
    }

    @RequestMapping(value = "/details_of_the_hotel", method = RequestMethod.GET)
    public ModelAndView getDetailsOfTheHotelPage() {
        ModelAndView detailsOfTheHotelModelAndView = new ModelAndView("/owner/details_of_the_hotel");
        return detailsOfTheHotelModelAndView;
    }
}
