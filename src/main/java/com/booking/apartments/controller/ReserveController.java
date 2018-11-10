package com.booking.apartments.controller;

import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.service.ManageTheHotelService;
import com.booking.apartments.service.ReserveService;
import com.booking.apartments.utility.Session;
import com.booking.apartments.utility.enums.Profile;
import com.booking.apartments.utility.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@SessionAttributes({"email", "profile"})
//@SessionAttributes("email")
public class ReserveController {

    @Autowired
    Session session;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ManageTheHotelService manageTheHotelService;

    @Autowired
    ReserveService reserveService;

    private Set<Status> currentStatus = new HashSet<Status>() {{
        add(Status.Waiting);
        add(Status.Approved);
    }};

    private Set<Status> historicalStatus = new HashSet<Status>() {{
        add(Status.Suspended);
        add(Status.Ended);
    }};

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

        userReservationModelAndView.addObject("reservations", reserveService.findAllReservation(currentStatus));
        userReservationModelAndView.addObject("history", reserveService.findAllReservation(historicalStatus));

        return userReservationModelAndView;
    }

    @RequestMapping(value = "/reserved_apartments", method = RequestMethod.GET)
    public ModelAndView getReservedApartmentsPage() {
        ModelAndView reservedApartmentsPageModelAndView = new ModelAndView("/owner/reserved_apartments");

        reservedApartmentsPageModelAndView.addObject("reservations", reserveService.findAllOwnersReservations(currentStatus));
        reservedApartmentsPageModelAndView.addObject("history", reserveService.findAllOwnersReservations(historicalStatus));

        return reservedApartmentsPageModelAndView;
    }


    @RequestMapping(value = "/cancel_rezerwation/{id_reservation}", method = RequestMethod.GET)
    public RedirectView cancelReservation(@PathVariable("id_reservation") Integer idReserwation) {
        RedirectView cancelReservationApartmentRedirectView = null;
        if (Profile.Client.toString().contains(session.getParam("profile").toString())) {
            cancelReservationApartmentRedirectView = new RedirectView("/user_reservations");
        } else if (Profile.Owner.toString().contains(session.getParam("profile").toString())) {
            cancelReservationApartmentRedirectView = new RedirectView("/reserved_apartments");
        }
        reserveService.cancelReservation(idReserwation);
        return cancelReservationApartmentRedirectView;
    }

    @RequestMapping(value = "/accept_reservations/{id_reservation}", method = RequestMethod.GET)
    public RedirectView acceptReservation(@PathVariable("id_reservation") Integer idReserwation) {
        RedirectView acceptReservationApartmentRedirectView = new RedirectView("/reserved_apartments");
        reserveService.acceptReservation(idReserwation);
        return acceptReservationApartmentRedirectView;
    }
}
