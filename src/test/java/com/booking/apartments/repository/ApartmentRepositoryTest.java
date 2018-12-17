package com.booking.apartments.repository;

import com.booking.apartments.entity.ApartmentEntity;
import com.booking.apartments.entity.HotelEntity;
import com.booking.apartments.entity.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ApartmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ApartmentRepository apartmentRepository;

    List<ApartmentEntity> apartments;

    HotelEntity hotel;

    @Before
    public void init(){

        // given
        UserEntity user = new UserEntity("name", "lastname", "email", "password", "phone", "street", 2, 1, 1);
        entityManager.persist(user);
        entityManager.flush();
        hotel = new HotelEntity("Nazwa hotelu", 4, "description", user.getIdUser(), 1, "street");
        entityManager.persist(hotel);
        entityManager.flush();
        ApartmentEntity apartment = new ApartmentEntity(hotel.getIdHotel(), "Nazwa apartamentu", 20, 200.0f, "Available");
        ApartmentEntity apartment2 = new ApartmentEntity(hotel.getIdHotel(), "Nazwa apartamentu 2", 40, 300.0f, "Available");
        ApartmentEntity apartment3 = new ApartmentEntity(hotel.getIdHotel(), "Nazwa apartamentu 3", 60, 400.0f, "Available");
        entityManager.persist(apartment);
        entityManager.persist(apartment2);
        entityManager.persist(apartment3);
        entityManager.flush();

        apartments = Arrays.asList(
                apartment, apartment2, apartment3
        );
    }

    @Test
    public void findApartmentsByHotelIdTest() {

        // when
        List<ApartmentEntity> found = apartmentRepository.findApartmentsByHotelId(hotel.getIdHotel());

        // then
        assertEquals(found, apartments);

    }

    @Test
    public void findApartmentsByApartmentIdTest() {

        // when
        ApartmentEntity found = apartmentRepository.findApartmentsByApartmentId(apartments.get(0).getIdApartment()).get(0);

        // then
        assertEquals(found, apartments.get(0));

    }

    @Test
    public void findAllAvailableTest() {

        // when
        List<ApartmentEntity> found = apartmentRepository.findAllAvailable();

        // then
        assertTrue(found.containsAll(apartments));

    }

    @Test
    public void saveApartmentTest() {

        //given
        ApartmentEntity apartment4 = new ApartmentEntity(hotel.getIdHotel(), "Nazwa apartamentu 4", 80, 500.0f, "Available");
        apartmentRepository.save(apartment4);
        // when

        ApartmentEntity found = apartmentRepository.findApartmentsByApartmentId(apartment4.getIdApartment()).get(0);

        // then
        assertEquals(found,apartment4);
    }
}
