package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.Product;
import com.vasyerp.rolebasedsystem.model.Purchase;
import com.vasyerp.rolebasedsystem.model.PurchaseItem;
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
public class PurchaseItemRepositoryImpl implements PurchaseItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public PurchaseItemRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PurchaseItem> rowMapper = (rs, rowNum) -> {
        PurchaseItem item = new PurchaseItem();
        item.setPurchaseItemId(rs.getLong("purchase_item_id"));

        Long purchaseId = rs.getObject("purchase_id", Long.class);
        if (purchaseId != null) {
            Purchase purchase = new Purchase();
            purchase.setPurchaseId(purchaseId);
            item.setPurchase(purchase);
        }

        Long productId = rs.getObject("product_id", Long.class);
        if (productId != null) {
            Product product = new Product();
            product.setProductId(productId);
            item.setProduct(product);
        }

        item.setQuantity(rs.getObject("quantity", Double.class));
        item.setPurchasePrice(rs.getObject("purchase_price", Double.class));
        return item;
    };

    @Override
    public PurchaseItem save(PurchaseItem purchaseItem) {
        if (purchaseItem.getPurchaseItemId() == null) {
            String sql = "INSERT INTO purchase_item (purchase_id, product_id, quantity, purchase_price) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, purchaseItem.getPurchase().getPurchaseId());
                ps.setLong(2, purchaseItem.getProduct().getProductId());
                ps.setObject(3, purchaseItem.getQuantity());
                ps.setObject(4, purchaseItem.getPurchasePrice());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key != null) {
                purchaseItem.setPurchaseItemId(key.longValue());
            }
        } else {
            String sql = "UPDATE purchase_item SET purchase_id = ?, product_id = ?, quantity = ?, purchase_price = ? WHERE purchase_item_id = ?";
            jdbcTemplate.update(sql,
                    purchaseItem.getPurchase().getPurchaseId(),
                    purchaseItem.getProduct().getProductId(),
                    purchaseItem.getQuantity(),
                    purchaseItem.getPurchasePrice(),
                    purchaseItem.getPurchaseItemId());
        }
        return purchaseItem;
    }

    @Override
    public Optional<PurchaseItem> findById(Long id) {
        String sql = "SELECT * FROM purchase_item WHERE purchase_item_id = ?";
        try {
            PurchaseItem item = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PurchaseItem> findAll() {
        String sql = "SELECT * FROM purchase_item";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM purchase_item WHERE purchase_item_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<PurchaseItem> findByPurchase_PurchaseId(Long purchaseId) {
        String sql = "SELECT * FROM purchase_item WHERE purchase_id = ?";
        return jdbcTemplate.query(sql, rowMapper, purchaseId);
    }
}
