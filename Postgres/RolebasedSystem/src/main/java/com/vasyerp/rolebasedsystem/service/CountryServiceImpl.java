package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.model.Country;
import com.vasyerp.rolebasedsystem.repository.CountryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private static final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);
    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    @Cacheable("countries")
    public List<Country> getAllCountries() {
        log.debug("Cache MISS for 'countries'; loading all countries from database");
        return countryRepository.findAll();
    }

    @Override
    @CacheEvict(value = "countries", allEntries = true)
    public Country getOrCreateCountry(String countryName) {
        if (countryName == null || countryName.trim().isEmpty()) {
            return null;
        }

        log.debug("Evicting 'countries' cache due to create/get request for country: {}", countryName.trim());
        return countryRepository.findByName(countryName.trim())
                .orElseGet(() -> {
                    Country newCountry = new Country();
                    newCountry.setName(countryName.trim());
                    return countryRepository.save(newCountry);
                });
    }
}
