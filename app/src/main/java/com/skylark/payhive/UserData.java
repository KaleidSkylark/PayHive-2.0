package com.skylark.payhive;

public class UserData {
    private String fullName;
    private String userName;
    private String email;
    private long phoneNumber;
    private String password;
    private long walletTotalBalance;

    // Default constructor for Firebase database
    public UserData() {}

    public UserData(String fullName, String email, long phoneNumber, String password) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getWalletTotalBalance() {
        return walletTotalBalance;
    }

    public void setWalletTotalBalance(long walletTotalBalance) {
        this.walletTotalBalance = walletTotalBalance;
    }
}