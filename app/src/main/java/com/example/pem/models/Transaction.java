package com.example.pem.models;

import java.util.Date;

public class Transaction {
    private long id;
    private String type;       // "chi" or "thu"
    private String category;   // "Ăn", "Giải trí", etc.
    private double amount;
    private String note;
    private Date date;
    private String accountId;
    private String bookId;

    public Transaction() {}

    public Transaction(String type, String category, double amount, String note, Date date, String accountId, String bookId) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.accountId = accountId;
        this.bookId = bookId;
    }

    // Getters & Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getFormattedAmount() {
        long val = (long) amount;
        if (val >= 1000) {
            return (type.equals("chi") ? "-" : "+") + (val / 1000) + "k";
        }
        return (type.equals("chi") ? "-" : "+") + val + "đ";
    }
}
