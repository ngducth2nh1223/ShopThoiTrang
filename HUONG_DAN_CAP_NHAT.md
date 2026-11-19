# Cập nhật FashionStore - thống kê, người dùng, đánh giá

## Các thay đổi
- `/admin/statistics`: bỏ tổng đơn hàng, thay bằng đơn hàng cần xử lý; lọc từ ngày/đến ngày; biểu đồ doanh thu theo ngày và sản phẩm bán chạy.
- `/admin/users`: giữ tìm kiếm và khóa/mở khóa; bổ sung đổi quyền CUSTOMER/EMPLOYEE. Admin không hiển thị nút đổi quyền và backend chặn khóa/đổi quyền Admin.
- `/admin/reviews`: xem đánh giá, lọc trạng thái/số sao, ẩn/hiện đánh giá.

## SQL cần chạy
Mở SSMS, chọn database `FashionStoreDB`, chạy file `database/insert_employee_role.sql` để thêm quyền EMPLOYEE nếu chưa tồn tại.

## Lưu ý quan trọng
- Doanh thu hiện tính từ các đơn `COMPLETED`, dùng `finalAmount` và ngày `createAt`.
- Đơn cần xử lý hiện tính các trạng thái `PENDING`, `CONFIRMED`, `PREPARING`, `SHIPPING`.
- Trạng thái đánh giá dùng `ACTIVE` để hiện và `HIDDEN` để ẩn.
- Biểu đồ tải Chart.js từ CDN, máy chạy ứng dụng cần có Internet.
- Bản ZIP gốc được dùng là `FashionStore-bo-sung-tim-kiem-thong-ke-user.zip` (bản cập nhật 03/11/2026). Cần đối chiếu tên cột thực tế trong SQL Server trước khi chạy trên dữ liệu thật.
- Chưa thể chạy Maven build trong môi trường đóng gói này vì không có lệnh `mvn`; hãy chạy `Maven > Update Project` rồi `Run As > Maven build...` trong Eclipse để kiểm tra.
