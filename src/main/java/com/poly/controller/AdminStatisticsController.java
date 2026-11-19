package com.poly.controller;

import com.poly.repository.OrdersRepository;
import com.poly.repository.ProductRepository;
import com.poly.repository.UserRepository;
import com.poly.util.OrderLabels;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/statistics")
public class AdminStatisticsController {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;

    public AdminStatisticsController(ProductRepository productRepository, UserRepository userRepository, OrdersRepository ordersRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.ordersRepository = ordersRepository;
    }

    @GetMapping
    public String index(@RequestParam(required = false) String fromDate,
                        @RequestParam(required = false) String toDate,
                        Model model) {
        LocalDate to = (toDate == null || toDate.isBlank()) ? LocalDate.now() : LocalDate.parse(toDate);
        LocalDate from = (fromDate == null || fromDate.isBlank()) ? to.withDayOfMonth(1) : LocalDate.parse(fromDate);
        if (from.isAfter(to)) { LocalDate temp = from; from = to; to = temp; }
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.plusDays(1).atStartOfDay();

        model.addAttribute("productCount", productRepository.count());
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("pendingCount", ordersRepository.countByOrderStatusIn(List.of("PENDING", "CONFIRMED", "PREPARING", "SHIPPING")));
        model.addAttribute("completedCount", ordersRepository.countByOrderStatus("COMPLETED"));
        model.addAttribute("cancelledCount", ordersRepository.countByOrderStatus("CANCELLED"));
        BigDecimal revenue = ordersRepository.revenueBetween(start, end);
        model.addAttribute("revenue", revenue == null ? BigDecimal.ZERO : revenue);
        model.addAttribute("fromDate", from.toString());
        model.addAttribute("toDate", to.toString());

        List<String> dayLabels = new ArrayList<>();
        List<String> revenueValues = new ArrayList<>();
        for (Object[] row : ordersRepository.dailyRevenue(start, end)) {
            dayLabels.add(String.valueOf(row[0]));
            revenueValues.add(String.valueOf(row[1]));
        }
        List<String> productLabels = new ArrayList<>();
        List<String> productValues = new ArrayList<>();
        for (Object[] row : ordersRepository.bestSellingProducts(start, end, PageRequest.of(0, 8))) {
            productLabels.add(String.valueOf(row[0]));
            productValues.add(String.valueOf(row[1]));
        }
        model.addAttribute("dayLabels", dayLabels);
        model.addAttribute("revenueValues", revenueValues);
        model.addAttribute("bestProductLabels", productLabels);
        model.addAttribute("bestProductValues", productValues);
        model.addAttribute("orderStatusLabels", OrderLabels.ORDER_STATUS);
        return "admin/statistics";
    }
}
