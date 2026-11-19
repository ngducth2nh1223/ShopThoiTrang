package com.poly.controller;

import com.poly.repository.CategoryRepository;
import com.poly.repository.ProductRepository;
import java.math.BigDecimal;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public HomeController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String home(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) BigDecimal minPrice,
                       @RequestParam(required = false) BigDecimal maxPrice,
                       Model model, Principal principal) {
        model.addAttribute("categories", categoryRepository.findByParentCategoryIdIsNullAndStatus("ACTIVE"));
        boolean searching = (keyword != null && !keyword.isBlank()) || minPrice != null || maxPrice != null;
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            model.addAttribute("searchError", "Giá thấp nhất không được lớn hơn giá cao nhất.");
            model.addAttribute("products", java.util.List.of());
        } else if (searching) {
            model.addAttribute("products", productRepository.searchActiveProducts(keyword == null ? "" : keyword.trim(), minPrice, maxPrice));
        } else {
            model.addAttribute("products", productRepository.findTop8ByStatusOrderByCreatedAtDesc("ACTIVE"));
        }
        model.addAttribute("title", searching ? "Kết quả tìm kiếm" : "Sản phẩm mới nhất");
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("username", principal != null ? principal.getName() : null);
        return "home";
    }
}
