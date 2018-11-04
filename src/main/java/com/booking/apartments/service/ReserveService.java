package com.booking.apartments.service;

import com.booking.apartments.entity.ReservationEntity;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.repository.ReservationRepository;
import com.booking.apartments.utility.Session;
import com.booking.apartments.utility.enums.Status;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReserveService {

    Mapper mapper;

    Session session;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ManageTheHotelService manageTheHotelService;

    ReservationRepository reservationRepository;

    public boolean addNewReservation(Integer idApartment, LocalDate startDate, LocalDate endDate) {
        boolean condition = false;

        if(reservationRepository.findAllIdApartmentFromAGivenDateRangeAndApartmentId(startDate,endDate,idApartment).isEmpty()){
            ReservationEntity reservation = new ReservationEntity();
            reservation.setIdApartment(idApartment);
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setIdUser(authenticationService.getUserId(session.getParam("email").toString()));
            reservation.setPrice(manageTheHotelService.getApartment(idApartment).getPrice());
            reservation.setStatus(Status.Waiting.toString());
            reservationRepository.save(reservation);
            condition = true;
        }
        return condition;
    }


    public List<Mapper.BookingInformation> findAllReservation() {

        List<ReservationEntity> reservations = reservationRepository.findAllReservationByUserId(authenticationService.getUserId(session.getParam("email").toString()));

        return reservations.stream().map(mapper.bookingInformation).collect(Collectors.toList());

    }

    public void cancelReservation(Integer idReserwation) {
        ReservationEntity reservation = (ReservationEntity) reservationRepository.findById(idReserwation).get();
        reservation.setStatus(Status.Suspended.toString());
        reservationRepository.save(reservation);
    }
}
