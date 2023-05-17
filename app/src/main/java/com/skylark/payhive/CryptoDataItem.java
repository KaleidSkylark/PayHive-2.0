package com.skylark.payhive;

public class CryptoDataItem {
    private final String name;
    private final String symbol;
    private final String balance;
    private final String price;
    private final int imageResId;

    public CryptoDataItem(String name, String symbol, String balance, String price, int imageResId) {
        this.name = name;
        this.symbol = symbol;
        this.balance = balance;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getBalance() {
        return balance;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }
}