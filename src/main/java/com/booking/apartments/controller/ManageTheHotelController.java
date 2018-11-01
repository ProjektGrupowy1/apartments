package com.booking.apartments.controller;

import com.booking.apartments.entity.HotelEntity;
import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.service.ManageTheHotelService;
import com.booking.apartments.utility.ApartmentException;
import com.booking.apartments.utility.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@SessionAttributes("email")
public class ManageTheHotelController {

    @Autowired
    Session session;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ManageTheHotelService manageTheHotelService;

    @RequestMapping(value = "/manage_hotels", method = RequestMethod.GET)
    public ModelAndView getManageHotelsPage() {
        ModelAndView manageHotelsModelAndView = new ModelAndView("/owner/manage_hotels");

        int idOwner = authenticationService.getUserId(session.getParam("email").toString());

        List<NewHotel> hotels = manageTheHotelService.getHotels(idOwner).stream().map(mapTheHotel).collect(Collectors.toList());
        manageHotelsModelAndView.addObject("hotels", hotels);

        return manageHotelsModelAndView;
    }

    Function<HotelEntity, NewHotel> mapTheHotel = new Function<HotelEntity, NewHotel>() {
        public NewHotel apply(HotelEntity hotelEntity) {
            return new NewHotel(hotelEntity.getName(), hotelEntity.getDescription(), manageTheHotelService.getCityName(hotelEntity.getIdCity()),
                    hotelEntity.getStreet(), hotelEntity.getRating());
        }
    };

    @RequestMapping(value = "/details_of_the_hotel", method = RequestMethod.GET)
    public ModelAndView getDetailsOfTheHotelPage() {
        ModelAndView detailsOfTheHotelModelAndView = new ModelAndView("/owner/details_of_the_hotel");
        return detailsOfTheHotelModelAndView;
    }

    @Getter
    @AllArgsConstructor
    public class NewHotel {
        private String name;
        private String description;
        private String city;
        private String street;
        private int rating;
    }

    @RequestMapping(value = "/add_hotel", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView signIn(@ModelAttribute("new_hotel") NewHotel newHotel) throws ApartmentException {

        HotelEntity hotel = new HotelEntity();
        hotel.setIdCity(authenticationService.getIdByCityName(newHotel.getCity()));
        hotel.setIdOwner(authenticationService.getUserId(session.getParam("email").toString()));
        hotel.setDescription(newHotel.getDescription());
        hotel.setRating(newHotel.getRating());
        hotel.setStreet(newHotel.getStreet());
        hotel.setName(newHotel.getName());

        manageTheHotelService.addNewHotel(hotel);

        return new RedirectView("/manage_hotels");
    }


}
