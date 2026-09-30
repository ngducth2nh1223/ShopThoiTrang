package com.poly.utils;

import com.poly.entity.Product;

public class CartItem {

    private Product product;
    private int quantity;

    public CartItem() {
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Tính tổng tiền của sản phẩm sau khi giảm giá
    public double getTotalPrice() {

        if (product == null) {
            return 0.0;
        }

        double discountRate = (product.getDiscount() != null)
                ? product.getDiscount()
                : 0.0;

        double price = (product.getPrice() != null)
                ? product.getPrice().doubleValue()
                : 0.0;

        double finalPrice = price * (1.0 - discountRate);

        return finalPrice * quantity;
    }
}