package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.UserRoleNew;
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
public class UserRoleNewRepositoryImpl implements UserRoleNewRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRoleNewRepositoryImpl(JdbcTemplate jdbcTemplate) {
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

    private final RowMapper<UserRoleNew> rowMapper = (rs, rowNum) -> {
        UserRoleNew roleNew = new UserRoleNew();
        roleNew.setUserRoleNewId(rs.getLong("user_role_new_id"));
        roleNew.setRoleId(rs.getLong("role_id"));
        roleNew.setUserFrontId(rs.getLong("user_front_id"));
        return roleNew;
    };

    @Override
    public UserRoleNew save(UserRoleNew userRoleNew) {
        if (userRoleNew.getUserRoleNewId() == null) {
            String sql = "INSERT INTO user_role_new (role_id, user_front_id) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] { "user_role_new_id" });
                ps.setLong(1, userRoleNew.getRoleId());
                ps.setLong(2, userRoleNew.getUserFrontId());
                return ps;
            }, keyHolder);
            Long key = extractGeneratedId(keyHolder, "user_role_new_id");
            if (key != null) {
                userRoleNew.setUserRoleNewId(key);
            }
        } else {
            String sql = "UPDATE user_role_new SET role_id = ?, user_front_id = ? WHERE user_role_new_id = ?";
            jdbcTemplate.update(sql,
                    userRoleNew.getRoleId(),
                    userRoleNew.getUserFrontId(),
                    userRoleNew.getUserRoleNewId());
        }
        return userRoleNew;
    }

    @Override
    public Optional<UserRoleNew> findById(Long id) {
        String sql = "SELECT * FROM user_role_new WHERE user_role_new_id = ?";
        try {
            UserRoleNew roleNew = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(roleNew);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM user_role_new WHERE user_role_new_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<UserRoleNew> findByUserFrontIdAndRoleId(Long userFrontId, Long roleId) {
        String sql = "SELECT * FROM user_role_new WHERE user_front_id = ? AND role_id = ?";
        try {
            UserRoleNew roleNew = jdbcTemplate.queryForObject(sql, rowMapper, userFrontId, roleId);
            return Optional.ofNullable(roleNew);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserRoleNew> findByUserFrontId(Long userFrontId) {
        String sql = "SELECT * FROM user_role_new WHERE user_front_id = ?";
        return jdbcTemplate.query(sql, rowMapper, userFrontId);
    }
}
