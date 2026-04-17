# Dự án Spring Boot

Đây là dự án Spring Boot phát triển hệ thống với cơ chế xác thực và phân quyền (Authentication & Authorization) bảo mật, sử dụng JWT (JSON Web Token) kết hợp Refresh Token, cùng giao diện front-end tĩnh được tích hợp sẵn.

## Tính năng nổi bật

### 1. Xác thực & Phân quyền (Security)
- **JWT Authentication:** Cơ chế đăng nhập an toàn với Access Token và Refresh Token.
- **Refresh Token Lifecycle:** Tự động cấp lại token mới và thu hồi token cũ khi người dùng đăng xuất, lưu trữ an toàn dưới cơ sở dữ liệu.
- **Role-Based Access Control (RBAC):** Phân quyền người dùng theo vai trò (`USER`, `ADMIN`).
- **Global Exception Handling:** Quản lý ngoại lệ tập trung, trả về định dạng response chuẩn (ví dụ: HTTP 409 khi đăng ký trùng lặp).

### 2. Quản lý Người Dùng (User Management)
- Đăng ký tài khoản với tính năng kiểm tra dữ liệu đầu vào.
- Đăng nhập, đăng xuất và xem thông tin cá nhân.
- (Dành cho Admin): Xem danh sách người dùng trong hệ thống.

### 3. Giao Diện Frontend Tích Hợp
- Trình bày tĩnh qua HTML/CSS/JS Vanilla.
- **Dynamic UI:** Giao diện thay đổi linh hoạt theo trạng thái (chưa đăng nhập/đã đăng nhập) và theo vai trò (hiển thị menu Admin cho người có quyền).
- Tương tác với backend thông qua `Fetch API` và quản lý token bằng `localStorage`.

## 🛠 Công nghệ sử dụng

- **Backend:** Java 17+, Spring Boot, Spring Security, Spring Data JPA, Hibernate.
- **Database:** MySQL / PostgreSQL (Tùy cấu hình).
- **Frontend:** HTML5, CSS3, JavaScript (Vanilla).
- **Công cụ:** Gradle, Lombok, MapStruct (nếu có).

## Cấu trúc dự án cơ bản

```
src/
├── main/
│   ├── java/va/project/
│   │   ├── controller/      # Nơi chứa các REST APIs
│   │   ├── service/         # Xử lý logic nghiệp vụ, Token, Auth
│   │   ├── security/        # Cấu hình Spring Security, JWT Filter
│   │   ├── dto/             # Các lớp Data Transfer Object
│   │   └── exception/       # Xử lý lỗi tập trung
│   └── resources/
│       ├── application.properties  # Cấu hình ứng dụng, Database, JWT Secret
│       └── static/          # Chứa các file HTML, CSS, JS
│           ├── index.html   # Trang chủ
│           ├── login.html   # Trang đăng nhập
│           ├── register.html# Trang đăng ký
│           └── css/         # Thư mục chứa các file CSS (index.css, login.css...)
```

## 🔗 Các API Endpoints chính

| Method | Endpoint                 | Mô tả                                      | Yêu cầu Auth |
|--------|--------------------------|--------------------------------------------|--------------|
| POST   | `/api/v1/auth/register`  | Đăng ký tài khoản mới                      | Không        |
| POST   | `/api/v1/auth/login`     | Đăng nhập, nhận Access/Refresh Token       | Không        |
| POST   | `/api/v1/auth/logout`    | Đăng xuất, vô hiệu hóa Refresh Token       | Có           |
| GET    | `/api/v1/user/me`        | Lấy thông tin cá nhân của người dùng       | Có           |
| GET    | `/api/v1/admin/users`    | Lấy danh sách toàn bộ người dùng           | Có (Admin)   |

## ⚙️ Hướng dẫn cài đặt và chạy dự án

1. **Clone repository:**
   ```bash
   git clone https://github.com/vanh251/Spring_Boot_Project
   ```

2. **Cấu hình Cơ sở dữ liệu:**
   - Đảm bảo bạn đã cài đặt hệ quản trị cơ sở dữ liệu postgresql.
   - Mở `src/main/resources/application.properties và cập nhật thông tin kết nối database (URL, Username, Password).

3. **Chạy ứng dụng:**
   Sử dụng Gradle wrapper để chạy:
   ```bash
   ./gradlew bootRun
   ```

4. **Truy cập:**
   - Ứng dụng sẽ chạy tại: `http://localhost:8080/`
   - Giao diện Frontend được load mặc định ở trang chủ (`index.html`).

## ✍️ Tác giả
- Phát triển bởi: Nguyễn Việt Anh
- Dự án: TTCS (Thực Tập Cơ Sở)
