CREATE DATABASE PHONE_STORE;
GO
USE PHONE_STORE;
GO

-- ================================
-- TABLE: Brand (Hãng điện thoại)
-- ================================
CREATE TABLE Brand (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL,
    Country NVARCHAR(100)
);
GO

-- ================================
-- TABLE: Phone (Sản phẩm điện thoại)
-- ================================
CREATE TABLE Phone (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(200) NOT NULL,
    Price FLOAT NOT NULL,
    Quantity INT NOT NULL,
    Image NVARCHAR(300),
    Description NVARCHAR(2000),
    Rom NVARCHAR(50),
    Ram NVARCHAR(50),
    Screen NVARCHAR(100),
    Battery NVARCHAR(100),
    BrandId INT NOT NULL,
    FOREIGN KEY (BrandId) REFERENCES Brand(Id)
);
GO

-- ================================
-- TABLE: Account (Người dùng)
-- ================================
CREATE TABLE Account (
    Username NVARCHAR(50) PRIMARY KEY,
    Password NVARCHAR(100) NOT NULL,
    Fullname NVARCHAR(100),
    Email NVARCHAR(100),
    Phone NVARCHAR(20),
    Role NVARCHAR(20) DEFAULT 'USER'
);
GO

-- ================================
-- TABLE: Orders (Đơn hàng)
-- ================================
CREATE TABLE Orders (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(50) NOT NULL,
    Address NVARCHAR(255),
    CreateDate DATE DEFAULT GETDATE(),
    Status NVARCHAR(50) DEFAULT 'Pending',
    FOREIGN KEY (Username) REFERENCES Account(Username)
);
GO

-- ================================
-- TABLE: OrderDetail (Chi tiết đơn)
-- ================================
CREATE TABLE OrderDetail (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    OrderId INT NOT NULL,
    PhoneId INT NOT NULL,
    Price FLOAT NOT NULL,
    Quantity INT NOT NULL,
    FOREIGN KEY (OrderId) REFERENCES Orders(Id),
    FOREIGN KEY (PhoneId) REFERENCES Phone(Id)
);
GO
INSERT INTO Brand (Name, Country) VALUES
('Apple', 'USA'),
('Samsung', 'Korea'),
('Xiaomi', 'China'),
('Oppo', 'China'),
('Vivo', 'China'),
('Realme', 'China'),
('Asus', 'Taiwan');
GO
INSERT INTO Phone 
(Name, Price, Quantity, Image, Description, Rom, Ram, Screen, Battery, BrandId)
VALUES
-- APPLE
('iPhone 15 Pro Max 256GB', 33990000, 20,
'iphone-15-pro-max-blue-thumb-600x600.jpg',
N'Flagship mới nhất của Apple với chip A17 Pro.',
'256GB', '8GB', '6.7"" OLED', '4422mAh', 1),

('iPhone 14 Pro 128GB', 24990000, 15,
'iphone-14-pro-thumb-xanh-600x600.jpg',
N'Camera 48MP, Dynamic Island.',
'128GB', '6GB', '6.1"" OLED', '3200mAh', 1),

-- SAMSUNG
('Samsung Galaxy S24 Ultra', 28990000, 20,
'samsung-galaxy-s24-ultra-titan-thumb-600x600.jpg',
N'Flagship cao cấp nhất của Samsung năm 2024.',
'256GB', '12GB', '6.8"" AMOLED 2X', '5000mAh', 2),

('Samsung Galaxy A54', 7990000, 40,
'samsung-galaxy-a54-5g-thumb-1-600x600.jpg',
N'Điện thoại tầm trung hot nhất 2024.',
'128GB', '8GB', '6.4"" Super AMOLED', '5000mAh', 2),

-- XIAOMI
('Xiaomi 13T Pro', 13990000, 30,
'xiaomi-13t-pro-blue-thumb-600x600.jpg',
N'Camera Leica, hiệu năng mạnh.',
'256GB', '12GB', '6.67"" AMOLED', '5000mAh', 3),

('Xiaomi Redmi Note 12', 4990000, 50,
'xiaomi-redmi-note-12-thumb-xanh-600x600.jpg',
N'Điện thoại phổ thông đáng mua nhất.',
'128GB', '6GB', '6.67"" AMOLED', '5000mAh', 3),

-- OPPO
('Oppo Reno10 Pro', 11990000, 25,
'oppo-reno10-pro-5g-thumb-600x600.jpg',
N'Máy đẹp, camera chân dung siêu nét.',
'256GB', '12GB', '6.7"" AMOLED', '4600mAh', 4),

-- VIVO
('Vivo V29 5G', 10490000, 22,
'vivo-v29-5g-thumb-xanh-600x600.jpg',
N'Selfie đẹp, thiết kế sang.',
'256GB', '8GB', '6.78"" AMOLED', '4600mAh', 5),

-- ASUS
('Asus ROG Phone 6', 19990000, 10,
'asus-rog-phone-6-thumb-600x600.jpg',
N'Gaming phone mạnh nhất phân khúc.',
'512GB', '16GB', '6.78"" AMOLED', '6000mAh', 7);
GO

GO
INSERT INTO Account (Username, Password, Fullname, Email, Phone, Role) VALUES
('admin', '123456', 'Administrator', 'admin@store.com', '0900000000', 'ADMIN'),
('user1', '123456', 'Nguyen Van A', 'user1@gmail.com', '0911111111', 'USER');
GO
-- Đơn hàng mẫu cho user1
INSERT INTO Orders (Username, Address, CreateDate, Status)
VALUES 
('user1', N'123 Lý Thường Kiệt, Hà Nội', GETDATE(), N'Completed');
GO

-- Lấy Id đơn hàng vừa tạo
-- (Nếu dùng SSMS, có thể chạy riêng lệnh này để xem)
SELECT * FROM Orders;

-- Giả sử Id đơn hàng là 1, thêm chi tiết:
INSERT INTO OrderDetail (OrderId, PhoneId, Price, Quantity)
VALUES
(1, 1, 33990000, 1), -- iPhone 15 Pro Max
(1, 3, 28990000, 1); -- Samsung S24 Ultra
GO
