package com.booking.apartments.controller;

import com.booking.apartments.entity.ApartmentEntity;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
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

    @Getter
    @AllArgsConstructor
    public class NewHotelMapper {
        private String name;
        private String description;
        private String city;
        private String street;
        private int rating;
    }

    @Getter
    @AllArgsConstructor
    public class HotelMapper {
        private int idHotel;
        private String name;
        private String description;
        private String city;
        private String street;
        private int rating;
    }

    @Getter
    @AllArgsConstructor
    public class NewApartmentMapper {
        private String hotelName;
        private String name;
        private int size;
        private float price;
        private int status;
    }

    @Getter
    @AllArgsConstructor
    public class ApartmentMapper {
        private int idApartment;
        private String hotelName;
        private String name;
        private int size;
        private float price;
        private int status;
    }

    Function<HotelEntity, HotelMapper> mapTheHotel = new Function<HotelEntity, HotelMapper>() {
        public HotelMapper apply(HotelEntity hotelEntity) {
            return new HotelMapper(hotelEntity.getIdHotel(), hotelEntity.getName(), hotelEntity.getDescription(), manageTheHotelService.getCityName(hotelEntity.getIdCity()),
                    hotelEntity.getStreet(), hotelEntity.getRating());
        }
    };

    Function<ApartmentEntity, ApartmentMapper> mapTheApartment = new Function<ApartmentEntity, ApartmentMapper>() {
        public ApartmentMapper apply(ApartmentEntity apartmentEntity) {
            return new ApartmentMapper(apartmentEntity.getIdApartment(), manageTheHotelService.getHotelName(apartmentEntity.getIdHotel()),
                    apartmentEntity.getName(), apartmentEntity.getSize(), apartmentEntity.getPrice(), apartmentEntity.getStatus());
        }
    };

    @RequestMapping(value = "/manage_hotels", method = RequestMethod.GET)
    public ModelAndView getManageHotelsPage() {
        ModelAndView manageHotelsModelAndView = new ModelAndView("/owner/manage_hotels");

        int idOwner = authenticationService.getUserId(session.getParam("email").toString());

        List<HotelMapper> hotels = manageTheHotelService.getHotels(idOwner).stream().map(mapTheHotel).collect(Collectors.toList());
        manageHotelsModelAndView.addObject("hotels", hotels);

        return manageHotelsModelAndView;
    }

    @RequestMapping(value = "/details_of_the_hotel", method = RequestMethod.GET)
    public ModelAndView getDetailsOfTheHotelPage(@RequestParam(value = "id_hotel", required = false) Integer idHotel,
                                                 @RequestParam(value = "hotel_name", required = false) String hotelName) {
        ModelAndView detailsOfTheHotelModelAndView = new ModelAndView("/owner/details_of_the_hotel");

        System.out.println("hotelName = "+hotelName);

        List<ApartmentMapper> apartments = null;
        if (idHotel!=null) {
            apartments = manageTheHotelService.getApartments(idHotel).stream().map(mapTheApartment).collect(Collectors.toList());
            detailsOfTheHotelModelAndView.addObject("hotel_name", manageTheHotelService.getHotelName(idHotel));
        } else if (!hotelName.isEmpty()) {
            apartments = manageTheHotelService.getApartments(hotelName).stream().map(mapTheApartment).collect(Collectors.toList());
            detailsOfTheHotelModelAndView.addObject("hotel_name", hotelName);
        }
        else {
            apartments = new ArrayList<>();
        }

        detailsOfTheHotelModelAndView.addObject("apartments", apartments);
        return detailsOfTheHotelModelAndView;
    }

    @RequestMapping(value = "/add_hotel", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView addHotel(@ModelAttribute("new_hotel") NewHotelMapper newHotelMapper) throws ApartmentException {

        HotelEntity hotel = new HotelEntity();
        hotel.setIdCity(authenticationService.getIdByCityName(newHotelMapper.getCity()));
        hotel.setIdOwner(authenticationService.getUserId(session.getParam("email").toString()));
        hotel.setDescription(newHotelMapper.getDescription());
        hotel.setRating(newHotelMapper.getRating());
        hotel.setStreet(newHotelMapper.getStreet());
        hotel.setName(newHotelMapper.getName());

        manageTheHotelService.addNewHotel(hotel);

        return new RedirectView("/manage_hotels");
    }

    @RequestMapping(value = "/hotel_modification", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView modifyTheHotel(@ModelAttribute("modified_hotel") HotelMapper hotelMapper) throws ApartmentException {

        HotelEntity hotel = manageTheHotelService.getHotel(hotelMapper.getIdHotel());
        hotel.setIdCity(authenticationService.getIdByCityName(hotelMapper.getCity()));
        hotel.setIdOwner(authenticationService.getUserId(session.getParam("email").toString()));
        hotel.setDescription(hotelMapper.getDescription());
        hotel.setRating(hotelMapper.getRating());
        hotel.setStreet(hotelMapper.getStreet());
        hotel.setName(hotelMapper.getName());

        manageTheHotelService.modyfyTheHotel(hotel);

        return new RedirectView("/manage_hotels");
    }

    @RequestMapping(value = "/remove_hotel/{id_hotel}", method = RequestMethod.GET)
    public RedirectView hotelRemoval(@PathVariable("id_hotel") int idHotel) {
        manageTheHotelService.deleteHotel(idHotel);
        return new RedirectView("/manage_hotels");
    }

    @RequestMapping(value = "/add_apartment", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView addApartment(@ModelAttribute("new_apartment") NewApartmentMapper newApartmentMapper) throws ApartmentException {

        RedirectView detailOfTheHotelRedirectView = new RedirectView("/details_of_the_hotel");
        ApartmentEntity apartment = new ApartmentEntity();

        apartment.setIdHotel(manageTheHotelService.getHotelId(newApartmentMapper.getHotelName()));
        apartment.setName(newApartmentMapper.getName());
        apartment.setPrice(newApartmentMapper.getPrice());
        apartment.setSize(newApartmentMapper.getSize());
        apartment.setStatus(newApartmentMapper.getStatus());

        manageTheHotelService.addApartment(apartment);

        detailOfTheHotelRedirectView.addStaticAttribute("hotel_name", newApartmentMapper.getHotelName());

        return detailOfTheHotelRedirectView;
    }

    @RequestMapping(value = "/apartment_modification", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView modifyTheHotel(@ModelAttribute("modified_apartment") ApartmentMapper apartmentMapper,
                                RedirectAttributes redirectAttributes) throws ApartmentException {
        ApartmentEntity apartment = manageTheHotelService.getApartment(apartmentMapper.getIdApartment());
        apartment.setSize(apartmentMapper.getSize());
        apartment.setStatus(apartmentMapper.getStatus());
        apartment.setPrice(apartmentMapper.getPrice());
        apartment.setName(apartmentMapper.getName());
        apartment.setIdHotel(manageTheHotelService.getHotelId(apartmentMapper.getHotelName()));

        manageTheHotelService.modyfyTheApartment(apartment);

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
