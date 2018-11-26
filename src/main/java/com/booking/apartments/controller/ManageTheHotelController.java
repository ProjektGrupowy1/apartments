package com.booking.apartments.controller;

import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.service.ManageTheHotelService;
import com.booking.apartments.utility.ApartmentException;
import com.booking.apartments.utility.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@SessionAttributes("email")
@SessionAttributes({"email", "profile"})
public class ManageTheHotelController {

    @Autowired
    private Session session;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ManageTheHotelService manageTheHotelService;

    @Autowired
    private Mapper mapper;

    @RequestMapping(value = "/manage_hotels", method = RequestMethod.GET)
    public ModelAndView showManageHotelsPage() {
        ModelAndView manageHotelsModelAndView;
        String a = session.getParam("profile").toString();
        if (session.getParam("profile").toString().equals("Admin")) {
            manageHotelsModelAndView  = new ModelAndView("/admin/manage_hotels_admin");
        } else {
            manageHotelsModelAndView  = new ModelAndView("/owner/manage_hotels");
        }

        int idOwner = authenticationService.getUserId(session.getParam("email").toString());

        List<Mapper.HotelMapper> hotels = manageTheHotelService.getHotels(idOwner).stream().map(mapper.mapTheHotel).collect(Collectors.toList());

        manageHotelsModelAndView.addObject("hotels", hotels);

        return manageHotelsModelAndView;
    }

    @RequestMapping(value = "/details_of_the_hotel", method = RequestMethod.GET)
    public ModelAndView showDetailsOfTheHotelPage(@RequestParam(value = "id_hotel", required = false) Integer idHotel,
                                                  @RequestParam(value = "hotel_name", required = false) String hotelName) {
        ModelAndView detailsOfTheHotelModelAndView = new ModelAndView("/owner/details_of_the_hotel");

        List<Mapper.ApartmentMapper> apartments = null;
        if (idHotel != null) {
            apartments = manageTheHotelService.getApartments(idHotel).stream().map(mapper.mapTheApartment).collect(Collectors.toList());
            detailsOfTheHotelModelAndView.addObject("hotel_name", manageTheHotelService.getHotelName(idHotel));
        } else if (!hotelName.isEmpty()) {
            apartments = manageTheHotelService.getApartments(hotelName).stream().map(mapper.mapTheApartment).collect(Collectors.toList());
            detailsOfTheHotelModelAndView.addObject("hotel_name", hotelName);
        } else {
            apartments = new ArrayList<>();
        }

        detailsOfTheHotelModelAndView.addObject("apartments", apartments);
        return detailsOfTheHotelModelAndView;
    }

