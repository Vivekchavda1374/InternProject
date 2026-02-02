package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.*;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.model.UserRole;
import com.vasyerp.rolebasedsystem.model.UserRoleNew;
import com.vasyerp.rolebasedsystem.model.UserFrontAddress;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import com.vasyerp.rolebasedsystem.repository.UserRoleNewRepository;
import com.vasyerp.rolebasedsystem.repository.UserRoleRepository;
import com.vasyerp.rolebasedsystem.repository.UserFrontAddressRepository;
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
    private final UserFrontAddressRepository addressRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserFrontService(UserFrontRepository userFrontRepository,
            UserRoleRepository userRoleRepository,
            UserRoleNewRepository userRoleNewRepository,
            UserFrontAddressRepository addressRepository) {
        this.userFrontRepository = userFrontRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRoleNewRepository = userRoleNewRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    private UserFrontDTO convertToDTO(UserFront userFront) {
        List<AddressDTO> addresses = userFront.getAddresses().stream()
                .map(this::convertAddressToDTO)
                .collect(Collectors.toList());

        return new UserFrontDTO(
                userFront.getUserFrontId(),
                userFront.getName(),
                userFront.getParentCompany() != null ? userFront.getParentCompany().getUserFrontId() : null,
                userFront.getGstNo(),
                userFront.getPhoneNo(),
                addresses);
    }

    private AddressDTO convertAddressToDTO(UserFrontAddress address) {
        return new AddressDTO(
                address.getUserFrontAddressId(),
                address.getName(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getAddressType());
    }

    public AddressDTO addAddress(Long userFrontId, CreateAddressRequest request) {
        UserFront userFront = userFrontRepository.findById(userFrontId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserFrontAddress address = new UserFrontAddress();
        address.setUserFront(userFront);
        address.setName(request.getName());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setAddressType(request.getAddressType());

        UserFrontAddress savedAddress = addressRepository.save(address);
        return convertAddressToDTO(savedAddress);
    }

    public List<AddressDTO> getAddresses(Long userFrontId) {
        return addressRepository.findByUserFront_UserFrontId(userFrontId)
                .stream()
                .map(this::convertAddressToDTO)
                .collect(Collectors.toList());
    }

    public void deleteAddress(Long addressId) {
        UserFrontAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        addressRepository.delete(address);
    }

    public UserFrontDTO createCompany(Long userId, CreateUserFrontRequest request) {
        UserFront currentUser = userFrontRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!"admin".equals(currentUser.getName())) {
            throw new RuntimeException("Only default admin can create companies");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Company name cannot be empty");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        if (request.getParentCompanyId() != null) {
            throw new RuntimeException("Company cannot have a parent company");
        }

        UserFront company = new UserFront();
        company.setName(request.getName());
        company.setPassword(passwordEncoder.encode(request.getPassword()));
        company.setParentCompany(null);
        company.setGstNo(request.getGstNo());
        company.setPhoneNo(request.getPhoneNo());
        company.setAddresses(new ArrayList<>());

        UserFront savedCompany = userFrontRepository.save(company);

        if (request.getAddressLine1() != null || request.getCity() != null) {
            UserFrontAddress address = new UserFrontAddress();
            address.setUserFront(savedCompany);
            address.setName(savedCompany.getName());
            address.setAddressLine1(request.getAddressLine1());
            address.setAddressLine2(request.getAddressLine2());
            address.setCity(request.getCity());
            address.setState(request.getState());
            address.setCountry(request.getCountry());
            address.setAddressType("Primary");
            addressRepository.save(address);
        }

        return convertToDTO(userFrontRepository.findById(savedCompany.getUserFrontId()).get());
    }

    public UserFrontDTO createBranch(CreateUserFrontRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Branch name cannot be empty");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        if (request.getParentCompanyId() == null) {
            throw new RuntimeException("Parent company ID is required for branch");
        }
        UserFront parentCompany = userFrontRepository
                .findById(request.getParentCompanyId())
                .orElseThrow(() -> new RuntimeException("Parent company not found"));

        if (parentCompany.getParentCompany() != null) {
            throw new RuntimeException("Cannot create branch under a branch. Parent must be a company");
        }

        UserFront branch = new UserFront();
        branch.setName(request.getName());
        branch.setPassword(passwordEncoder.encode(request.getPassword()));
        branch.setParentCompany(parentCompany);
        branch.setGstNo(request.getGstNo());
        branch.setPhoneNo(request.getPhoneNo());
        branch.setAddresses(new ArrayList<>());

        UserFront savedBranch = userFrontRepository.save(branch);

        if (request.getAddressLine1() != null || request.getCity() != null) {
            UserFrontAddress address = new UserFrontAddress();
            address.setUserFront(savedBranch);
            address.setName(savedBranch.getName());
            address.setAddressLine1(request.getAddressLine1());
            address.setAddressLine2(request.getAddressLine2());
            address.setCity(request.getCity());
            address.setState(request.getState());
            address.setCountry(request.getCountry());
            address.setAddressType("Primary");
            addressRepository.save(address);
        }

        return convertToDTO(userFrontRepository.findById(savedBranch.getUserFrontId()).get());
    }

    public List<UserFrontDTO> getAllCompanies() {
        return userFrontRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserFrontDTO> getBranchesByCompany(Long companyId) {
        UserFront company = userFrontRepository
                .findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return company.getBranches()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserFrontDTO> getCompaniesByUser(Long userId) {
        UserFront currentUser = userFrontRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("admin".equals(currentUser.getName())) {
            return getAllCompanies();
        }

        if (currentUser.getParentCompany() == null) {
            List<UserFrontDTO> result = new ArrayList<>();
            result.add(convertToDTO(currentUser));
            result.addAll(getBranchesByCompany(currentUser.getUserFrontId()));
            return result;
        }
        List<UserFrontDTO> result = new ArrayList<>();
        UserFront parentCompany = currentUser.getParentCompany();
        result.add(convertToDTO(parentCompany));
        result.addAll(getBranchesByCompany(parentCompany.getUserFrontId()));
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

        if ("admin".equals(userFront.getName()) && !"admin".equals(request.getName())) {
            throw new RuntimeException("Cannot change name of default admin");
        }
        userFront.setName(request.getName());
        UserFront updatedUserFront = userFrontRepository.save(userFront);
        return convertToDTO(updatedUserFront);
    }

    public void deleteUserFront(Long userFrontId) {
        UserFront userFront = userFrontRepository
                .findById(userFrontId)
                .orElseThrow(() -> new RuntimeException("User/Company/Branch not found"));

        if (userFront.getParentCompany() == null) {
            boolean hasBranches = userFrontRepository.existsByParentCompany(userFront);
            if (hasBranches) {
                throw new RuntimeException("Cannot delete company with existing branches");
            }
        }
        List<UserRoleNew> roleAssignments = userRoleNewRepository.findByUserFrontId(userFrontId);
        roleAssignments.forEach(userRoleNewRepository::delete);

        userFrontRepository.delete(userFront);
    }

    public UserRoleDTO assignRoleToUser(AssignUserRoleRequest request) {
        UserFront userFront = userFrontRepository
                .findById(request.getUserFrontId())
                .orElseThrow(() -> new RuntimeException("User/Company/Branch not found"));
        UserRole role = userRoleRepository
                .findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (userRoleNewRepository.findByUserFrontIdAndRoleId(request.getUserFrontId(), request.getRoleId())
                .isPresent()) {
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

        List<UserRoleNew> userRoles = userRoleNewRepository.findByUserFrontId(userFrontId);
        List<Long> roleIds = userRoles.stream()
                .map(UserRoleNew::getRoleId)
                .collect(Collectors.toList());
        List<String> roleNames = userRoles.stream()
                .map(userRole -> userRoleRepository.findById(userRole.getRoleId())
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

    public boolean hasRole(Long userFrontId, String roleName) {
        UserFront userFront = userFrontRepository.findById(userFrontId)
                .orElse(null);
        if (userFront == null) {
            return false;
        }
        return userFront.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals(roleName));
    }

}
