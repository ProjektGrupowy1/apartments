package com.booking.apartments.service;

import com.booking.apartments.entity.ApartmentEntity;
import com.booking.apartments.entity.HotelEntity;
import com.booking.apartments.entity.ReservationEntity;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.repository.ReservationRepository;
import com.booking.apartments.utility.Session;
import com.booking.apartments.utility.enums.Status;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


import static java.time.temporal.ChronoUnit.DAYS;

@Service
@AllArgsConstructor
public class ReserveService {

    private Mapper mapper;

    private Session session;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ManageTheHotelService manageTheHotelService;

    private ReservationRepository reservationRepository;

    public boolean addNewReservation(Integer idApartment, LocalDate startDate, LocalDate endDate) {
        boolean condition = false;

        if (reservationRepository.findAllIdApartmentFromAGivenDateRangeAndApartmentId(startDate, endDate, idApartment).isEmpty()) {
            ReservationEntity reservation = new ReservationEntity();
            reservation.setIdApartment(idApartment);
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setIdUser(authenticationService.getUserId(session.getParam("email").toString()));
            reservation.setPrice((manageTheHotelService.getApartment(idApartment).getPrice()) * DAYS.between(startDate, endDate));
            reservation.setStatus(Status.Waiting.toString());
            reservationRepository.save(reservation);
            condition = true;
        }
        return condition;
    }

    public List<Mapper.BookingInformation> findAllReservation(Set<Status> status) {

        Status element;
        List<ReservationEntity> reservations = new ArrayList<>();
        Iterator<Status> iterator = status.iterator();
        while(iterator.hasNext()) {
            element = iterator.next();
            reservations.addAll(reservationRepository.findAllReservationByUserId(authenticationService.getUserId(session.getParam("email").toString()),element.toString()));
        }
        return reservations.stream().map(mapper.bookingInformation).collect(Collectors.toList());

    }

    public List<Mapper.InformationForTheOwner> findAllOwnersReservations(Set<Status> status) {

        List<ReservationEntity> reservations = new ArrayList<>();
        List<ApartmentEntity> apartments = new ArrayList<>();
        List<HotelEntity> hotels = manageTheHotelService.getHotels(authenticationService.getUserId(session.getParam("email").toString()));

        hotels.forEach(h->{apartments.addAll(manageTheHotelService.getApartments(h.getIdHotel()));});

        Iterator<Status> iterator = status.iterator();
        while(iterator.hasNext()) {
            Status element = iterator.next();
            apartments.forEach(a->{reservations.addAll(reservationRepository.findAllReservationByApartmentId(a.getIdApartment(),element.toString()));});
        }

        return reservations.stream().map(mapper.informationForTheOwner).collect(Collectors.toList());
    }

    public void cancelReservation(Integer idReservation) {
        ReservationEntity reservation = (ReservationEntity) reservationRepository.findById(idReservation).get();
        reservation.setStatus(Status.Suspended.toString());
        reservationRepository.save(reservation);
    }
    public void acceptReservation(Integer idReservation) {
        ReservationEntity reservation = (ReservationEntity) reservationRepository.findById(idReservation).get();
        reservation.setStatus(Status.Approved.toString());
        reservationRepository.save(reservation);
    }
}
