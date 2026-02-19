package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.model.UserFrontAddress;
import com.vasyerp.rolebasedsystem.model.Country;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserFrontAddressRepositoryImpl implements UserFrontAddressRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserFrontAddressRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Long extractGeneratedId(KeyHolder keyHolder, String keyName) {
        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && !keys.isEmpty()) {
            Object value = keys.get(keyName);
            if (value == null) {
                value = keys.values().iterator().next();
            }
            if (value instanceof Number n) {
                return n.longValue();
            }
        }
        Number singleKey = keyHolder.getKey();
        return singleKey != null ? singleKey.longValue() : null;
    }

    private final RowMapper<UserFrontAddress> rowMapper = (rs, rowNum) -> {
        UserFrontAddress address = new UserFrontAddress();
        address.setUserFrontAddressId(rs.getLong("user_front_address_id"));

        Long userFrontId = rs.getObject("user_front_id", Long.class);
        if (userFrontId != null) {
            UserFront userFront = new UserFront();
            userFront.setUserFrontId(userFrontId);
            address.setUserFront(userFront);
        }

        address.setAddressType(rs.getString("address_type"));
        address.setName(rs.getString("name"));
        address.setAddressLine1(rs.getString("address_line_1"));
        address.setAddressLine2(rs.getString("address_line_2"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        String countryName = rs.getString("country_name");
        if (countryName != null) {
            Country country = new Country();
            country.setName(countryName);
            address.setCountryRef(country);
        }
        return address;
    };

    @Override
    public UserFrontAddress save(UserFrontAddress address) {
        if (address.getUserFrontAddressId() == null) {
            String sql = """
                    INSERT INTO user_front_address (
                        user_front_id, address_type, name, address_line_1, address_line_2, city, state, country_id
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT country_id FROM country WHERE name = ?))
                    """;
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] { "user_front_address_id" });
                ps.setLong(1, address.getUserFront().getUserFrontId());
                ps.setString(2, address.getAddressType());
                ps.setString(3, address.getName());
                ps.setString(4, address.getAddressLine1());
                ps.setString(5, address.getAddressLine2());
                ps.setString(6, address.getCity());
                ps.setString(7, address.getState());
                ps.setString(8, address.getCountry());
                return ps;
            }, keyHolder);
            Long key = extractGeneratedId(keyHolder, "user_front_address_id");
            if (key != null) {
                address.setUserFrontAddressId(key);
            }
        } else {
            String sql = """
                    UPDATE user_front_address
                    SET user_front_id = ?, address_type = ?, name = ?, address_line_1 = ?, address_line_2 = ?,
                        city = ?, state = ?, country_id = (SELECT country_id FROM country WHERE name = ?)
                    WHERE user_front_address_id = ?
                    """;
            jdbcTemplate.update(sql,
                    address.getUserFront().getUserFrontId(),
                    address.getAddressType(),
                    address.getName(),
                    address.getAddressLine1(),
                    address.getAddressLine2(),
                    address.getCity(),
                    address.getState(),
                    address.getCountry(),
                    address.getUserFrontAddressId());
        }
        return address;
    }

    @Override
    public Optional<UserFrontAddress> findById(Long id) {
        String sql = """
                SELECT ufa.*, c.name AS country_name
                FROM user_front_address ufa
                LEFT JOIN country c ON c.country_id = ufa.country_id
                WHERE ufa.user_front_address_id = ?
                """;
        try {
            UserFrontAddress address = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(address);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserFrontAddress> findAll() {
        String sql = """
                SELECT ufa.*, c.name AS country_name
                FROM user_front_address ufa
                LEFT JOIN country c ON c.country_id = ufa.country_id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<UserFrontAddress> findByUserFront_UserFrontId(Long userFrontId) {
        String sql = """
                SELECT ufa.*, c.name AS country_name
                FROM user_front_address ufa
                LEFT JOIN country c ON c.country_id = ufa.country_id
                WHERE ufa.user_front_id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, userFrontId);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM user_front_address WHERE user_front_address_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
