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
        long hierarchyOrderArray[] = { 1 };

        var users = userFrontRepository.findAll();
        var products = productRepository.findAll();

        var productsByCompany = products.stream()
                .collect(Collectors.groupingBy(Product::getCompanyId, Collectors.counting()));

        List<UserFront> companies = users.stream()
                .filter(u -> u.getParentCompany() == null)
                .sorted((c1, c2) -> c1.getUserFrontId().compareTo(c2.getUserFrontId()))
                .collect(Collectors.toList());

        var branchesByCompany = users.stream()
                .filter(u -> u.getParentCompany() != null)
                .collect(Collectors.groupingBy(u -> u.getParentCompany().getUserFrontId()));

        for (UserFront company : companies) {
            Long companyProductCount = productsByCompany.getOrDefault(company.getUserFrontId(), 0L);
            result.add(createDTO(company, companyProductCount, hierarchyOrderArray[0]++));

            // Add Branches for this Company
            List<UserFront> companyBranches = branchesByCompany.getOrDefault(company.getUserFrontId(),
                    new ArrayList<>());
            companyBranches.sort((b1, b2) -> b1.getUserFrontId().compareTo(b2.getUserFrontId()));

            for (UserFront branch : companyBranches) {
                Long branchProductCount = productsByCompany.getOrDefault(branch.getUserFrontId(), 0L);
                result.add(createDTO(branch, branchProductCount, hierarchyOrderArray[0]++));
            }
        }

        return result;
    }

    @Override
    public List<CompleteDataDTO> getDataByUser(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return getAllData();
        }

        List<CompleteDataDTO> result = new ArrayList<>();
        long hierarchyOrderArray[] = { 1 };
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
                .collect(Collectors.groupingBy(Product::getCompanyId, Collectors.counting()));

        relevantUsers.forEach(user -> {
            Long count = productsByCompany.getOrDefault(user.getUserFrontId(), 0L);
            result.add(createDTO(user, count, hierarchyOrderArray[0]++));
        });

        return result;
    }

    private CompleteDataDTO createDTO(UserFront user, Long productCount, Long hierarchyOrder) {
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

        dto.setProductCount(productCount != null ? productCount : 0L);
        dto.setHierarchyOrder(hierarchyOrder);

        dto.setId(user.getUserFrontId());
        dto.setType(user.getParentCompany() == null ? "Company" : "Branch");

        return dto;
    }
}
