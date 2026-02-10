package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.AddressDTO;
import com.vasyerp.rolebasedsystem.dto.AssignUserRoleRequest;
import com.vasyerp.rolebasedsystem.dto.CreateAddressRequest;
import com.vasyerp.rolebasedsystem.dto.CreateUserFrontRequest;
import com.vasyerp.rolebasedsystem.dto.UpdateUserFrontRequest;
import com.vasyerp.rolebasedsystem.dto.UserFrontDTO;
import com.vasyerp.rolebasedsystem.dto.UserRoleDTO;
import com.vasyerp.rolebasedsystem.model.UserRole;

import java.util.List;

public interface UserFrontService {
    AddressDTO addAddress(Long userFrontId, CreateAddressRequest request);

    List<AddressDTO> getAddresses(Long userFrontId);

    void deleteAddress(Long addressId);

    UserFrontDTO createCompany(Long userId, CreateUserFrontRequest request);

    UserFrontDTO createBranch(CreateUserFrontRequest request);

    List<UserFrontDTO> getAllCompanies();

    List<UserFrontDTO> getBranchesByCompany(Long companyId);

    List<UserFrontDTO> getCompaniesByUser(Long userId);

    UserFrontDTO getUserFrontById(Long userFrontId);

    UserFrontDTO updateUserFront(Long userFrontId, UpdateUserFrontRequest request);

    void deleteUserFront(Long userFrontId);

    UserRoleDTO assignRoleToUser(AssignUserRoleRequest request);

    UserRoleDTO revokeRoleFromUser(Long userFrontId, Long roleId);

    UserRoleDTO getUserRoles(Long userFrontId);

    List<UserRole> getAllRoles();

    boolean hasRole(Long userFrontId, String roleName);

    List<UserFrontDTO> findByCountry(String countryName);
}
