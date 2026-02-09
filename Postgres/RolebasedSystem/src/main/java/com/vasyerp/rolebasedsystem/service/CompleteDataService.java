package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CompleteDataDTO;
import com.vasyerp.rolebasedsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompleteDataService {

    private final UserFrontRepository userFrontRepository;
    private final ProductRepository productRepository;
    private final UserRoleRepository userRoleRepository;

    public List<CompleteDataDTO> getAllData() {
        List<CompleteDataDTO> result = new ArrayList<>();
        
        userFrontRepository.findAll().forEach(user -> {
            CompleteDataDTO dto = new CompleteDataDTO();
            dto.setId(user.getUserFrontId());
            dto.setType(user.getParentCompany() == null ? "Company" : "Branch");
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
            
            result.add(dto);
        });
        
        productRepository.findAll().forEach(product -> {
            CompleteDataDTO dto = new CompleteDataDTO();
            dto.setId(product.getProductId());
            dto.setType("Product");
            dto.setProductName(product.getProductName());
            dto.setItemCode(product.getItemCode());
            dto.setMrp(product.getMrp());
            dto.setSellingPrice(product.getSellingPrice());
            dto.setDescription(product.getDescription());
            dto.setStockQuantity(product.getStockQuantity());
            
            var company = userFrontRepository.findById(product.getCompanyId());
            company.ifPresent(c -> dto.setCompanyName(c.getName()));
            
            result.add(dto);
        });
        
        return result;
    }

    public List<CompleteDataDTO> getDataByUser(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return getAllData();
        }
        
        List<CompleteDataDTO> result = new ArrayList<>();
        var user = userFrontRepository.findById(userId);
        
        if (user.isEmpty()) {
            return result;
        }
        
        var currentUser = user.get();
        Long companyId = currentUser.getParentCompany() != null ? 
                         currentUser.getParentCompany().getUserFrontId() : userId;
        
        userFrontRepository.findAll().stream()
            .filter(u -> {
                if (u.getUserFrontId().equals(userId)) return true;
                if (u.getParentCompany() != null && u.getParentCompany().getUserFrontId().equals(companyId)) return true;
                return false;
            })
            .forEach(u -> {
                CompleteDataDTO dto = new CompleteDataDTO();
                dto.setId(u.getUserFrontId());
                dto.setType(u.getParentCompany() == null ? "Company" : "Branch");
                dto.setCompanyName(u.getParentCompany() == null ? u.getName() : null);
                dto.setBranchName(u.getParentCompany() != null ? u.getName() : null);
                dto.setParentCompany(u.getParentCompany() != null ? u.getParentCompany().getName() : null);
                dto.setGstNo(u.getGstNo());
                dto.setPhoneNo(u.getPhoneNo());
                
                if (u.getAddresses() != null && !u.getAddresses().isEmpty()) {
                    var addr = u.getAddresses().get(0);
                    dto.setAddressType(addr.getAddressType());
                    dto.setAddressLine1(addr.getAddressLine1());
                    dto.setAddressLine2(addr.getAddressLine2());
                    dto.setCity(addr.getCity());
                    dto.setState(addr.getState());
                    dto.setCountry(addr.getCountry());
                }
                
                if (u.getRoles() != null && !u.getRoles().isEmpty()) {
                    dto.setRoleName(u.getRoles().stream().findFirst().get().getRoleName());
                }
                
                result.add(dto);
            });
        
        productRepository.findAll().stream()
            .filter(p -> p.getCompanyId().equals(companyId) || p.getCompanyId().equals(userId))
            .forEach(product -> {
                CompleteDataDTO dto = new CompleteDataDTO();
                dto.setId(product.getProductId());
                dto.setType("Product");
                dto.setProductName(product.getProductName());
                dto.setItemCode(product.getItemCode());
                dto.setMrp(product.getMrp());
                dto.setSellingPrice(product.getSellingPrice());
                dto.setDescription(product.getDescription());
                dto.setStockQuantity(product.getStockQuantity());
                
                var company = userFrontRepository.findById(product.getCompanyId());
                company.ifPresent(c -> dto.setCompanyName(c.getName()));
                
                result.add(dto);
            });
        
        return result;
    }
}
