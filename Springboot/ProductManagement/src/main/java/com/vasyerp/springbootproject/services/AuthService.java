package com.vasyerp.springbootproject.services;

import com.vasyerp.springbootproject.entity.Role;
import com.vasyerp.springbootproject.entity.User;
import com.vasyerp.springbootproject.repository.RoleRepository;
import com.vasyerp.springbootproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthService(UserRepo userRepo, RoleRepository roleRepository) {
        this.userRepo = userRepo;
        this.roleRepository = roleRepository;
    }

    public User registerUser(String company, String branch, String username, String password) {
        User user = new User();
        user.setCompany(company);
        user.setBranch(branch);
        user.setUsername(username);
        user.setPassword(password);
        
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> roleRepository.save(new Role(null, "USER")));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        return userRepo.save(user);
    }

    public User registerAdmin(String company, String branch, String username, String password) {
        User user = new User();
        user.setCompany(company);
        user.setBranch(branch);
        user.setUsername(username);
        user.setPassword(password);
        
        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);

        return userRepo.save(user);
    }
}
