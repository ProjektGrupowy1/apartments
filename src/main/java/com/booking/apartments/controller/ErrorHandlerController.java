package com.booking.apartments.controller;

import com.booking.apartments.utility.ApartmentException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(ApartmentException.class)
    public ModelAndView handleSQLException(HttpServletRequest request, ApartmentException ex) {
        ModelAndView errorModelAndView = new ModelAndView("error");

        String errorHeader = ex.getHeader();
        String errorMessage = ex.getMessage();

        if(errorHeader==null) errorHeader = "Błąd";
        if(errorMessage==null) errorMessage = "Usługa nieodpowiada";

        errorModelAndView.addObject("errorHeader", errorHeader);
        errorModelAndView.addObject("errorMessage", errorMessage);
        return errorModelAndView;
    }
}
