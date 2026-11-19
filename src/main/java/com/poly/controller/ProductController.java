package com.poly.controller;

import com.poly.entity.Color;
import com.poly.entity.Product;
import com.poly.entity.ProductImage;
import com.poly.entity.ProductVariant;
import com.poly.entity.Size;
import com.poly.repository.ProductRepository;
import com.poly.repository.ProductVariantRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    public ProductController(ProductRepository productRepository, ProductVariantRepository variantRepository) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
    }

    @GetMapping("/product/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Product product = productRepository.findById(id)
                .filter(p -> "ACTIVE".equalsIgnoreCase(p.getStatus()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Ảnh: ảnh chính lên đầu
        List<ProductImage> images = new ArrayList<>(product.getImages());
        images.sort(Comparator.comparing((ProductImage i) -> !Boolean.TRUE.equals(i.getMain())));

        // Biến thể, danh sách màu và size (không trùng)
        List<ProductVariant> variants = variantRepository.findByProduct_ProductIdAndStatus(id, "ACTIVE");
        TreeMap<Integer, Color> colors = new TreeMap<>();
        TreeMap<Integer, Size> sizes = new TreeMap<>();
        for (ProductVariant v : variants) {
            if (v.getColor() != null) colors.putIfAbsent(v.getColor().getColorId(), v.getColor());
            if (v.getSize() != null) sizes.putIfAbsent(v.getSize().getSizeId(), v.getSize());
        }

        BigDecimal min = variants.stream().map(ProductVariant::getPrice).filter(Objects::nonNull)
                .min(Comparator.naturalOrder()).orElse(product.getBasePrice());
        BigDecimal max = variants.stream().map(ProductVariant::getPrice).filter(Objects::nonNull)
                .max(Comparator.naturalOrder()).orElse(product.getBasePrice());

        model.addAttribute("product", product);
        model.addAttribute("images", images);
        model.addAttribute("mainImage", images.isEmpty() ? null : images.get(0).getImageUrl());
        model.addAttribute("variants", variants);
        model.addAttribute("colors", colors.values());
        model.addAttribute("sizes", sizes.values());
        model.addAttribute("minPrice", min);
        model.addAttribute("hasRange", min != null && max != null && min.compareTo(max) != 0);
        return "product-detail";
    }
}