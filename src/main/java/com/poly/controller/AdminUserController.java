package com.poly.controller;

import com.poly.entity.User;
import com.poly.repository.UserRepository;
import com.poly.repository.RoleRepository;
import com.poly.entity.Role;
import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminUserController(UserRepository userRepository, RoleRepository roleRepository) { this.userRepository = userRepository; this.roleRepository = roleRepository; }

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String keyword, Model model) {
        model.addAttribute("users", userRepository.searchUsers(keyword.trim()));
        var allRoles = roleRepository.findAll();
        model.addAttribute("roles", allRoles.stream().filter(r -> "CUSTOMER".equalsIgnoreCase(r.getRoleName()) || "EMPLOYEE".equalsIgnoreCase(r.getRoleName())).toList());
        model.addAttribute("roleNames", allRoles.stream().collect(Collectors.toMap(Role::getRoleId, Role::getRoleName)));
        model.addAttribute("keyword", keyword);
        return "admin/users";
    }


    @PostMapping("/{id}/role")
    public String changeRole(@PathVariable Integer id, @RequestParam String roleName,
                             Principal principal, RedirectAttributes redirect) {
        User user = userRepository.findById(id).orElse(null);
        String requested = roleName == null ? "" : roleName.trim().toUpperCase();
        if (user == null) {
            redirect.addFlashAttribute("adminError", "Không tìm thấy tài khoản.");
        } else if ("ADMIN".equalsIgnoreCase(roleRepository.findById(user.getRoleId()).map(Role::getRoleName).orElse(""))) {
            redirect.addFlashAttribute("adminError", "Không thể thay đổi quyền của tài khoản Admin.");
        } else if (!requested.equals("EMPLOYEE") && !requested.equals("CUSTOMER")) {
            redirect.addFlashAttribute("adminError", "Quyền không hợp lệ.");
        } else {
            Role role = roleRepository.findByRoleName(requested).orElse(null);
            if (role == null) redirect.addFlashAttribute("adminError", "Database chưa có quyền " + requested + ".");
            else {
                user.setRoleId(role.getRoleId());
                userRepository.save(user);
                redirect.addFlashAttribute("adminMessage", "Đã cập nhật quyền người dùng.");
            }
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Integer id, Principal principal, RedirectAttributes redirect) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            redirect.addFlashAttribute("adminError", "Không tìm thấy tài khoản.");
        } else if ("ADMIN".equalsIgnoreCase(roleRepository.findById(user.getRoleId()).map(Role::getRoleName).orElse(""))) {
            redirect.addFlashAttribute("adminError", "Không thể khóa tài khoản Admin.");
        } else if (principal != null && principal.getName().equalsIgnoreCase(user.getEmail())) {
            redirect.addFlashAttribute("adminError", "Bạn không thể tự khóa tài khoản đang đăng nhập.");
        } else {
            String current = user.getStatus();
            user.setStatus("ACTIVE".equalsIgnoreCase(current) ? "INACTIVE" : "ACTIVE");
            userRepository.save(user);
            redirect.addFlashAttribute("adminMessage", "Đã cập nhật trạng thái tài khoản.");
        }
        return "redirect:/admin/users";
    }
}
