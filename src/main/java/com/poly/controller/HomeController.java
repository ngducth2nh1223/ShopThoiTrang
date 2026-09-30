package com.poly.controller;

import java.util.List;

import com.poly.dao.CategoryDAO;
import com.poly.dao.ProductDAO;
import com.poly.entity.Category;
import com.poly.entity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @GetMapping({"/", "/home"})
    public String home(
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            Model model) {

        List<Product> productList;

        // Kiểm tra xem người dùng có chọn danh mục hay không
        if (categoryId != null) {
            productList = productDAO.findByCategoryIdAndAvailableTrue(categoryId);
        } else {
            productList = productDAO.findByAvailableTrue();
        }

        List<Category> categoryList = categoryDAO.findAll();

        // Đưa dữ liệu sang View
        model.addAttribute("products", productList);
        model.addAttribute("categories", categoryList);

        return "home"; // Trả về file giao diện (home.html hoặc home.jsp)
    }
}