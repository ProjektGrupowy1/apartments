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
    Session session;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ManageTheHotelService manageTheHotelService;

    @Autowired
    Mapper mapper;

    @RequestMapping(value = "/manage_hotels", method = RequestMethod.GET)
    public ModelAndView getManageHotelsPage() {
        ModelAndView manageHotelsModelAndView = new ModelAndView("/owner/manage_hotels");

        int idOwner = authenticationService.getUserId(session.getParam("email").toString());

        List<Mapper.HotelMapper> hotels = manageTheHotelService.getHotels(idOwner).stream().map(mapper.mapTheHotel).collect(Collectors.toList());

        manageHotelsModelAndView.addObject("hotels", hotels);
//        manageHotelsModelAndView.addObject("stars", );

        return manageHotelsModelAndView;
    }

    @RequestMapping(value = "/details_of_the_hotel", method = RequestMethod.GET)
    public ModelAndView getDetailsOfTheHotelPage(@RequestParam(value = "id_hotel", required = false) Integer idHotel,
                                                 @RequestParam(value = "hotel_name", required = false) String hotelName) {
        ModelAndView detailsOfTheHotelModelAndView = new ModelAndView("/owner/details_of_the_hotel");

        System.out.println("hotelName = " + hotelName);

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
    RedirectView addHotel(@ModelAttribute("new_hotel") Mapper.NewHotelMapper newHotelMapper) throws ApartmentException {

        manageTheHotelService.addNewHotel(newHotelMapper);

        return new RedirectView("/manage_hotels");
    }

    @RequestMapping(value = "/hotel_modification", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView modifyTheHotel(@ModelAttribute("modified_hotel") Mapper.HotelMapper hotelMapper) throws ApartmentException {

        manageTheHotelService.modyfyTheHotel(hotelMapper);

        return new RedirectView("/manage_hotels");
    }

    @RequestMapping(value = "/remove_hotel/{id_hotel}", method = RequestMethod.GET)
    public RedirectView hotelRemoval(@PathVariable("id_hotel") int idHotel) {
        manageTheHotelService.deleteHotel(idHotel);
        return new RedirectView("/manage_hotels");
    }

    @RequestMapping(value = "/add_apartment", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView addApartment(@ModelAttribute("new_apartment") Mapper.NewApartmentMapper newApartmentMapper) throws ApartmentException {

        RedirectView detailOfTheHotelRedirectView = new RedirectView("/details_of_the_hotel");

        manageTheHotelService.addApartment(newApartmentMapper);

        detailOfTheHotelRedirectView.addStaticAttribute("hotel_name", newApartmentMapper.getHotelName());

        return detailOfTheHotelRedirectView;
    }

    @RequestMapping(value = "/apartment_modification", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView modifyTheHotel(@ModelAttribute("modified_apartment") Mapper.ApartmentMapper apartmentMapper,
                                RedirectAttributes redirectAttributes) throws ApartmentException {

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

//    @RequestMapping(value = "/delete_hotel/{id_hotel}", method = RequestMethod.DELETE)
//    public void deleteHotel(@PathVariable("id_hotel") int hotelName) {
//        manageTheHotelService.deleteHotel(hotelName);
//        System.out.println("dupa");
//    }
}
