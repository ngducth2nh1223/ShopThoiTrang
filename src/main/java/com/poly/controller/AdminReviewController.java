package com.poly.controller;

import com.poly.entity.Review;
import com.poly.repository.ReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {
    private final ReviewRepository reviewRepository;
    public AdminReviewController(ReviewRepository reviewRepository) { this.reviewRepository = reviewRepository; }

    @GetMapping
    public String list(@RequestParam(defaultValue = "") String status,
                       @RequestParam(defaultValue = "0") Integer rating, Model model) {
        model.addAttribute("reviews", reviewRepository.search(status, rating));
        model.addAttribute("status", status);
        model.addAttribute("rating", rating);
        return "admin/reviews";
    }

    @PostMapping("/{id}/visibility")
    public String toggle(@PathVariable Integer id, RedirectAttributes redirect) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) redirect.addFlashAttribute("adminError", "Không tìm thấy đánh giá.");
        else {
            review.setStatus("ACTIVE".equalsIgnoreCase(review.getStatus()) ? "HIDDEN" : "ACTIVE");
            reviewRepository.save(review);
            redirect.addFlashAttribute("adminMessage", "Đã cập nhật trạng thái đánh giá.");
        }
        return "redirect:/admin/reviews";
    }
}