    @RequestMapping(value = "/add_hotel", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView addNewHotel(@ModelAttribute("new_hotel") Mapper.NewHotelMapper newHotelMapper) throws ApartmentException {

        validationOfHotelData(true, newHotelMapper.getName(), newHotelMapper.getDescription(), newHotelMapper.getStreet(),
                newHotelMapper.getCity(), newHotelMapper.getState(), newHotelMapper.getPostalCode());

        manageTheHotelService.addNewHotel(newHotelMapper);

        return new RedirectView("/manage_hotels");
    }

    @RequestMapping(value = "/hotel_modification", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView modifyTheHotel(@ModelAttribute("modified_hotel") Mapper.HotelMapper hotelMapper) throws ApartmentException {

        validationOfHotelData(false, hotelMapper.getName(), hotelMapper.getDescription(), hotelMapper.getStreet(),
                hotelMapper.getCity(), hotelMapper.getState(), hotelMapper.getPostalCode());


        manageTheHotelService.modyfyTheHotel(hotelMapper);

        return new RedirectView("/manage_hotels");
    }

    @RequestMapping(value = "/remove_hotel/{id_hotel}", method = RequestMethod.GET)
    public RedirectView hotelRemoval(@PathVariable("id_hotel") int idHotel) {
        manageTheHotelService.deleteHotel(idHotel);
        return new RedirectView("/manage_hotels");
    }
    @RequestMapping(value = "/change_hotel_status/{id_hotel}", method = RequestMethod.GET)
    public RedirectView hotelStatusChange(@PathVariable("id_hotel") int idHotel) {
        manageTheHotelService.changeHotelStatus(idHotel);
        return new RedirectView("/manage_hotels");
    }

    @RequestMapping(value = "/add_apartment", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView addNewApartment(@ModelAttribute("new_apartment") Mapper.NewApartmentMapper newApartmentMapper) throws ApartmentException {

        RedirectView detailOfTheHotelRedirectView = new RedirectView("/details_of_the_hotel");

        validationOfApartmentData(true, newApartmentMapper.getName());

        manageTheHotelService.addNewApartment(newApartmentMapper);

        detailOfTheHotelRedirectView.addStaticAttribute("hotel_name", newApartmentMapper.getHotelName());

        return detailOfTheHotelRedirectView;
    }

    @RequestMapping(value = "/apartment_modification", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView modifyTheApartment(@ModelAttribute("modified_apartment") Mapper.ApartmentMapper apartmentMapper,
                                    RedirectAttributes redirectAttributes) throws ApartmentException {

        validationOfApartmentData(false, apartmentMapper.getName());

        manageTheHotelService.modyfyTheApartment(apartmentMapper);

        redirectAttributes.addAttribute("hotel_name", apartmentMapper.getHotelName());

        return new RedirectView("/details_of_the_hotel");
    }

    @RequestMapping(value = "/remove_apartment/{hotel_name}/{id_apartment}", method = RequestMethod.GET)
    public RedirectView apartmentRemoval(@PathVariable("hotel_name") String hotelName, @PathVariable("id_apartment") int idApartment) {
        RedirectView detailOfTheHotelRedirectView = new RedirectView("/details_of_the_hotel");
        manageTheHotelService.deleteApartment(idApartment);
        detailOfTheHotelRedirectView.addStaticAttribute("hotel_name", hotelName);
        return detailOfTheHotelRedirectView;
    }

    static void validationOfHotelData(boolean newHotel, String... data) throws ApartmentException {

        String variableMessage = "", statement = "";

        if (newHotel) {
            variableMessage = " Hotel nie został dodany.";
        } else {
            variableMessage = " Dane o hotelu nie zostały zmienione.";
        }

        if (data[0] != null && data[0].length() > 100) {
            statement = "Wprowadzono niepoprawny nazwe hotelu lub nazwa hotelu już istnieje w systemie." + variableMessage;
        } else if (data[1] != null && data[1].length() > 2000) {
            statement = "Wprowadzono niepoprawny opis hotelu." + variableMessage;
        } else if (data[2] != null && data[2].length() > 100) {
            statement = "Wprowadzono niepoprawną ulice." + variableMessage;
        } else if (data[3] != null && data[3].length() > 100) {
            statement = "Wprowadzono niepoprawne miasto." + variableMessage;
        } else if (data[4] != null && data[4].length() > 100) {
            statement = "Wprowadzono niepoprawne kraj." + variableMessage;
        } else if (data[5] != null && data[5].length() > 10) {
            statement = "Wprowadzono niepoprawne kod pocztowy." + variableMessage;
        }

        if (!"".contains(statement)) throw new ApartmentException("Błąd biznesowy", statement);
    }

    static void validationOfApartmentData(boolean newApartment, String name) throws ApartmentException {

        String variableMessage = "", statement = "";

        if (newApartment) {
            variableMessage = " Apartment nie został dodany.";
        } else {
            variableMessage = " Dane o apartamencie nie zostały zmienione.";
        }

        if (name != null && name.length() > 60) {
            statement = "Wprowadzono niepoprawny nazwe apartamentu." + variableMessage;
        }

        if (!"".contains(statement)) throw new ApartmentException("Błąd biznesowy", statement);
    }

}
