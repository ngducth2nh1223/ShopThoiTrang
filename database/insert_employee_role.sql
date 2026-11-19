-- Chạy trong database FashionStoreDB nếu ROLE chưa có quyền EMPLOYEE.
IF NOT EXISTS (SELECT 1 FROM dbo.ROLE WHERE roleName = 'EMPLOYEE')
BEGIN
    INSERT INTO dbo.ROLE (roleName, description)
    VALUES ('EMPLOYEE', N'Nhân viên cửa hàng');
END;
GO
