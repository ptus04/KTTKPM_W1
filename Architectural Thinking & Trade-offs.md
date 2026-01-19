# Architectural Thinking & Trade-offs

## 1. Xác định 7–10 architectural characteristics (availabilty, elasticity, performance, modifiability…)

- **Availability**: Hệ thống phải hoạt động liên tục
- **Security**: Bảo vệ thông tin, dữ liệu khách hàng, thanh toán
- **Performance**: Thời gian load trang dưới 2 giây, xử lý thanh toán nhanh
- **Scalability**: Xử lý được số lượng người truy cập lớn, đột ngột
- **Elasticity**: Tự động scale up/down resources theo traffic thực tế để tối ưu chi phí
- **Data Integrity**: Đảm bảo dữ liệu chính xác
- **Usability**: UI/UX đơn giản
- **Modifiability**: Dễ thêm tính năng mới
- **Testability**: Dễ viết test tự động

## 2. Utility Tree

### Availability (H, H)

- 99.9% uptime (H, M): Deploy multi-region
- Graceful degradation (M, H): Fallback khi service down

### Security (H, H)

- PCI DSS compliance (H, H): Encrypt payment data
- Authentication & Authorization (H, M): JWT, OAuth2

### Performance (H, M)

- Thời gian tải trang dưới 2 giây (H, H): CDN, caching
- Tốc độ phản hồi API dưới 200ms (M, H): Database indexing
- Xử lý cùng lúc được khoảng 1000 users (M, M): Load balancing

### Scalability (M, M)

- Horizontal scaling (M, H): Containerization
- Database sharding (L, M):

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
