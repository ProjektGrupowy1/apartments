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
    private Mapper mapper;

    @Autowired
    private Session session;

    @Autowired
    private ManageTheHotelService manageTheHotelService;

    @Autowired
    private SearchEngineService searchEngineService;

    @RequestMapping(value = "/search_engine", method = RequestMethod.GET)
    public ModelAndView showSearchEnginePage(@RequestParam(value = "city", required = false) String city,
                                             @RequestParam(value = "hotel_name", required = false) String hotelName,
                                             @RequestParam(value = "date_start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateStart,
                                             @RequestParam(value = "date_end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateEnd) throws ApartmentException {
        ModelAndView searchEngineModelAndView = new ModelAndView("/client/search_engine");

        validationSearchEngineData(city,hotelName);

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
    public ModelAndView showDetailsOfTheApartmentPage(@RequestParam(value = "id_apartment") Integer idApartment) {
        ModelAndView detailsOfTheApartmentModelAndView = new ModelAndView("/client/details_of_the_apartment");

        Mapper.CustomerInformationAboutTheApartmentMapper apartment = mapper.customerInformationAboutTheApartment
                .apply(manageTheHotelService.getApartment(idApartment));

        detailsOfTheApartmentModelAndView.addObject("apartment", apartment);

        return detailsOfTheApartmentModelAndView;
    }

    static void validationSearchEngineData(String... data) throws ApartmentException {

        String statement = "";

        if(data[0]!=null && data[0].length() > 100){
            statement = "Wprowadzono niepoprawne miasto.";
        } else if(data[1]!=null && data[1].length() > 100){
            statement = "Wprowadzono niepoprawną nazwe hotelu.";
        }
        if(!"".contains(statement)) throw new ApartmentException("Błąd biznesowy", statement);
    }
}
