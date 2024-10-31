package fpl.md37.genz_fashion.models;

import java.util.HashMap;

public class Client {
    String password;
    String name;
    String email;
    String phone;
    String avatar;
    String address;

    public HashMap<String, Object> convertHashMap() {
        HashMap<String, Object> Client = new HashMap<>();
        Client.put("name", name);
        Client.put("email", email);
        Client.put("password", password);
        Client.put("phone", phone);
        Client.put("avatar", avatar);
        Client.put("address", address);

        return Client;
    }

    public Client() {
    }

    public Client(String address, String avatar, String phone, String email, String name, String password) {
        this.address = address;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
        this.address = address;
    }
}
