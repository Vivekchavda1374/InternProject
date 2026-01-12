package com.vasyerp.springbootproject.Controller;

import com.vasyerp.springbootproject.entity.Product;
import com.vasyerp.springbootproject.services.ProductSrvcs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductWebController {

    private final ProductSrvcs productSrvcs;

    @Autowired
    public ProductWebController(ProductSrvcs productSrvcs) {
        this.productSrvcs = productSrvcs;
    }

    @GetMapping("/products/list")
    public String listProducts(Model model) {
        model.addAttribute("products", productSrvcs.getAllProductList());
        return "list-product";
    }

    @GetMapping("/products/showFormForAdd")
    public String showFormForAdd(Model model) {
        model.addAttribute("product", new Product());
        return "Product";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute("product") Product product) {
        productSrvcs.addProduct(product);
        return "redirect:/products/list";
    }

    @PostMapping("/products/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("productId") Long id, Model model) {
        Product p = productSrvcs.searchProductById(id);
        model.addAttribute("product", p != null ? p : new Product());
        return "Product";
    }

    @PostMapping("/products/delete")
    public String deleteProduct(@RequestParam("productId") Long id) {
        productSrvcs.deleteById(id);
        return "redirect:/products/list";
    }
}
