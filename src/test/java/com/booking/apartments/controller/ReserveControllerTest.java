package com.booking.apartments.controller;

import com.booking.apartments.ApartmentsApplication;
import com.booking.apartments.utility.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApartmentsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")

public class ReserveControllerTest {

    @Autowired
    Session session;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @WithMockUser(value = "user@wp.pl", roles = "Client", username = "user@wp.pl")
    public void reserveApartmentTest() throws Exception {

        session.addParam("email", "user@wp.pl");
        session.addParam("profile", "Client");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/request_for_rent")
                .param("id_apartment", "2")
                .param("startDate", "2019-05-10")
                .param("endDate", "2019-05-30"))
                .andExpect(redirectedUrl("/user_reservations?added_reservation=true"));
    }

    @Test
    @WithMockUser(value = "owner@wp.pl", roles = "Owner", username = "owner@wp.pl")
    public void acceptReservationTest() throws Exception {

        session.addParam("email", "owner@wp.pl");
        session.addParam("profile", "Owner");

        mockMvc.perform(get("/accept_reservations/{id_reservation}", "2"))
                .andExpect(redirectedUrl("/reserved_apartments"));

    }

    @Test
    @WithMockUser(value = "owner@wp.pl", roles = "Owner", username = "owner@wp.pl")
    public void cancelReservationTest() throws Exception {

        session.addParam("email", "owner@wp.pl");
        session.addParam("profile", "Owner");

        mockMvc.perform(get("/cancel_rezerwation/{id_reservation}", "2"))
                .andExpect(redirectedUrl("/reserved_apartments"));

    }

    @Test
    @WithMockUser(value = "user@wp.pl", roles = "Client", username = "user@wp.pl")
    public void showUserReservationPageTest() throws Exception {

        session.addParam("email", "user@wp.pl");
        session.addParam("profile", "Client");

        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build()
                .perform(get("/user_reservations")
                        .param("added_reservation", "true"))
                .andExpect(view().name("/client/user_reservations"))
                .andExpect(model().attribute("added_reservation", true))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "owner@wp.pl", roles = "Owner", username = "owner@wp.pl")
    public void showReservedApartmentsPageTest() throws Exception {

        session.addParam("email", "owner@wp.pl");
        session.addParam("profile", "Owner");

        MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build()
                .perform(get("/reserved_apartments"))
                .andExpect(view().name("/owner/reserved_apartments"))
                .andExpect(status().isOk());
    }
}
