package com.poly.controller;

import com.poly.dao.UserDAO;
import com.poly.entity.User;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserDAO userDAO;

    // Hiển thị trang đăng nhập (GET)
    @GetMapping
    public String showLoginForm() {
        return "login"; // Trả về file giao diện (login.html hoặc login.jsp)
    }

    // Xử lý thông tin đăng nhập (POST)
    @PostMapping
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        // Kiểm tra thông tin người dùng từ DAO
        User user = userDAO.findByUsernameAndPassword(username, password);

        if (user != null) {
            // Lưu thông tin user vào Session
            session.setAttribute("user", user);

            // Điều hướng theo vai trò (Role)
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/home";
            }
        } else {
            // Đăng nhập thất bại -> Gửi thông báo lỗi về View
            model.addAttribute("error", "Tài khoản hoặc mật khẩu không chính xác!");
            return "login";
        }
    }
}