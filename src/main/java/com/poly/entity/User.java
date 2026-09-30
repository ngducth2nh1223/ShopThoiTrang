package com.poly.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String fullname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    private String phone;
    private String address;
    private String photo = "default-user.png";
    private Boolean activated = true;

    @Column(length = 20)
    private String role = "CUSTOMER";

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    public User() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
    public Boolean getActivated() { return activated; }
    public void setActivated(Boolean activated) { this.activated = activated; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}