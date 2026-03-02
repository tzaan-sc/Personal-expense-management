package com.example.pem.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataManager {
    private static DataManager instance;

    private List<Book> books = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    private DataManager() {
        // Initialize with sample data
        books.add(new Book("so1", "Sổ 1", 0));
        books.add(new Book("so2", "Sổ 2", 0));

        accounts.add(new Account("acc1", "sổ 1", "Thẻ tín dụng", 0, "so1"));
        accounts.add(new Account("acc2", "sổ 2", "Tiền mặt", 0, "so2"));

        // Sample transactions
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.FEBRUARY, 27);
        transactions.add(new Transaction("chi", "Ăn", 1000, "", cal.getTime(), "acc1", "so1"));
        transactions.add(new Transaction("thu", "Tiền công", 20000, "", cal.getTime(), "acc1", "so1"));
        transactions.add(new Transaction("chi", "Xe", 4000, "", cal.getTime(), "acc1", "so1"));
    }

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    public List<Book> getBooks() { return books; }
    public List<Account> getAccounts() { return accounts; }
    public List<Transaction> getTransactions() { return transactions; }

    public void addTransaction(Transaction t) { transactions.add(t); }
    public void addAccount(Account a) { accounts.add(a); }
    public void addBook(Book b) { books.add(b); }

    public List<Transaction> getTransactionsByMonth(int month, int year) {
        List<Transaction> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (Transaction t : transactions) {
            cal.setTime(t.getDate());
            if (cal.get(Calendar.MONTH) == month - 1 && cal.get(Calendar.YEAR) == year) {
                result.add(t);
            }
        }
        return result;
    }

    public double getTotalThuByMonth(int month, int year) {
        double total = 0;
        for (Transaction t : getTransactionsByMonth(month, year)) {
            if ("thu".equals(t.getType())) total += t.getAmount();
        }
        return total;
    }

    public double getTotalChiByMonth(int month, int year) {
        double total = 0;
        for (Transaction t : getTransactionsByMonth(month, year)) {
            if ("chi".equals(t.getType())) total += t.getAmount();
        }
        return total;
    }

    public double getTotalAssets() {
        double total = 0;
        for (Account a : accounts) total += a.getBalance();
        return total;
    }

    public double getTotalDebt() {
        double total = 0;
        for (Account a : accounts) total += a.getDebt();
        return total;
    }
}
