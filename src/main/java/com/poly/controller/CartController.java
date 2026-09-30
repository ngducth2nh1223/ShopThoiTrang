package com.poly.controller;

import com.poly.dao.ProductDAO;
import com.poly.entity.Product;
import com.poly.utils.CartItem;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductDAO productDAO;

    // Xem giỏ hàng
    @GetMapping("/view")
    public String viewCart() {
        return "cart"; // Trả về giao diện giỏ hàng (cart.jsp hoặc cart.html)
    }

    // Thêm sản phẩm vào giỏ hàng
    @GetMapping("/add")
    public String addToCart(@RequestParam("id") Integer productId, HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }

        // ProductDAO kế thừa JpaRepository trả về Optional<Product>
        Optional<Product> optionalProduct = productDAO.findById(productId);

        if (optionalProduct.isPresent()) {
            Product p = optionalProduct.get();
            if (cart.containsKey(productId)) {
                CartItem item = cart.get(productId);
                item.setQuantity(item.getQuantity() + 1);
            } else {
                cart.put(productId, new CartItem(p, 1));
            }
        }

        return "redirect:/cart/view";
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @GetMapping("/remove")
    public String removeFromCart(@RequestParam("id") Integer productId, HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");

        if (cart != null) {
            cart.remove(productId);
        }

        return "redirect:/cart/view";
    }
}