package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserRole;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRoleRepositoryImpl implements UserRoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRoleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserRole> rowMapper = (rs, rowNum) -> {
        UserRole role = new UserRole();
        role.setRoleId(rs.getLong("role_id"));
        role.setRoleName(rs.getString("role_name"));
        return role;
    };

    @Override
    public UserRole save(UserRole role) {
        if (role.getRoleId() == null) {
            String sql = "INSERT INTO user_role (role_name) VALUES (?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, role.getRoleName());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key != null) {
                role.setRoleId(key.longValue());
            }
        } else {
            String sql = "UPDATE user_role SET role_name = ? WHERE role_id = ?";
            jdbcTemplate.update(sql, role.getRoleName(), role.getRoleId());
        }
        return role;
    }

    @Override
    public Optional<UserRole> findById(Long id) {
        String sql = "SELECT * FROM user_role WHERE role_id = ?";
        try {
            UserRole role = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(role);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserRole> findByRoleName(String roleName) {
        String sql = "SELECT * FROM user_role WHERE role_name = ?";
        try {
            UserRole role = jdbcTemplate.queryForObject(sql, rowMapper, roleName);
            return Optional.ofNullable(role);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserRole> findAll() {
        String sql = "SELECT * FROM user_role";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM user_role WHERE role_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM user_role WHERE role_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
