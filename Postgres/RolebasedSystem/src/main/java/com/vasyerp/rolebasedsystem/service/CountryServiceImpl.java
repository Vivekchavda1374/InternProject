package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.model.Country;
import com.vasyerp.rolebasedsystem.repository.CountryRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    @Cacheable(value = "countryList", key = "'all'")
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    @CacheEvict(value = "countryList", allEntries = true)
    public Country getOrCreateCountry(String countryName) {

        if (countryName == null || countryName.trim().isEmpty()) {
            return null;
        }

        String trimmedName = countryName.trim();

        return countryRepository.findByName(trimmedName)
                .orElseGet(() -> {
                    Country newCountry = new Country();
                    newCountry.setName(trimmedName);
                    return countryRepository.save(newCountry);
                });
    }
}
