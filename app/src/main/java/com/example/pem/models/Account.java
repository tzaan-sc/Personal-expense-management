package com.example.pem.models;

public class Account {
    private String id;
    private String name;
    private String type;       // "Thẻ tín dụng", "Tiền mặt", etc.
    private double balance;
    private double debt;
    private String bookId;

    public Account() {}

    public Account(String id, String name, String type, double balance, String bookId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.bookId = bookId;
        this.debt = 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public double getDebt() { return debt; }
    public void setDebt(double debt) { this.debt = debt; }
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getFormattedBalance() {
        if (balance == 0) return "0đ";
        long val = (long) balance;
        if (val >= 1000) return (val / 1000) + "k";
        return val + "đ";
    }
}
