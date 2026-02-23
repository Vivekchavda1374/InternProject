package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query(value = """
            SELECT *
            FROM country
            WHERE LOWER(name) = LOWER(:name)
            LIMIT 1
            """, nativeQuery = true)
    Optional<Country> findByName(@Param("name") String name);
}
