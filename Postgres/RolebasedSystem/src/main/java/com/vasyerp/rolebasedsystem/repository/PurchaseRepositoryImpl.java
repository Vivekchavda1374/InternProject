package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Purchase;
import com.vasyerp.rolebasedsystem.model.UserFront;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class PurchaseRepositoryImpl implements PurchaseRepository {

    private final JdbcTemplate jdbcTemplate;

    public PurchaseRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Purchase> rowMapper = (rs, rowNum) -> {
        Purchase purchase = new Purchase();
        purchase.setPurchaseId(rs.getLong("purchase_id"));
        purchase.setContactId(rs.getObject("contact_id", Integer.class));

        Long companyId = rs.getObject("company_id", Long.class);
        if (companyId != null) {
            UserFront company = new UserFront();
            company.setUserFrontId(companyId);
            purchase.setCompany(company);
        }

        Long branchId = rs.getObject("branch_id", Long.class);
        if (branchId != null) {
            UserFront branch = new UserFront();
            branch.setUserFrontId(branchId);
            purchase.setBranch(branch);
        }

        purchase.setPrefix(rs.getString("prefix"));
        purchase.setPurchaseNo(rs.getString("purchase_no"));
        purchase.setTotalAmount(rs.getObject("total_amount", Double.class));

        Date date = rs.getDate("purchase_date");
        if (date != null) {
            purchase.setPurchaseDate(date.toLocalDate());
        }

        return purchase;
    };

    @Override
    public Purchase save(Purchase purchase) {
        if (purchase.getPurchaseId() == null) {
            String sql = "INSERT INTO purchase (contact_id, company_id, branch_id, prefix, purchase_no, total_amount, purchase_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, purchase.getContactId());
                ps.setLong(2, purchase.getCompany().getUserFrontId());
                ps.setLong(3, purchase.getBranch().getUserFrontId());
                ps.setString(4, purchase.getPrefix());
                ps.setString(5, purchase.getPurchaseNo());
                ps.setObject(6, purchase.getTotalAmount());
                ps.setDate(7, purchase.getPurchaseDate() != null ? Date.valueOf(purchase.getPurchaseDate()) : null);
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key != null) {
                purchase.setPurchaseId(key.longValue());
            }
        } else {
            String sql = "UPDATE purchase SET contact_id = ?, company_id = ?, branch_id = ?, prefix = ?, purchase_no = ?, total_amount = ?, purchase_date = ? WHERE purchase_id = ?";
            jdbcTemplate.update(sql,
                    purchase.getContactId(),
                    purchase.getCompany().getUserFrontId(),
                    purchase.getBranch().getUserFrontId(),
                    purchase.getPrefix(),
                    purchase.getPurchaseNo(),
                    purchase.getTotalAmount(),
                    purchase.getPurchaseDate() != null ? Date.valueOf(purchase.getPurchaseDate()) : null,
                    purchase.getPurchaseId());
        }
        return purchase;
    }

    @Override
    public Optional<Purchase> findById(Long id) {
        String sql = "SELECT * FROM purchase WHERE purchase_id = ?";
        try {
            Purchase purchase = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(purchase);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Purchase> findAll() {
        String sql = "SELECT * FROM purchase";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Purchase> findByBranch_UserFrontId(Long branchId) {
        String sql = "SELECT * FROM purchase WHERE branch_id = ?";
        return jdbcTemplate.query(sql, rowMapper, branchId);
    }

    @Override
    public List<Purchase> findByCompany_UserFrontId(Long companyId) {
        String sql = "SELECT * FROM purchase WHERE company_id = ?";
        return jdbcTemplate.query(sql, rowMapper, companyId);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM purchase WHERE purchase_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
