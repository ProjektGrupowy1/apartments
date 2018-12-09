package com.booking.apartments.controller;

import com.booking.apartments.ApartmentsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApartmentsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void signInTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration")
                .param("name", "name")
                .param("lastname", "lastname")
                .param("street", "street")
                .param("city", "city")
                .param("state", "state")
                .param("postalCode", "postalCode")
                .param("phone", "phone")
                .param("email", "email")
                .param("password", "password")
                .param("profile", "Client")
                .param("enabled", "true"))
                .andExpect(redirectedUrl("/search_engine"));
    }

    @Test(expected = Exception.class)
    public void attemptToSaveUserWhoExistsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/registration")
                .param("name", "user")
                .param("lastname", "user")
                .param("street", "user")
                .param("city", "user")
                .param("state", "user")
                .param("postalCode", "user")
                .param("phone", "user")
                .param("email", "user@wp.pl")
                .param("password", "")
                .param("profile", "Client")
                .param("enabled", "true"));
    }


    @Test
    public void loginTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/screenname", "user@wp.pl", "$2a$10$50IcwAXaRKuVAnSbvlqPtecWTAfHaPLVVNXJrz0G.BrYPTFTtt5ru"));
    }

}
