create database ShopThoiTrang
USE ShopThoiTrang;
GO

-- =========================
-- 1. USERS
-- =========================
CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fullname NVARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address NVARCHAR(255),
    photo VARCHAR(255) DEFAULT 'default-user.png',
    activated BIT DEFAULT 1,
    role VARCHAR(20) DEFAULT 'CUSTOMER'
);
GO

-- =========================
-- 2. CATEGORIES
-- =========================
CREATE TABLE Categories (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE
);
GO

-- =========================
-- 3. PRODUCTS
-- =========================
CREATE TABLE Products (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(200) NOT NULL,
    image VARCHAR(255) DEFAULT 'default-product.png',
    price DECIMAL(12,2) NOT NULL,
    discount FLOAT DEFAULT 0.0,
    quantity INT NOT NULL DEFAULT 0,
    size VARCHAR(20) DEFAULT 'M',
    description NVARCHAR(MAX),
    created_date DATETIME DEFAULT GETDATE(),
    available BIT DEFAULT 1,
    category_id INT NOT NULL,

    CONSTRAINT FK_Products_Categories
        FOREIGN KEY (category_id)
        REFERENCES Categories(id)
        ON DELETE CASCADE
);
GO

-- =========================
-- 4. ORDERS
-- =========================
CREATE TABLE Orders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    created_date DATETIME DEFAULT GETDATE(),
    address NVARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    note NVARCHAR(255),
    status VARCHAR(30) DEFAULT 'PENDING',

    CONSTRAINT FK_Orders_Users
        FOREIGN KEY (user_id)
        REFERENCES Users(id)
        ON DELETE CASCADE
);
GO

-- =========================
-- 5. ORDER DETAILS
-- =========================
CREATE TABLE OrderDetails (
    id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    quantity INT NOT NULL,

    CONSTRAINT FK_OrderDetails_Orders
        FOREIGN KEY (order_id)
        REFERENCES Orders(id)
        ON DELETE CASCADE,

    CONSTRAINT FK_OrderDetails_Products
        FOREIGN KEY (product_id)
        REFERENCES Products(id)
        ON DELETE CASCADE
);
GO
USE ShopThoiTrang;
GO

-- =========================================================
-- 1. THÊM ADMIN & KHÁCH HÀNG
-- =========================================================

INSERT INTO Users
(username, password, fullname, email, phone, address, photo, activated, role)
VALUES
('duytran', '123456', N'Duy Tr?n',
 'duytran@fashionshop.com', '0901111111',
 N'TP. H? Chí Minh', 'duytran.jpg', 1, 'ADMIN'),

('hoducqui', '123456', N'H? ??c Quí',
 'ducqui@fashionshop.com', '0902222222',
 N'TP. H? Chí Minh', 'ducqui.jpg', 1, 'ADMIN'),

('nguyenhuuhau', '123456', N'Nguy?n H?u H?u',
 'huuhau@fashionshop.com', '0903333333',
 N'TP. H? Chí Minh', 'huuhau.jpg', 1, 'ADMIN'),

('Nguyenducthanh', '123456', N'Nguy?n ??c Thành',
 'nguyenducthanh@gmail.com', '0396180060',
 N'TP. H? Chí Minh', 'nguyenducthanh.jpg', 1, 'ADMIN'),

('lethiba', '123456', N'Lê Th? Ba',
 'lethiba@gmail.com', '0981234567',
 N'Qu?n 1, TP.HCM', 'user1.jpg', 1, 'CUSTOMER'),

('tranhung', '123456', N'Tr?n V?n Hùng',
 'tranhung@gmail.com', '0972345678',
 N'Qu?n 7, TP.HCM', 'user2.jpg', 1, 'CUSTOMER'),

('phamminh', '123456', N'Ph?m Minh Tu?n',
 'phamtuan@gmail.com', '0963456789',
 N'Qu?n Bình Th?nh, TP.HCM', 'user3.jpg', 1, 'CUSTOMER');
