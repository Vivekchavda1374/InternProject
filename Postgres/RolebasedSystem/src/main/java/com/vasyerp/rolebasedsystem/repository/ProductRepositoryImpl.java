package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Product> rowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setProductId(rs.getLong("product_id"));
        product.setProductName(rs.getString("product_name"));
        product.setCompanyId(rs.getLong("company_id"));
        product.setItemCode(rs.getString("item_code"));
        product.setMrp(rs.getObject("mrp", Double.class));
        product.setSellingPrice(rs.getObject("selling_price", Double.class));
        product.setDescription(rs.getString("description"));
        product.setStockQuantity(rs.getObject("stock_quantity", Double.class));
        return product;
    };

    @Override
    public Product save(Product product) {
        if (product.getProductId() == null) {
            String sql = "INSERT INTO product (product_name, company_id, item_code, mrp, selling_price, description, stock_quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, product.getProductName());
                ps.setLong(2, product.getCompanyId());
                ps.setString(3, product.getItemCode());
                ps.setObject(4, product.getMrp());
                ps.setObject(5, product.getSellingPrice());
                ps.setString(6, product.getDescription());
                ps.setObject(7, product.getStockQuantity());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key != null) {
                product.setProductId(key.longValue());
            }
        } else {
            String sql = "UPDATE product SET product_name = ?, company_id = ?, item_code = ?, mrp = ?, selling_price = ?, description = ?, stock_quantity = ? WHERE product_id = ?";
            jdbcTemplate.update(sql,
                    product.getProductName(),
                    product.getCompanyId(),
                    product.getItemCode(),
                    product.getMrp(),
                    product.getSellingPrice(),
                    product.getDescription(),
                    product.getStockQuantity(),
                    product.getProductId());
        }
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        try {
            Product product = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Product> findByCompanyId(Long companyId) {
        String sql = "SELECT * FROM product WHERE company_id = ?";
        return jdbcTemplate.query(sql, rowMapper, companyId);
    }

    @Override
    public Optional<Product> findByProductNameAndCompanyId(String productName, Long companyId) {
        String sql = "SELECT * FROM product WHERE product_name = ? AND company_id = ?";
        try {
            Product product = jdbcTemplate.queryForObject(sql, rowMapper, productName, companyId);
            return Optional.ofNullable(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> findByItemCodeAndCompanyId(String itemCode, Long companyId) {
        String sql = "SELECT * FROM product WHERE item_code = ? AND company_id = ?";
        try {
            Product product = jdbcTemplate.queryForObject(sql, rowMapper, itemCode, companyId);
            return Optional.ofNullable(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAllByCompanyIdWithDetails(Long companyId) {
        String sql = "SELECT * FROM product WHERE company_id = ? ORDER BY product_id DESC";
        return jdbcTemplate.query(sql, rowMapper, companyId);
    }

    @Override
    public List<Product> searchProductsByName(Long companyId, String searchTerm) {
        String sql = "SELECT * FROM product WHERE company_id = ? AND LOWER(product_name) LIKE LOWER(CONCAT('%', ?, '%'))";
        return jdbcTemplate.query(sql, rowMapper, companyId, searchTerm);
    }

    @Override
    public List<Product> findByCompanyIdIn(List<Long> companyIds) {
        if (companyIds == null || companyIds.isEmpty()) {
            return Collections.emptyList();
        }
        String inSql = String.join(",", Collections.nCopies(companyIds.size(), "?"));
        String sql = "SELECT * FROM product WHERE company_id IN (" + inSql + ")";
        return jdbcTemplate.query(sql, rowMapper, companyIds.toArray());
    }

    @Override
    public boolean existsByProductIdAndCompanyId(Long productId, Long companyId) {
        String sql = "SELECT COUNT(*) FROM product WHERE product_id = ? AND company_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId, companyId);
        return count != null && count > 0;
    }
}
