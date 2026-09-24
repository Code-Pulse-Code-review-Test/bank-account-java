package com.example.bank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {

    private final String number;
    private final String owner;
    private long balance;
    private final List<Transaction> history = new ArrayList<>();

    public Account(String number, String owner) {
        this.number = number;
        this.owner = owner;
    }

    public void deposit(long amount) {
        requirePositive(amount);
        balance += amount;
        history.add(new Transaction(Transaction.Type.DEPOSIT, amount, LocalDate.now()));
    }

    public void withdraw(long amount) {
        requirePositive(amount);
        if (amount > balance) {
            throw new InsufficientFundsException(number, amount, balance);
        }
        balance -= amount;
        history.add(new Transaction(Transaction.Type.WITHDRAWAL, amount, LocalDate.now()));
    }

    public void addInterest(double monthlyRate) {
        long interest = Math.round(balance * monthlyRate);
        if (interest > 0) {
            balance += interest;
            history.add(new Transaction(Transaction.Type.INTEREST, interest, LocalDate.now()));
        }
    }

    public String getNumber() {
        return number;
    }

    public String getOwner() {
        return owner;
    }

    public long getBalance() {
        return balance;
    }

    public List<Transaction> getHistory() {
        return Collections.unmodifiableList(history);
    }

    private static void requirePositive(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
