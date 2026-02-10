package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CompleteDataDTO;
import com.vasyerp.rolebasedsystem.model.Product;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.repository.ProductRepository;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompleteDataServiceImpl implements CompleteDataService {

    private final UserFrontRepository userFrontRepository;
    private final ProductRepository productRepository;

    public CompleteDataServiceImpl(UserFrontRepository userFrontRepository, ProductRepository productRepository) {
        this.userFrontRepository = userFrontRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<CompleteDataDTO> getAllData() {
        List<CompleteDataDTO> result = new ArrayList<>();

        var users = userFrontRepository.findAll();
        var products = productRepository.findAll();

        var productsByCompany = products.stream()
                .collect(Collectors.groupingBy(Product::getCompanyId));

        users.forEach(user -> {
            List<Product> userProducts = productsByCompany
                    .getOrDefault(user.getUserFrontId(), new ArrayList<>());

            if (userProducts.isEmpty()) {
                result.add(createDTO(user, null));
            } else {
                userProducts.forEach(product -> result.add(createDTO(user, product)));
            }
        });

        return result;
    }

    @Override
    public List<CompleteDataDTO> getDataByUser(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return getAllData();
        }

        List<CompleteDataDTO> result = new ArrayList<>();
        var userOpt = userFrontRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return result;
        }

        var currentUser = userOpt.get();
        List<UserFront> relevantUsers = new ArrayList<>();

        if (currentUser.getParentCompany() == null) {
            relevantUsers = userFrontRepository.findAll().stream()
                    .filter(u -> u.getUserFrontId().equals(userId) ||
                            (u.getParentCompany() != null && u.getParentCompany().getUserFrontId().equals(userId)))
                    .collect(Collectors.toList());
        } else {
            relevantUsers.add(currentUser);
        }

        List<Long> relevantUserIds = relevantUsers.stream()
                .map(UserFront::getUserFrontId)
                .collect(Collectors.toList());

        var products = productRepository.findAll().stream()
                .filter(p -> relevantUserIds.contains(p.getCompanyId()))
                .collect(Collectors.toList());

        var productsByCompany = products.stream()
                .collect(Collectors.groupingBy(Product::getCompanyId));

        relevantUsers.forEach(user -> {
            List<Product> userProducts = productsByCompany
                    .getOrDefault(user.getUserFrontId(), new ArrayList<>());

            if (userProducts.isEmpty()) {
                result.add(createDTO(user, null));
            } else {
                userProducts.forEach(product -> result.add(createDTO(user, product)));
            }
        });

        return result;
    }

    private CompleteDataDTO createDTO(UserFront user, Product product) {
        CompleteDataDTO dto = new CompleteDataDTO();

        dto.setCompanyName(user.getParentCompany() == null ? user.getName() : null);
        dto.setBranchName(user.getParentCompany() != null ? user.getName() : null);
        dto.setParentCompany(user.getParentCompany() != null ? user.getParentCompany().getName() : null);
        dto.setGstNo(user.getGstNo());
        dto.setPhoneNo(user.getPhoneNo());

        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
            var addr = user.getAddresses().get(0);
            dto.setAddressType(addr.getAddressType());
            dto.setAddressLine1(addr.getAddressLine1());
            dto.setAddressLine2(addr.getAddressLine2());
            dto.setCity(addr.getCity());
            dto.setState(addr.getState());
            dto.setCountry(addr.getCountry());
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            dto.setRoleName(user.getRoles().stream().findFirst().get().getRoleName());
        }

        dto.setUserFrontId(user.getUserFrontId());

        if (product != null) {
            dto.setId(product.getProductId());
            dto.setType("Product");
            dto.setProductName(product.getProductName());
            dto.setItemCode(product.getItemCode());
            dto.setMrp(product.getMrp());
            dto.setSellingPrice(product.getSellingPrice());
            dto.setDescription(product.getDescription());
            dto.setStockQuantity(product.getStockQuantity());
        } else {
            dto.setId(user.getUserFrontId());
            dto.setType(user.getParentCompany() == null ? "Company" : "Branch");
        }

        return dto;
    }
}
