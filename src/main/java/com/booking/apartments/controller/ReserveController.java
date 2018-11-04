package com.booking.apartments.controller;

import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.service.ManageTheHotelService;
import com.booking.apartments.service.ReserveService;
import com.booking.apartments.utility.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;

@RestController
@SessionAttributes("email")
public class ReserveController {

    @Autowired
    Session session;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ManageTheHotelService manageTheHotelService;

    @Autowired
    ReserveService reserveService;

    @RequestMapping(value = "/request_for_rent", method = RequestMethod.POST)
    public RedirectView reserveApartment(@RequestParam(value = "id_apartment") Integer idApartment,
                                         @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                         @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        RedirectView reserveApartmentRedirectView = new RedirectView("/user_reservations");

        reserveService.addNewReservation(idApartment, startDate, endDate);

        return reserveApartmentRedirectView;
    }

//    @RequestMapping(value = "/reserve_apartment", method = RequestMethod.POST)
//    public RedirectView reserveApartment(@RequestParam(value = "id_apartment") Integer idApartment,
//                                         @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//                                         @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
//
//        RedirectView reserveApartmentRedirectView = new RedirectView("/user_reservations");
//
//        reserveService.addNewReservation(idApartment, startDate, endDate);
//
//        return reserveApartmentRedirectView;
//    }

    @RequestMapping(value = "/user_reservations", method = RequestMethod.GET)
    public ModelAndView getUserReservationPage() {
        ModelAndView userReservationModelAndView = new ModelAndView("/client/user_reservations");

        userReservationModelAndView.addObject("reservations",reserveService.findAllReservation());

        return userReservationModelAndView;
    }

    @RequestMapping(value = "/cancel_rezerwation", method = RequestMethod.GET)
    public RedirectView cancelReservation(@RequestParam("id_reservation") Integer idReserwation){
        RedirectView cancelReservationApartmentRedirectView = new RedirectView("/user_reservations");
        reserveService.cancelReservation(idReserwation);
        return cancelReservationApartmentRedirectView;
    }
}
