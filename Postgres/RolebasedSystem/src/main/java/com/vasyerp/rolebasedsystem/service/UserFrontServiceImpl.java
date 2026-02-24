package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.AddressDTO;
import com.vasyerp.rolebasedsystem.dto.AssignUserRoleRequest;
import com.vasyerp.rolebasedsystem.dto.CreateAddressRequest;
import com.vasyerp.rolebasedsystem.dto.CreateUserFrontRequest;
import com.vasyerp.rolebasedsystem.dto.UpdateUserFrontRequest;
import com.vasyerp.rolebasedsystem.dto.UserFrontDTO;
import com.vasyerp.rolebasedsystem.dto.UserRoleDTO;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.model.UserFrontAddress;
import com.vasyerp.rolebasedsystem.model.UserRole;
import com.vasyerp.rolebasedsystem.model.UserRoleNew;
import com.vasyerp.rolebasedsystem.repository.UserFrontAddressRepository;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import com.vasyerp.rolebasedsystem.repository.UserRoleNewRepository;
import com.vasyerp.rolebasedsystem.repository.UserRoleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserFrontServiceImpl implements UserFrontService {
    private static final Pattern namePattern = Pattern.compile("^[A-Za-z0-9 ]+_*$");

    private final UserFrontRepository userFrontRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleNewRepository userRoleNewRepository;
    private final UserFrontAddressRepository addressRepository;
    private final CountryService countryService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserFrontServiceImpl(UserFrontRepository userFrontRepository,
            UserRoleRepository userRoleRepository,
            UserRoleNewRepository userRoleNewRepository,
            UserFrontAddressRepository addressRepository,
            CountryService countryService) {
        this.userFrontRepository = userFrontRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRoleNewRepository = userRoleNewRepository;
        this.addressRepository = addressRepository;
        this.countryService = countryService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "addressesByUser", key = "#userFrontId"),
            @CacheEvict(value = "userFrontById", allEntries = true),
            @CacheEvict(value = "usersByCountry", allEntries = true),
            @CacheEvict(value = "completeDataAll", allEntries = true),
            @CacheEvict(value = "completeDataByUser", allEntries = true)
    })
    public AddressDTO addAddress(Long userFrontId, CreateAddressRequest request) {
        UserFront userFront = userFrontRepository.findById(userFrontId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String country = normalizeText(request.getCountry());
        if (country == null) {
            throw new RuntimeException("Country is required for address");
        }
        countryService.getOrCreateCountry(country);

        UserFrontAddress address = new UserFrontAddress();
        address.setUserFront(userFront);
        address.setName(normalizeText(request.getName()));
        address.setAddressLine1(normalizeText(request.getAddressLine1()));
        address.setAddressLine2(normalizeText(request.getAddressLine2()));
        address.setCity(normalizeText(request.getCity()));
        address.setState(normalizeText(request.getState()));
        address.setCountry(country);
        address.setAddressType(normalizeText(request.getAddressType()));

        UserFrontAddress savedAddress = addressRepository.save(address);

        return convertAddressToDTO(savedAddress);
    }

    @Override
    @Cacheable(value = "addressesByUser", key = "#userFrontId")
    public List<AddressDTO> getAddresses(Long userFrontId) {
        return addressRepository.findByUserFront_UserFrontId(userFrontId)
                .stream()
                .map(this::convertAddressToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "addressesByUser", allEntries = true),
            @CacheEvict(value = "userFrontById", allEntries = true),
            @CacheEvict(value = "usersByCountry", allEntries = true),
            @CacheEvict(value = "completeDataAll", allEntries = true),
            @CacheEvict(value = "completeDataByUser", allEntries = true)
    })
    public void deleteAddress(Long addressId) {
        UserFrontAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        addressRepository.deleteById(address.getUserFrontAddressId());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "allCompanies", allEntries = true),
            @CacheEvict(value = "branchesByCompany", allEntries = true),
            @CacheEvict(value = "companiesByUser", allEntries = true),
            @CacheEvict(value = "userFrontById", allEntries = true),
            @CacheEvict(value = "addressesByUser", allEntries = true),
            @CacheEvict(value = "usersByCountry", allEntries = true),
            @CacheEvict(value = "completeDataAll", allEntries = true),
            @CacheEvict(value = "completeDataByUser", allEntries = true)
    })
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

        if (hasAddressInput(request)) {
            String country = normalizeText(request.getCountry());
            if (country == null) {
                throw new RuntimeException("Country is required when address is provided");
            }
            countryService.getOrCreateCountry(country);
            addressRepository.save(buildPrimaryAddress(savedCompany, request, country));
        }

        return convertToDTO(userFrontRepository.findById(savedCompany.getUserFrontId()).get());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "allCompanies", allEntries = true),
            @CacheEvict(value = "branchesByCompany", allEntries = true),
            @CacheEvict(value = "companiesByUser", allEntries = true),
            @CacheEvict(value = "userFrontById", allEntries = true),
            @CacheEvict(value = "addressesByUser", allEntries = true),
            @CacheEvict(value = "usersByCountry", allEntries = true),
            @CacheEvict(value = "completeDataAll", allEntries = true),
            @CacheEvict(value = "completeDataByUser", allEntries = true)
    })
    public UserFrontDTO createBranch(CreateUserFrontRequest request) {
        String normalizedBranchName = normalizeBranchName(request.getName());
        validateBranchName(normalizedBranchName);

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

        if (userFrontRepository.existsBranchByParentCompanyIdAndName(
                request.getParentCompanyId(), normalizedBranchName)) {
            throw new RuntimeException("Branch already exists.");
        }


        UserFront branch = new UserFront();
        branch.setName(normalizedBranchName);
        branch.setPassword(passwordEncoder.encode(request.getPassword()));
        branch.setParentCompany(parentCompany);
        branch.setGstNo(request.getGstNo());
        branch.setPhoneNo(request.getPhoneNo());
        branch.setAddresses(new ArrayList<>());

        UserFront savedBranch = userFrontRepository.save(branch);

        return convertToDTO(userFrontRepository.findById(savedBranch.getUserFrontId()).get());
    }

    @Override
    public boolean branchNameExists(Long parentCompanyId, String branchName) {
        String normalizedBranchName = normalizeBranchName(branchName);
        if ((parentCompanyId == null) || normalizedBranchName.isEmpty()) {
            return false;
        }
        return userFrontRepository.existsBranchByParentCompanyIdAndName(parentCompanyId, normalizedBranchName);
    }

    @Override
    @Cacheable("allCompanies")
    public List<UserFrontDTO> getAllCompanies() {
        return userFrontRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "branchesByCompany", key = "#companyId")
    public List<UserFrontDTO> getBranchesByCompany(Long companyId) {
        UserFront company = userFrontRepository
                .findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (company.getParentCompany() != null) {
            throw new RuntimeException("Provided user is not a company");
        }

        return userFrontRepository.findByParentCompany(company)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "companiesByUser", key = "#userId")
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
        Long parentCompanyId = currentUser.getParentCompany().getUserFrontId();
        UserFront parentCompany = userFrontRepository.findById(parentCompanyId)
                .orElseThrow(() -> new RuntimeException("Parent company not found"));
        result.add(convertToDTO(parentCompany));
        result.addAll(getBranchesByCompany(parentCompany.getUserFrontId()));
        return result;
    }

    @Override
    @Cacheable(value = "userFrontById", key = "#userFrontId")
    public UserFrontDTO getUserFrontById(Long userFrontId) {
        UserFront userFront = userFrontRepository
                .findById(userFrontId)
                .orElseThrow(() -> new RuntimeException("User/Company/Branch not found"));
        return convertToDTO(userFront);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "allCompanies", allEntries = true),
            @CacheEvict(value = "branchesByCompany", allEntries = true),
            @CacheEvict(value = "companiesByUser", allEntries = true),
            @CacheEvict(value = "userFrontById", allEntries = true),
            @CacheEvict(value = "usersByCountry", allEntries = true),
            @CacheEvict(value = "completeDataAll", allEntries = true),
            @CacheEvict(value = "completeDataByUser", allEntries = true)
    })
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

    @Override
    @Caching(evict = {
            @CacheEvict(value = "allCompanies", allEntries = true),
            @CacheEvict(value = "branchesByCompany", allEntries = true),
            @CacheEvict(value = "companiesByUser", allEntries = true),
            @CacheEvict(value = "userFrontById", allEntries = true),
            @CacheEvict(value = "addressesByUser", allEntries = true),
            @CacheEvict(value = "usersByCountry", allEntries = true),
            @CacheEvict(value = "userRolesByUser", allEntries = true),
            @CacheEvict(value = "hasRole", allEntries = true),
            @CacheEvict(value = "completeDataAll", allEntries = true),
            @CacheEvict(value = "completeDataByUser", allEntries = true)
    })
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
        roleAssignments.forEach(arg0 -> userRoleNewRepository.deleteById(arg0.getUserRoleNewId()));

        userFrontRepository.deleteById(userFront.getUserFrontId());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userRolesByUser", key = "#request.userFrontId"),
            @CacheEvict(value = "hasRole", allEntries = true)
    })
    public UserRoleDTO assignRoleToUser(AssignUserRoleRequest request) {
        if (!userFrontRepository.existsById(request.getUserFrontId())) {
            throw new RuntimeException("User/Company/Branch not found");
        }
        if (!userRoleRepository.existsById(request.getRoleId())) {
            throw new RuntimeException("Role not found");
        }

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

    @Override
    @Caching(evict = {
            @CacheEvict(value = "userRolesByUser", key = "#userFrontId"),
            @CacheEvict(value = "hasRole", allEntries = true)
    })
    public UserRoleDTO revokeRoleFromUser(Long userFrontId, Long roleId) {
        userFrontRepository
                .findById(userFrontId)
                .orElseThrow(() -> new RuntimeException("User/Company/Branch not found"));
        UserRoleNew userRole = userRoleNewRepository
                .findByUserFrontIdAndRoleId(userFrontId, roleId)
                .orElseThrow(() -> new RuntimeException("Role is not assigned to this user"));

        userRoleNewRepository.deleteById(userRole.getUserRoleNewId());

        return getUserRoles(userFrontId);
    }

    @Override
    @Cacheable(value = "userRolesByUser", key = "#userFrontId")
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

    @Override
    @Cacheable("allRoles")
    public List<UserRole> getAllRoles() {
        return userRoleRepository.findAll();
    }

    @Override
    @Cacheable(value = "usersByCountry", key = "#countryName")
    public List<UserFrontDTO> findByCountry(String countryName) {
        List<UserFrontAddress> addresses = addressRepository.findAll().stream()
                .filter(address -> countryName.equalsIgnoreCase(address.getCountry()))
                .collect(Collectors.toList());

        List<UserFront> users = addresses.stream()
                .map(UserFrontAddress::getUserFront)
                .distinct()
                .collect(Collectors.toList());

        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "hasRole", key = "#userFrontId + ':' + #roleName")
    public boolean hasRole(Long userFrontId, String roleName) {
        UserFront userFront = userFrontRepository.findById(userFrontId)
                .orElse(null);
        if (userFront == null) {
            return false;
        }
        return userFront.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals(roleName));
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

    private String normalizeBranchName(String branchName) {
        return branchName == null ? "" : branchName.trim();
    }

    private void validateBranchName(String branchName) {
        if (branchName.isEmpty()) {
            throw new RuntimeException("Branch name cannot be empty");
        }
        if (!namePattern.matcher(branchName).matches()) {
            throw new RuntimeException("Branch name must not contain special characters");
        }
    }

    private boolean hasAddressInput(CreateUserFrontRequest request) {
        return hasText(request.getAddressLine1())
                || hasText(request.getAddressLine2())
                || hasText(request.getCity())
                || hasText(request.getState())
                || hasText(request.getCountry());
    }

    private UserFrontAddress buildPrimaryAddress(UserFront userFront, CreateUserFrontRequest request, String country) {
        UserFrontAddress address = new UserFrontAddress();
        address.setUserFront(userFront);
        address.setName(userFront.getName());
        address.setAddressLine1(normalizeText(request.getAddressLine1()));
        address.setAddressLine2(normalizeText(request.getAddressLine2()));
        address.setCity(normalizeText(request.getCity()));
        address.setState(normalizeText(request.getState()));
        address.setCountry(country);
        address.setAddressType("Primary");
        return address;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String normalizeText(String value) {
        return hasText(value) ? value.trim() : null;
    }

}
