# Architectural Thinking & Trade-offs

## 1. Xác định 7–10 architectural characteristics (availabilty, elasticity, performance, modifiability…)

- **Availability**: Hệ thống phải hoạt động liên tục
  - Deploy nhiều container, sử dụng loadbalancer
  - Replicate cho database
  - Resilence4j để retry
- **Security**: Bảo vệ thông tin, dữ liệu khách hàng, thanh toán
  - Chỉ dùng HTTPS
  - Kiểm tra input và phòng tránh SQL Injection
  - Dùng tường lửa (WAF)
- **Performance**: Thời gian load trang dưới 2 giây, xử lý thanh toán nhanh
  - Dùng CDN cho các assets static
  - Dùng Redis cache
  - Tối ưu truy vấn SQL
  - Lazy load hình ảnh, nội dung khi chưa cuộn đến
- **Scalability**: Xử lý được số lượng người truy cập lớn, đột ngột
  - Dùng nhiều container
  - Dùng kiến trúc microservices
- **Elasticity**: Tự động scale up/down resources theo traffic thực tế để tối ưu chi phí
- **Deployability**: Tự động deploy sau khi pass các tests
  - Github actions, jenkins
- **Usability**: UI/UX đơn giản
  - Responsive
  - Đảm bảo tính nhất quán trong màu sắc, kích cỡ, ...
  - Accessibility
- **Modifiability**: Dễ dàng chỉnh sửa, thêm tính năng mới
  - Áp dụng tốt các architecture/design pattern
  - Đảm bảo các phụ thuộc được liên kết không quá chặt chẽ nhau
  - DI
- **Testability**: Dễ viết test tự động, integrate với CI/CD

## 2. Utility Tree

### Availability (H, H)

- 99.9% uptime (H, M): Deploy multi-region
- Graceful degradation (M, H): Fallback khi service down

### Security (H, H)

- PCI DSS compliance (H, H): Mã hóa dữ liệu thanh toán
- Authentication & Authorization (H, M): JWT, OAuth2

### Performance (H, M)

- Thời gian tải trang dưới 2 giây (H, H): CDN, caching
- Tốc độ phản hồi API dưới 200ms (M, H): Database indexing
- Xử lý cùng lúc được khoảng 1000 users (M, M): Load balancing

### Scalability (M, M)

- Horizontal scaling (M, H): Containerization
- Database sharding (L, M): Phân vùng theo region

### Elasticity (M, M)

- Auto-scale up khi traffic tăng (M, H): Kubernetes HPA
- Auto-scale down khi traffic giảm (M, M): Cost optimization

### Deployability (M, L)

- Automated CI/CD pipeline (M, L): GitHub Actions

### Usability (L, M)

- Mobile responsive (M, L): dùng các CSS framework
- Accessibility compliance (L, M): WCAG 2.1

### Modifiability (M, M)

- Add new payment method < 1 week (H, M): Plugin architecture

### Testability (M, L)

- Integration tests in CI (M, L): Test containers

*Chú thích:* (Độ quan trọng, Độ khó)

- H = Cao, M = Trung bình, L = Thấp

## Architectural Trade-offs

### 1. SQL vs NoSQL Database

| | SQL | NoSQL |
| --- | :---: | --- |
| ACID | x | |
| Quan hệ phức tạp | x | |
| Scaling dễ | | x |
| Flexible schema | | x |

**Decision**: Chọn SQL - Vì tính toàn vẹn dữ liệu (ACID) quan trọng hơn với e-commerce

### 2. Sync vs Async

| | Sync | Async |
| --- | :---: | --- |
| Đơn giản | x | |
| Hiệu năng tốt hơn | | x |
| Xử lý đồng thời | | x |
| Blocking | x | |
| Dễ debug | x | |

**Decision**: Async cho payment, Sync cho browsing products
