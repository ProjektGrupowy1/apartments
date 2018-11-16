package com.booking.apartments.controller;

import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.service.ManageUsersService;
import com.booking.apartments.utility.ApartmentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@SessionAttributes({"email", "profile"})
public class ManageUsersController {

    @Autowired
    private ManageUsersService manageUsersService;

    @Autowired
    private Mapper mapper;

    @RequestMapping(value = "/manage_users", method = RequestMethod.GET)
    public ModelAndView showManageUsersPage() {
        ModelAndView manageUsersModelAndView = new ModelAndView(("/admin/manage_users"));
        List<Mapper.UserMapper> users = manageUsersService.getAllUsers().stream().map(mapper.mapUser).collect(Collectors.toList());

        manageUsersModelAndView.addObject("users", users);



        return manageUsersModelAndView;
    }
    @RequestMapping(value = "/remove_user/{id_user}", method = RequestMethod.GET)
    public RedirectView hotelRemoval(@PathVariable("id_user") Integer idUser) {
        manageUsersService.deleteUser(idUser);
        return new RedirectView("/manage_users");
    }

    @RequestMapping(value = "/user_modification", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView modifyUser(@ModelAttribute("modified_user") Mapper.UserMapper userMapper) throws ApartmentException {

        System.out.println("Enable = "+userMapper.isEnabled());

        manageUsersService.modifyUser(userMapper);

        return new RedirectView("/manage_users");
    }

    @RequestMapping(value = "/add_user", method = RequestMethod.POST)
    public @ResponseBody
    RedirectView addHotel(@ModelAttribute("new_user") Mapper.NewUserMapper newUserMapper) throws ApartmentException {

        manageUsersService.addNewUser(newUserMapper);

        return new RedirectView("/manage_users");
    }

}
