package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.*;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.model.UserRole;
import com.vasyerp.rolebasedsystem.model.UserRoleNew;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import com.vasyerp.rolebasedsystem.repository.UserRoleNewRepository;
import com.vasyerp.rolebasedsystem.repository.UserRoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserFrontService {

    private final UserFrontRepository userFrontRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleNewRepository userRoleNewRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserFrontService(UserFrontRepository userFrontRepository,
                            UserRoleRepository userRoleRepository,
                            UserRoleNewRepository userRoleNewRepository) {
        this.userFrontRepository = userFrontRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRoleNewRepository = userRoleNewRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    private UserFrontDTO convertToDTO(UserFront userFront) {
        return new UserFrontDTO(
                userFront.getUserFrontId(),
                userFront.getName(),
                userFront.getUsername(),
                userFront.getParentCompanyId(),
                userFront.getGstNo(),
                userFront.getPhoneNo()
        );
    }

    public UserFrontDTO createCompany(Long userId, CreateUserFrontRequest request) {
        // Only default admin can create companies
        UserFront currentUser = userFrontRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!"admin".equals(currentUser.getUsername())) {
            throw new RuntimeException("Only default admin can create companies");
        }
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Company name cannot be empty");
        }

        if (request.getParentCompanyId() != null) {
            throw new RuntimeException("Company cannot have a parent company");
        }

        UserFront company = new UserFront();
        company.setName(request.getName());
        company.setUsername(request.getUsername() != null ? request.getUsername() : request.getName().toLowerCase().replaceAll("\\s+", ""));
        company.setPassword(request.getPassword() != null ? passwordEncoder.encode(request.getPassword()) : passwordEncoder.encode("admin123"));
        company.setParentCompanyId(null);
        company.setGstNo(request.getGstNo());
        company.setPhoneNo(request.getPhoneNo());

        UserFront savedCompany = userFrontRepository.save(company);
        return convertToDTO(savedCompany);
    }

    public UserFrontDTO createBranch(CreateUserFrontRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Branch name cannot be empty");
        }

        if (request.getParentCompanyId() == null) {
            throw new RuntimeException("Parent company ID is required for branch");
        }
        UserFront parentCompany = userFrontRepository
                .findById(request.getParentCompanyId())
                .orElseThrow(() -> new RuntimeException("Parent company not found"));

        if (parentCompany.getParentCompanyId() != null) {
            throw new RuntimeException("Cannot create branch under a branch. Parent must be a company");
        }

        UserFront branch = new UserFront();
        branch.setName(request.getName());
        branch.setUsername(request.getUsername() != null ? request.getUsername() : request.getName().toLowerCase().replaceAll("\\s+", "") + "_user");
        branch.setPassword(request.getPassword() != null ? passwordEncoder.encode(request.getPassword()) : passwordEncoder.encode("user123"));
        branch.setParentCompanyId(request.getParentCompanyId());
        branch.setGstNo(request.getGstNo());
        branch.setPhoneNo(request.getPhoneNo());

        UserFront savedBranch = userFrontRepository.save(branch);
        return convertToDTO(savedBranch);
    }
    public List<UserFrontDTO> getAllCompanies() {
        return userFrontRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<UserFrontDTO> getBranchesByCompany(Long companyId) {
        userFrontRepository
                .findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return userFrontRepository.findBranchesByCompany(companyId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserFrontDTO> getCompaniesByUser(Long userId) {
        UserFront currentUser = userFrontRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Default admin sees all
        if ("admin".equals(currentUser.getUsername())) {
            return getAllCompanies();
        }

        // Regular companies see only themselves and their branches
        if (currentUser.getParentCompanyId() == null) {
            List<UserFrontDTO> result = new ArrayList<>();
            result.add(convertToDTO(currentUser));
            result.addAll(getBranchesByCompany(currentUser.getUserFrontId()));
            return result;
        }

        // Branches see their parent company and sibling branches
        List<UserFrontDTO> result = new ArrayList<>();
        UserFront parentCompany = userFrontRepository.findById(currentUser.getParentCompanyId())
                .orElseThrow(() -> new RuntimeException("Parent company not found"));
        result.add(convertToDTO(parentCompany));
        result.addAll(getBranchesByCompany(currentUser.getParentCompanyId()));
        return result;
    }
    public UserFrontDTO getUserFrontById(Long userFrontId) {
        UserFront userFront = userFrontRepository
                .findById(userFrontId)
                .orElseThrow(() -> new RuntimeException("User/Company/Branch not found"));
        return convertToDTO(userFront);
    }
    public UserFrontDTO updateUserFront(Long userFrontId, UpdateUserFrontRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }

        UserFront userFront = userFrontRepository
                .findById(userFrontId)
                .orElseThrow(() -> new RuntimeException("User/Company/Branch not found"));

        userFront.setName(request.getName());
        UserFront updatedUserFront = userFrontRepository.save(userFront);
        return convertToDTO(updatedUserFront);
    }
    public void deleteUserFront(Long userFrontId) {
        UserFront userFront = userFrontRepository
                .findById(userFrontId)
                .orElseThrow(() -> new RuntimeException("User/Company/Branch not found"));

        if (userFront.getParentCompanyId() == null) {
            boolean hasBranches = userFrontRepository.existsByParentCompanyId(userFrontId);
            if (hasBranches) {
                throw new RuntimeException("Cannot delete company with existing branches");
            }
        }
        List<UserRoleNew> roleAssignments = userRoleNewRepository
                .findRolesByUserFrontId(userFrontId)
                .stream()
                .map(roleId -> userRoleNewRepository
                        .findByUserFrontIdAndRoleId(userFrontId, roleId)
                        .orElse(null))
                .toList();

        roleAssignments.forEach(role -> {
            if (role != null) {
                userRoleNewRepository.delete(role);
            }
        });

        userFrontRepository.delete(userFront);
    }
    public UserRoleDTO assignRoleToUser(AssignUserRoleRequest request) {
        UserFront userFront = userFrontRepository
                .findById(request.getUserFrontId())
                .orElseThrow(() -> new RuntimeException("User/Company/Branch not found"));
        UserRole role = userRoleRepository
                .findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (userRoleNewRepository.
                findByUserFrontIdAndRoleId(request.getUserFrontId(), request.getRoleId()).isPresent())
        {
            throw new RuntimeException("Role is already assigned to this user");
        }

        UserRoleNew userRole = new UserRoleNew();
        userRole.setUserFrontId(request.getUserFrontId());
        userRole.setRoleId(request.getRoleId());

        userRoleNewRepository.save(userRole);

        return getUserRoles(request.getUserFrontId());
    }
    public UserRoleDTO revokeRoleFromUser(Long userFrontId, Long roleId) {
        userFrontRepository
                .findById(userFrontId)
                .orElseThrow(() -> new RuntimeException("User/Company/Branch not found"));
        UserRoleNew userRole = userRoleNewRepository
                .findByUserFrontIdAndRoleId(userFrontId, roleId)
                .orElseThrow(() -> new RuntimeException("Role is not assigned to this user"));

        userRoleNewRepository.delete(userRole);

        return getUserRoles(userFrontId);
    }
    public UserRoleDTO getUserRoles(Long userFrontId) {
        UserFront userFront = userFrontRepository.findById(userFrontId)
                .orElseThrow(() -> new RuntimeException("User/Company/Branch not found"));

        List<Long> roleIds = userRoleNewRepository.findRolesByUserFrontId(userFrontId);
        List<String> roleNames = roleIds.stream()
                .map(roleId -> userRoleRepository.findById(roleId)
                        .map(UserRole::getRoleName)
                        .orElse("Unknown"))
                .collect(Collectors.toList());

        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserFrontId(userFrontId);
        userRoleDTO.setUserName(userFront.getName());
        userRoleDTO.setRoleIds(roleIds);
        userRoleDTO.setRoleNames(roleNames);

        return userRoleDTO;
    }

    public List<UserRole> getAllRoles() {
        return userRoleRepository.findAll();
    }

}
