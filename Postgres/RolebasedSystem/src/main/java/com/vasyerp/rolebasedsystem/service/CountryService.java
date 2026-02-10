package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.model.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAllCountries();

    Country getOrCreateCountry(String countryName);
}
