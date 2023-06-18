package com.skylark.payhive;

public class Transaction {
    private String id;
    private String type;
    private long amount;

    public Transaction() {
    }

    public Transaction(String id, String type, long amount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
    }

    // Add getter and setter methods for id, type, and amount

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}