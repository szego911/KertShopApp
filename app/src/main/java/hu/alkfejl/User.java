package hu.alkfejl;

public class User {
    private String userName;
    private String email;
    private String address;
    private String password;

    public User() {
        this.userName = "defaultUserName";
        this.email = "defaultEmail";
        this.address = "defaultAddress";
        this.password = "defaultPassword";
    }

    public User(String userName, String email, String address, String password) {
        this.userName = userName;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
