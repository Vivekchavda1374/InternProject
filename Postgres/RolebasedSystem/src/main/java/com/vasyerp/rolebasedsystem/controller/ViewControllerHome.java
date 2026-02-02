package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.service.ProductService;
import com.vasyerp.rolebasedsystem.service.UserFrontService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewControllerHome {

    private final ProductService productService;
    private final UserFrontService userFrontService;

    public ViewControllerHome(ProductService productService, UserFrontService userFrontService) {
        this.productService = productService;
        this.userFrontService = userFrontService;
    }

    @GetMapping("/")
    public String home() {
        return "hello";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/products")
    public String products(Model model, HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId != null) {
                model.addAttribute("products", productService.getProductsByCompany(userId, userId));
            } else {
                model.addAttribute("products", List.of());
            }
        } catch (Exception e) {
            model.addAttribute("products", List.of());
            model.addAttribute("error", "Error loading products: " + e.getMessage());
        }
        return "products";
    }

    @GetMapping("/users")
    public String users(Model model) {
        try {
            model.addAttribute("users", userFrontService.getAllCompanies());
        } catch (Exception e) {
            model.addAttribute("users", List.of());
            model.addAttribute("error", "Error loading users: " + e.getMessage());
        }
        return "users";
    }

    @GetMapping("/roles")
    public String roles(Model model) {
        try {
            model.addAttribute("roles", userFrontService.getAllRoles());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("roles", List.of());
            model.addAttribute("error", "Error loading roles: " + e.getMessage());
        }
        return "roles";
    }

    @GetMapping("/sales")
    public String sales(Model model) {
        try {
            model.addAttribute("sales", List.of());
            model.addAttribute("salesItems", List.of());
        } catch (Exception e) {
            model.addAttribute("sales", List.of());
            model.addAttribute("error", "Error loading sales: " + e.getMessage());
        }
        return "sales";
    }

    @GetMapping("/purchases")
    public String purchases(Model model) {
        try {
            model.addAttribute("purchases", List.of());
            model.addAttribute("purchaseItems", List.of());
            model.addAttribute("suppliers", List.of());
        } catch (Exception e) {
            model.addAttribute("purchases", List.of());
            model.addAttribute("error", "Error loading purchases: " + e.getMessage());
        }
        return "purchases";
    }

    @GetMapping("/branches")
    public String branches() {
        return "branches";
    }
}
