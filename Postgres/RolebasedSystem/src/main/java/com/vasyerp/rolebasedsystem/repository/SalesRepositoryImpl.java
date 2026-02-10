package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Sales;
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
public class SalesRepositoryImpl implements SalesRepository {

    private final JdbcTemplate jdbcTemplate;

    public SalesRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Sales> rowMapper = (rs, rowNum) -> {
        Sales sales = new Sales();
        sales.setSalesId(rs.getLong("sales_id"));
        sales.setContactId(rs.getObject("contact_id", Integer.class));

        Long companyId = rs.getObject("company_id", Long.class);
        if (companyId != null) {
            UserFront company = new UserFront();
            company.setUserFrontId(companyId);
            sales.setCompany(company);
        }

        Long branchId = rs.getObject("branch_id", Long.class);
        if (branchId != null) {
            UserFront branch = new UserFront();
            branch.setUserFrontId(branchId);
            sales.setBranch(branch);
        }

        sales.setPrefix(rs.getString("prefix"));
        sales.setSalesNo(rs.getString("sales_no"));
        sales.setTotalAmount(rs.getObject("total_amount", Double.class));

        Date date = rs.getDate("sales_date");
        if (date != null) {
            sales.setSalesDate(date.toLocalDate());
        }

        return sales;
    };

    @Override
    public Sales save(Sales sales) {
        if (sales.getSalesId() == null) {
            String sql = "INSERT INTO sales (contact_id, company_id, branch_id, prefix, sales_no, total_amount, sales_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, sales.getContactId());
                ps.setLong(2, sales.getCompany().getUserFrontId());
                ps.setLong(3, sales.getBranch().getUserFrontId());
                ps.setString(4, sales.getPrefix());
                ps.setString(5, sales.getSalesNo());
                ps.setObject(6, sales.getTotalAmount());
                ps.setDate(7, sales.getSalesDate() != null ? Date.valueOf(sales.getSalesDate()) : null);
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key != null) {
                sales.setSalesId(key.longValue());
            }
        } else {
            String sql = "UPDATE sales SET contact_id = ?, company_id = ?, branch_id = ?, prefix = ?, sales_no = ?, total_amount = ?, sales_date = ? WHERE sales_id = ?";
            jdbcTemplate.update(sql,
                    sales.getContactId(),
                    sales.getCompany().getUserFrontId(),
                    sales.getBranch().getUserFrontId(),
                    sales.getPrefix(),
                    sales.getSalesNo(),
                    sales.getTotalAmount(),
                    sales.getSalesDate() != null ? Date.valueOf(sales.getSalesDate()) : null,
                    sales.getSalesId());
        }
        return sales;
    }

    @Override
    public Optional<Sales> findById(Long id) {
        String sql = "SELECT * FROM sales WHERE sales_id = ?";
        try {
            Sales sales = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(sales);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Sales> findAll() {
        String sql = "SELECT * FROM sales";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Sales> findByBranch_UserFrontId(Long branchId) {
        String sql = "SELECT * FROM sales WHERE branch_id = ?";
        return jdbcTemplate.query(sql, rowMapper, branchId);
    }

    @Override
    public List<Sales> findByCompany_UserFrontId(Long companyId) {
        String sql = "SELECT * FROM sales WHERE company_id = ?";
        return jdbcTemplate.query(sql, rowMapper, companyId);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM sales WHERE sales_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
