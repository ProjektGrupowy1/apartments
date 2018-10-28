package com.booking.apartments.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ManageAccountController {
    @RequestMapping(value = "/manage_account", method = RequestMethod.GET)
    public ModelAndView getManageAccountPage() {
        ModelAndView manageAccountModelAndView = new ModelAndView("/admin/manage_account");
        return manageAccountModelAndView;
    }
}
