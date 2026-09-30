package com.poly.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.poly.dao.OrderDAO;
import com.poly.entity.Order;
import com.poly.entity.OrderDetail;
import com.poly.entity.User;
import com.poly.utils.CartItem;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CheckoutController {

    @Autowired
    private OrderDAO orderDAO;

    @PostMapping("/checkout")
    public String checkout(
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            @RequestParam(value = "note", required = false) String note,
            HttpSession session,
            Model model) {

        // 1. Kiểm tra đăng nhập
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        // 2. Lấy giỏ hàng
        @SuppressWarnings("unchecked")
        Map<Integer, CartItem> cart =
                (Map<Integer, CartItem>) session.getAttribute("cart");

        // 3. Kiểm tra giỏ hàng
        if (cart == null || cart.isEmpty()) {
            return "redirect:/home";
        }

        // 4. Tạo Order
        Order order = new Order();

        order.setUser(user);
        order.setAddress(address);
        order.setPhone(phone);
        order.setNote(note);
        order.setCreatedDate(new Date());
        order.setStatus("PENDING");

        // 5. Tạo danh sách OrderDetail
        List<OrderDetail> details = new ArrayList<>();

        for (CartItem item : cart.values()) {

            if (item.getProduct() == null) {
                continue;
            }

            OrderDetail detail = new OrderDetail();

            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());

            // Giá gốc
            BigDecimal originalPrice = item.getProduct().getPrice();

            if (originalPrice == null) {
                originalPrice = BigDecimal.ZERO;
            }

            // Phần trăm giảm giá
            Double discount = item.getProduct().getDiscount();

            if (discount == null) {
                discount = 0.0;
            }

            // Tính giá sau giảm
            BigDecimal discountRate = BigDecimal.valueOf(discount);

            BigDecimal priceAfterDiscount = originalPrice
                    .multiply(BigDecimal.ONE.subtract(discountRate))
                    .setScale(2, RoundingMode.HALF_UP);

            detail.setPrice(priceAfterDiscount);

            details.add(detail);
        }

        // Không có sản phẩm hợp lệ
        if (details.isEmpty()) {
            model.addAttribute("error", "Giỏ hàng không có sản phẩm hợp lệ!");
            return "cart";
        }

        // 6. Gán OrderDetails cho Order
        order.setOrderDetails(details);

        // 7. Lưu đơn hàng
        try {

            orderDAO.save(order);

            // 8. Xóa giỏ hàng sau khi đặt hàng thành công
            session.removeAttribute("cart");

            model.addAttribute("message", "Đặt hàng thành công!");

            return "checkout-success";

        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute(
                    "error",
                    "Có lỗi xảy ra khi đặt hàng: " + e.getMessage()
            );

            return "cart";
        }
    }
}