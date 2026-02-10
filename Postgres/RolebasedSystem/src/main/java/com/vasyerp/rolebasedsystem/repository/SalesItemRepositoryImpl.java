package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Product;
import com.vasyerp.rolebasedsystem.model.Sales;
import com.vasyerp.rolebasedsystem.model.SalesItem;
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
public class SalesItemRepositoryImpl implements SalesItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public SalesItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<SalesItem> rowMapper = (rs, rowNum) -> {
        SalesItem item = new SalesItem();
        item.setSalesItemId(rs.getLong("sales_item_id"));

        Long salesId = rs.getObject("sales_id", Long.class);
        if (salesId != null) {
            Sales sales = new Sales();
            sales.setSalesId(salesId);
            item.setSales(sales);
        }

        Long productId = rs.getObject("product_id", Long.class);
        if (productId != null) {
            Product product = new Product();
            product.setProductId(productId);
            item.setProduct(product);
        }

        item.setQuantity(rs.getObject("quantity", Double.class));
        item.setSellingPrice(rs.getObject("selling_price", Double.class));
        return item;
    };

    @Override
    public SalesItem save(SalesItem salesItem) {
        if (salesItem.getSalesItemId() == null) {
            String sql = "INSERT INTO sales_item (sales_id, product_id, quantity, selling_price) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, salesItem.getSales().getSalesId());
                ps.setLong(2, salesItem.getProduct().getProductId());
                ps.setObject(3, salesItem.getQuantity());
                ps.setObject(4, salesItem.getSellingPrice());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key != null) {
                salesItem.setSalesItemId(key.longValue());
            }
        } else {
            String sql = "UPDATE sales_item SET sales_id = ?, product_id = ?, quantity = ?, selling_price = ? WHERE sales_item_id = ?";
            jdbcTemplate.update(sql,
                    salesItem.getSales().getSalesId(),
                    salesItem.getProduct().getProductId(),
                    salesItem.getQuantity(),
                    salesItem.getSellingPrice(),
                    salesItem.getSalesItemId());
        }
        return salesItem;
    }

    @Override
    public Optional<SalesItem> findById(Long id) {
        String sql = "SELECT * FROM sales_item WHERE sales_item_id = ?";
        try {
            SalesItem item = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<SalesItem> findAll() {
        String sql = "SELECT * FROM sales_item";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM sales_item WHERE sales_item_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<SalesItem> findBySales_SalesId(Long salesId) {
        String sql = "SELECT * FROM sales_item WHERE sales_id = ?";
        return jdbcTemplate.query(sql, rowMapper, salesId);
    }
}
