package com.booking.apartments.controller;

import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.service.ManageTheHotelService;
import com.booking.apartments.service.SearchEngineService;
import com.booking.apartments.utility.ApartmentException;
import com.booking.apartments.utility.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@SessionAttributes("email")
@SessionAttributes({"email", "profile"})
public class SearchEngineController {

    @Autowired
    Mapper mapper;

    @Autowired
    Session session;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ManageTheHotelService manageTheHotelService;

    @Autowired
    SearchEngineService searchEngineService;

    @RequestMapping(value = "/search_engine", method = RequestMethod.GET)
    public ModelAndView getSearchEnginePage(@RequestParam(value = "city", required = false) String city,
                                            @RequestParam(value = "hotel_name", required = false) String hotelName,
                                            @RequestParam(value = "date_start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateStart,
                                            @RequestParam(value = "date_end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateEnd) {
        ModelAndView searchEngineModelAndView = new ModelAndView("/client/search_engine");

        List<Mapper.CustomerInformationAboutTheApartmentMapper> apartments = searchEngineService.findApartmentsThatMeetTheCriteria(city, hotelName, dateStart, dateEnd).stream().map(mapper.customerInformationAboutTheApartment).collect(Collectors.toList());

        searchEngineModelAndView.addObject("apartments", apartments);
        if(session.getParam("email")!=null){
            searchEngineModelAndView.addObject("profile", session.getParam("profile").toString());
        }
        else{
            searchEngineModelAndView.addObject("profile", null);
        }

        return searchEngineModelAndView;
    }

    @RequestMapping(value = "/details_of_the_apartment", method = RequestMethod.POST)
    public ModelAndView getDetailsOfTheApartmentPage(@RequestParam(value = "id_apartment") Integer idApartment) throws ApartmentException {
        ModelAndView detailsOfTheApartmentModelAndView = new ModelAndView("/client/details_of_the_apartment");

        Mapper.CustomerInformationAboutTheApartmentMapper apartment = mapper.customerInformationAboutTheApartment
                .apply(manageTheHotelService.getApartment(idApartment));

        detailsOfTheApartmentModelAndView.addObject("apartment", apartment);

        return detailsOfTheApartmentModelAndView;
    }
}