GO


-- =========================================================
-- 2. THÊM DANH M?C
-- =========================================================

INSERT INTO Categories (name)
VALUES
(N'Áo Nam'),
(N'Áo N?'),
(N'Qu?n Nam'),
(N'Qu?n N?'),
(N'Ph? Ki?n');
GO


-- =========================================================
-- 3. THÊM S?N PH?M
-- =========================================================

INSERT INTO Products
(name, image, price, discount, quantity, size, description, category_id)
VALUES

-- =========================
-- ÁO NAM - category_id = 1
-- =========================

(N'Áo S? Mi Tr?ng Nam',
 'somi-trang.jpg',
 350000, 0.10, 50, 'L',
 N'Áo s? mi nam phom r?ng ch?t li?u cotton thoáng mát',
 1),

(N'Áo Polo Nam Th? Thao Co Giãn',
 'polo-nam-01.jpg',
 290000, 0.05, 45, 'L',
 N'Áo polo nam ch?t li?u v?i pima thoáng mát',
 1),

(N'Áo Khoác Bomber Nam Phong Cách',
 'bomber-nam-01.jpg',
 650000, 0.15, 20, 'XL',
 N'Áo khoác bomber 2 l?p gi? ?m t?t',
 1),

(N'Áo Hoodie Nam Form Wide Fit',
 'hoodie-nam-01.jpg',
 420000, 0.10, 35, 'L',
 N'Áo hoodie ch?t n? ngo?i cao c?p',
 1),


-- =========================
-- ÁO N? - category_id = 2
-- =========================

(N'Áo S? Mi N? Công S? L?a C? V',
 'somi-nu-01.jpg',
 320000, 0.10, 50, 'S',
 N'Áo s? mi l?a m?m m?i tôn dáng',
 2),

(N'Áo Baby Doll N? D? Th??ng',
 'babydoll-01.jpg',
 250000, 0.00, 60, 'M',
 N'Áo ki?u baby doll ch?t xô ?ôi thoáng mát',
 2),

(N'Váy Xòe N? Dáng Dài',
 'vay-xoe-nu.jpg',
 500000, 0.20, 25, 'S',
 N'Váy n? thanh l?ch thích h?p ?i ch?i',
 2),


-- =========================
-- QU?N NAM - category_id = 3
-- =========================

(N'Qu?n Jean Nam Co Giãn',
 'quan-jean-nam.jpg',
 450000, 0.05, 30, 'XL',
 N'Qu?n jean phong cách Hàn Qu?c',
 3),

(N'Qu?n Tây Nam L?ch Lãm Form Slimfit',
 'quantay-nam-01.jpg',
 480000, 0.10, 40, 'L',
 N'Qu?n tây ch?t li?u cao c?p ch?ng nh?n',
 3),


-- =========================
-- QU?N N? - category_id = 4
-- =========================

(N'Qu?n Jean N? C?p Cao ?ng R?ng',
 'jean-ongrong-nu.jpg',
 420000, 0.10, 50, 'M',
 N'Qu?n jean ?ng r?ng hack dáng',
 4),

(N'Chân Váy X?p Ly Dáng Dài',
 'chanvay-xepli.jpg',
 310000, 0.00, 35, 'M',
 N'Chân váy x?p ly phong cách n? tính',
 4),


-- =========================
-- PH? KI?N - category_id = 5
-- =========================

(N'M? Nón L??i Trai Unisex Canvas',
 'non-luoitrai.jpg',
 120000, 0.00, 100, 'FreeSize',
 N'Nón l??i trai ch?t li?u v?i canvas cao c?p',
 5),

(N'Th?t L?ng Nam Da Bò Th?t',
 'thatlung-nam.jpg',
 350000, 0.20, 45, 'FreeSize',
 N'Th?t l?ng da nam nguyên mi?ng',
 5);
GO
