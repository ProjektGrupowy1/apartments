package com.booking.apartments.controller;

import com.booking.apartments.ApartmentsApplication;
import com.booking.apartments.utility.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApartmentsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ManageTheHotelControllerTest {

    @Autowired
    Session session;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void init() {
        session.addParam("email", "owner@wp.pl");
        session.addParam("profile", "Owner");
    }

    @Test
    @WithMockUser(value = "owner@wp.pl", roles = "Owner", username = "owner@wp.pl")
    public void addNewHotelTest() throws Exception {

        MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build().perform(post("/add_hotel")
                .param("name", "hotel2")
                .param("description", "hotel2")
                .param("city", "Warszawa")
                .param("state", "Polska")
                .param("postalCode", "00-300")
                .param("street", "ulica")
                .param("rating", "4")
                .param("enabled", "true"))
                .andExpect(redirectedUrl("/manage_hotels"));
    }

    @Test
    @WithMockUser(value = "owner@wp.pl", roles = "Owner", username = "owner@wp.pl")
    public void modifyTheHotelTest() throws Exception {

        MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build().perform(post("/hotel_modification")
                .param("idHotel", "1")
                .param("name", "hotel")
                .param("description", "hotel")
                .param("city", "Warszawa")
                .param("state", "Polska")
                .param("postalCode", "00-300")
                .param("street", "bystra")
                .param("rating", "4")
                .param("enabled", "true"))
                .andExpect(redirectedUrl("/manage_hotels"));
    }

    @Test
    @WithMockUser(value = "owner@wp.pl", roles = "Owner", username = "owner@wp.pl")
    public void addNewApartmentTest() throws Exception {

        MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build().perform(post("/add_apartment")
                .param("hotelName", "hotel")
                .param("name", "apartament")
                .param("size", "90")
                .param("price", "200.0")
                .param("status", "Available"))
                .andExpect(redirectedUrl("/details_of_the_hotel?hotel_name=hotel"));
    }

    @Test
    @WithMockUser(value = "owner@wp.pl", roles = "Owner", username = "owner@wp.pl")
    public void modifyTheApartmentTest() throws Exception {

        MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build().perform(post("/apartment_modification")
                .param("idApartment", "1")
                .param("hotelName", "hotel")
                .param("name", "apartament")
                .param("size", "120")
                .param("price", "250.0")
                .param("status", "Available"))
                .andExpect(redirectedUrl("/details_of_the_hotel?hotel_name=hotel"));
    }

    @Test
    @WithMockUser(value = "owner@wp.pl", roles = "Owner", username = "owner@wp.pl")
    public void apartmentRemovalTest() throws Exception {

        MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build().perform(get("/remove_apartment/{hotel_name}/{id_apartment}", "hotel", "1"))
                .andExpect(redirectedUrl("/details_of_the_hotel?hotel_name=hotel"));
    }

    @Test
    @WithMockUser(value = "owner@wp.pl", roles = "Owner", username = "owner@wp.pl")
    public void showManageHotelsPageTest() throws Exception {

        session.addParam("email", "owner@wp.pl");
        session.addParam("profile", "Owner");

        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build()
                .perform(get("/manage_hotels"))
                .andExpect(view().name("/owner/manage_hotels"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin@wp.pl", roles = "Owner", username = "admin@wp.pl")
    public void showManageHotelsForAdminPageTest() throws Exception {

        session.addParam("email", "admin@wp.pl");
        session.addParam("profile", "Admin");

        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build()
                .perform(get("/manage_hotels_admin"))
                .andExpect(view().name("/admin/manage_hotels_admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "owner@wp.pl", roles = "Owner", username = "owner@wp.pl")
    public void showDetailsOfTheHotelPageTest() throws Exception {

        session.addParam("email", "owner@wp.pl");
        session.addParam("profile", "Owner");

        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build()
                .perform(get("/details_of_the_hotel")
                        .param("hotel_name", "hotel"))
                .andExpect(view().name("/owner/details_of_the_hotel"))
                .andExpect(status().isOk());
    }

}