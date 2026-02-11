package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.model.UserFrontAddress;
import com.vasyerp.rolebasedsystem.model.UserRole;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserFrontRepositoryImpl implements UserFrontRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserFrontRepositoryImpl(JdbcTemplate jdbcTemplate) {
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

    private final RowMapper<UserFront> rowMapper = (rs, rowNum) -> {
        UserFront user = new UserFront();
        user.setUserFrontId(rs.getLong("user_front_id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        Long parentCompanyId = rs.getObject("parent_company_id", Long.class);
        if (parentCompanyId != null) {
            UserFront parent = new UserFront();
            parent.setUserFrontId(parentCompanyId);
            user.setParentCompany(parent);
        }

        user.setGstNo(rs.getString("gst_no"));
        user.setPhoneNo(rs.getString("phone_no"));

        return user;
    };

    private void fetchRoles(UserFront user) {
        if (user == null || user.getUserFrontId() == null)
            return;
        String sql = "SELECT r.role_id, r.role_name FROM user_role r JOIN user_role_new urn ON r.role_id = urn.role_id WHERE urn.user_front_id = ?";
        List<UserRole> roles = jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserRole r = new UserRole();
            r.setRoleId(rs.getLong("role_id"));
            r.setRoleName(rs.getString("role_name"));
            return r;
        }, user.getUserFrontId());
        user.setRoles(new HashSet<>(roles));
    }

    private void fetchAddresses(UserFront user) {
        if (user == null || user.getUserFrontId() == null)
            return;
        String sql = "SELECT * FROM user_front_address WHERE user_front_id = ?";
        List<UserFrontAddress> addresses = jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserFrontAddress address = new UserFrontAddress();
            address.setUserFrontAddressId(rs.getLong("user_front_address_id"));
            address.setAddressType(rs.getString("address_type"));
            address.setName(rs.getString("name"));
            address.setAddressLine1(rs.getString("address_line_1"));
            address.setAddressLine2(rs.getString("address_line_2"));
            address.setCity(rs.getString("city"));
            address.setState(rs.getString("state"));
            address.setCountry(rs.getString("country"));
            return address;
        }, user.getUserFrontId());
        user.setAddresses(addresses);
    }

    @Override
    public UserFront save(UserFront userFront) {
        if (userFront.getUserFrontId() == null) {
            String sql = "INSERT INTO user_front (name, password, parent_company_id, gst_no, phone_no) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] { "user_front_id" });
                ps.setString(1, userFront.getName());
                ps.setString(2, userFront.getPassword());
                ps.setObject(3,
                        userFront.getParentCompany() != null ? userFront.getParentCompany().getUserFrontId() : null);
                ps.setString(4, userFront.getGstNo());
                ps.setString(5, userFront.getPhoneNo());
                return ps;
            }, keyHolder);
            Long key = extractGeneratedId(keyHolder, "user_front_id");
            if (key != null) {
                userFront.setUserFrontId(key);
            }
        } else {
            String sql = "UPDATE user_front SET name = ?, password = ?, parent_company_id = ?, gst_no = ?, phone_no = ? WHERE user_front_id = ?";
            jdbcTemplate.update(sql,
                    userFront.getName(),
                    userFront.getPassword(),
                    userFront.getParentCompany() != null ? userFront.getParentCompany().getUserFrontId() : null,
                    userFront.getGstNo(),
                    userFront.getPhoneNo(),
                    userFront.getUserFrontId());
        }
        return userFront;
    }

    @Override
    public Optional<UserFront> findById(Long id) {
        String sql = "SELECT * FROM user_front WHERE user_front_id = ?";
        try {
            UserFront user = jdbcTemplate.queryForObject(sql, rowMapper, id);
            fetchRoles(user);
            fetchAddresses(user);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserFront> findAll() {
        String sql = "SELECT * FROM user_front";
        List<UserFront> users = jdbcTemplate.query(sql, rowMapper);
        users.forEach(user -> {
            fetchRoles(user);
            fetchAddresses(user);
        });
        return users;
    }

    @Override
    public Optional<UserFront> findByName(String name) {
        String sql = "SELECT * FROM user_front WHERE name = ?";
        try {
            UserFront user = jdbcTemplate.queryForObject(sql, rowMapper, name);
            fetchRoles(user);
            fetchAddresses(user);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserFront> findByParentCompanyIsNull() {
        String sql = "SELECT * FROM user_front WHERE parent_company_id IS NULL";
        List<UserFront> users = jdbcTemplate.query(sql, rowMapper);
        users.forEach(user -> {
            fetchRoles(user);
            fetchAddresses(user);
        });
        return users;
    }

    @Override
    public List<UserFront> findByParentCompany(UserFront parentCompany) {
        if (parentCompany == null || parentCompany.getUserFrontId() == null)
            return List.of();
        String sql = "SELECT * FROM user_front WHERE parent_company_id = ?";
        List<UserFront> users = jdbcTemplate.query(sql, rowMapper, parentCompany.getUserFrontId());
        users.forEach(user -> {
            fetchRoles(user);
            fetchAddresses(user);
        });
        return users;
    }

    @Override
    public boolean existsByParentCompany(UserFront parentCompany) {
        if (parentCompany == null || parentCompany.getUserFrontId() == null)
            return false;
        String sql = "SELECT COUNT(*) FROM user_front WHERE parent_company_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, parentCompany.getUserFrontId());
        return count != null && count > 0;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM user_front WHERE user_front_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM user_front WHERE user_front_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
