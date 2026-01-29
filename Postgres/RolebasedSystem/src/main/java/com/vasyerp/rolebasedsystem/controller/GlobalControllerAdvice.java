package com.vasyerp.rolebasedsystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Value("${app.countries}")
    private String countriesString;

    @ModelAttribute("countries")
    public List<String> getCountries() {
        return Arrays.asList(countriesString.split(","));
    }
}
