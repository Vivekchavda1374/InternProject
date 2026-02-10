package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.model.Country;
import com.vasyerp.rolebasedsystem.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Country getOrCreateCountry(String countryName) {
        if (countryName == null || countryName.trim().isEmpty()) {
            return null;
        }

        return countryRepository.findByName(countryName.trim())
                .orElseGet(() -> {
                    Country newCountry = new Country();
                    newCountry.setName(countryName.trim());
                    return countryRepository.save(newCountry);
                });
    }
}
