package com.booking.apartments.repository;

import com.booking.apartments.entity.ApartmentEntity;
import com.booking.apartments.entity.HotelEntity;
import com.booking.apartments.entity.ReservationEntity;
import com.booking.apartments.entity.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    ApartmentEntity apartment;

    List<ReservationEntity> reservations;

    UserEntity owner, client, client2;

    @Before
    public void init(){
        // given
        owner = new UserEntity("Jan", "Kowalski", "jan.kowalski@wp.pl",
                "jan", "+48503654234", "ul. Waryńskiego 34", 2, 1, 1);
        entityManager.persist(owner);
        client = new UserEntity("Zbigniew", "Nowak", "zbigniew.nowak@wp.pl",
                "zbigniew", "+48503987234", "ul. Paderewskiego", 1, 1, 1);
        entityManager.persist(client);
        client2 = new UserEntity("Malwina", "Nowacka", "malwina.nowacka@wp.pl",
                "malwina", "+48950487235", "ul. Wojska Polskiego 98", 1, 1, 1);
        entityManager.persist(client2);
        entityManager.flush();
        HotelEntity hotel = new HotelEntity("Bellotto", 4, "", owner.getIdUser(), 1, "ul. Żelazna 39");
        entityManager.persist(hotel);
        entityManager.flush();
        apartment = new ApartmentEntity(hotel.getIdHotel(), "Apartament - dwa pokoje", 20, 200.0f, "Available");
        entityManager.persist(apartment);
        entityManager.flush();
        ReservationEntity reservation =
                new ReservationEntity(LocalDate.of(2018, Month.DECEMBER, 2),
                        LocalDate.of(2018, Month.DECEMBER, 10),200.0f,
                        apartment.getIdApartment(),client.getIdUser(),"Approved");
        ReservationEntity reservation2 =
                new ReservationEntity(LocalDate.of(2018, Month.DECEMBER, 12),
                        LocalDate.of(2018, Month.DECEMBER, 22),400.0f,
                        apartment.getIdApartment(),client.getIdUser(),"Approved");
        ReservationEntity reservation3 =
                new ReservationEntity(LocalDate.of(2018, Month.DECEMBER, 24),
                        LocalDate.of(2018, Month.DECEMBER, 30),500.0f,
                        apartment.getIdApartment(),client.getIdUser(),"Approved");
        entityManager.persist(reservation);
        entityManager.persist(reservation2);
        entityManager.persist(reservation3);
        entityManager.flush();

        reservations = Arrays.asList(
                reservation, reservation2, reservation3
        );
    }

    @Test
    public void findAllIdApartmentFromAGivenDateRangeAndApartmentIdTest(){

        // when
        List<ReservationEntity> found = reservationRepository.findAllIdApartmentFromAGivenDateRangeAndApartmentId(
                LocalDate.of(2018, Month.DECEMBER, 7), LocalDate.of(2018, Month.DECEMBER, 27),apartment.getIdApartment());

        // then
        assertEquals(found, reservations);
    }

    @Test
    public void findAllReservationByUserIdTest(){

        // given
        ReservationEntity reservation4 =
                new ReservationEntity(LocalDate.of(2018, Month.NOVEMBER, 24),
                        LocalDate.of(2018, Month.NOVEMBER, 30),150.0f,
                        apartment.getIdApartment(),client2.getIdUser(),"Approved");
        entityManager.persist(reservation4);
        entityManager.flush();

        // when
        List<ReservationEntity> found = reservationRepository.findAllReservationByUserId(client2.getIdUser(),"Approved");

        // then
        assertEquals(found.get(0),reservation4);

    }

    @Test
    public void findAllReservationByApartmentIdTest(){
        // when
        List<ReservationEntity> found = reservationRepository.findAllReservationByApartmentId(apartment.getIdApartment(),"Approved");

        // then
        assertEquals(found, reservations);
    }

    @Test
    public void saveReservationTest() {

        //given
        ReservationEntity newReservation =
                new ReservationEntity(LocalDate.of(2018, Month.NOVEMBER, 16),
                        LocalDate.of(2018, Month.NOVEMBER, 20),100.0f,
                        apartment.getIdApartment(),client2.getIdUser(),"Approved");
        // when
        reservationRepository.save(newReservation);

        List<ReservationEntity> found = reservationRepository.findAllReservationByUserId(client2.getIdUser(),"Approved");

        // then
        assertEquals(found.get(0),newReservation);
    }

    @Test
    public void removeReservationTest() {

        //given
        ReservationEntity addedReservation =
                new ReservationEntity(LocalDate.of(2018, Month.NOVEMBER, 1),
                        LocalDate.of(2018, Month.NOVEMBER, 9),160.0f,
                        apartment.getIdApartment(),client2.getIdUser(),"Waiting");
        reservationRepository.save(addedReservation);

        ReservationEntity existingReservation = reservationRepository.findAllReservationByUserId(client2.getIdUser(),"Waiting").get(0);
        assertThat(existingReservation).isEqualTo(addedReservation);

        // when
        reservationRepository.delete(addedReservation);

        List<ReservationEntity> found = reservationRepository.findAllReservationByUserId(client2.getIdUser(),"Waiting");

        // then
        assertTrue(found.isEmpty());
    }

    @Test
    public void cancelReservationTest(){

        //given
        ReservationEntity addedReservation =
                new ReservationEntity(LocalDate.of(2018, Month.NOVEMBER, 10),
                        LocalDate.of(2018, Month.NOVEMBER, 15),180.0f,
                        apartment.getIdApartment(),client2.getIdUser(),"Waiting");
        reservationRepository.save(addedReservation);

        ReservationEntity existingReservation = reservationRepository.findAllReservationByUserId(client2.getIdUser(),"Waiting").get(0);

        // when
        existingReservation.setIdReservation(existingReservation.getIdReservation());
        existingReservation.setIdApartment(existingReservation.getIdApartment());
        existingReservation.setIdUser(existingReservation.getIdUser());
        existingReservation.setPrice(existingReservation.getPrice());
        existingReservation.setStartDate(existingReservation.getStartDate());
        existingReservation.setEndDate(existingReservation.getEndDate());
        existingReservation.setStatus("Suspended");
        reservationRepository.save(existingReservation);

        List<ReservationEntity> found = reservationRepository.findAllReservationByUserId(client2.getIdUser(),"Suspended");

        // then
        assertEquals(found.get(0),existingReservation);
    }
}
