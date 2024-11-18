package fpl.md37.genz_fashion.models;

import java.util.HashMap;

public class Client {
    private String password;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private String address;

    // Phương thức chuyển đổi đối tượng thành HashMap
    public HashMap<String, Object> convertHashMap() {
        HashMap<String, Object> clientMap = new HashMap<>();
        clientMap.put("name", name);
        clientMap.put("email", email);
        clientMap.put("password", password);
        clientMap.put("phone", phone);
        clientMap.put("avatar", avatar);
        clientMap.put("address", address);
        return clientMap;
    }

    // Khởi tạo mặc định
    public Client() {}

    // Khởi tạo với tất cả các tham số
    public Client(String address, String avatar, String phone, String email, String name, String password) {
        this.address = address;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    // Getter và Setter với kiểm tra hợp lệ
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && password.length() >= 6) {  // Kiểm tra mật khẩu có đủ độ dài
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && name.length() > 2) {  // Kiểm tra tên có ít nhất 3 ký tự
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name must be at least 3 characters");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@")) {  // Kiểm tra email có hợp lệ không
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone != null && phone.length() > 9) {  // Kiểm tra số điện thoại có đủ độ dài
            this.phone = phone;
        } else {
            throw new IllegalArgumentException("Phone number must be at least 10 digits");
        }
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address != null && address.length() > 5) {  // Kiểm tra địa chỉ có ít nhất 6 ký tự
            this.address = address;
        } else {
            throw new IllegalArgumentException("Address must be at least 6 characters");
        }
    }

    // Phương thức toString() để hiển thị thông tin đối tượng
    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
