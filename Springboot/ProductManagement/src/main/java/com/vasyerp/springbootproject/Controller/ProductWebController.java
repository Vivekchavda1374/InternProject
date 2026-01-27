package com.vasyerp.springbootproject.Controller;

import com.vasyerp.springbootproject.entity.Product;
import com.vasyerp.springbootproject.services.ProductSrvcs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
public class ProductWebController {

    private final ProductSrvcs productSrvcs;

    @Autowired
    public ProductWebController(ProductSrvcs productSrvcs) {
        this.productSrvcs = productSrvcs;
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String products(Model model, Authentication authentication) {
        model.addAttribute("products", productSrvcs.getAllProductList());
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        
        return "list-product";
    }

    @GetMapping("/products/list")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String listProducts(Model model, Authentication authentication) {
        model.addAttribute("products", productSrvcs.getAllProductList());
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        
        return "list-product";
    }

    @GetMapping("/products/showFormForAdd")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String showFormForAdd(Model model) {
        model.addAttribute("product", new Product());
        return "Product";
    }

    @PostMapping("/products/save")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productSrvcs.addProduct(product);
        return "redirect:/products/list";
    }

    @PostMapping("/products/showFormForUpdate")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String showFormForUpdate(@RequestParam("productId") Long id, Model model) {
        Product p = productSrvcs.searchProductById(id);
        model.addAttribute("product", p != null ? p : new Product());
        return "Product";
    }

    @PostMapping("/products/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProduct(@RequestParam("productId") Long id) {
        productSrvcs.deleteById(id);
        return "redirect:/products/list";
    }
}
