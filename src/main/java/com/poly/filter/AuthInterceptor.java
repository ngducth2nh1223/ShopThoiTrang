package com.poly.filter;

import com.poly.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String uri = request.getRequestURI();

        // 1. Nếu chưa đăng nhập mà truy cập trang yêu cầu đăng nhập
        if (user == null) {
            session.setAttribute("security-uri", uri);
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        // 2. Nếu không phải ADMIN mà cố tình vào trang Admin
        if (uri.startsWith(request.getContextPath() + "/admin") && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login?error=access-denied");
            return false;
        }

        return true;
    }
}