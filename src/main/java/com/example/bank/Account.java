package com.example.bank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {

    public static final long DAILY_WITHDRAWAL_LIMIT = 100_000;

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
        LocalDate today = LocalDate.now();
        if (withdrawnOn(today) + amount > DAILY_WITHDRAWAL_LIMIT) {
            throw new IllegalStateException("Daily withdrawal limit reached for " + number);
        }
        balance -= amount;
        history.add(new Transaction(Transaction.Type.WITHDRAWAL, amount, today));
    }

    public long withdrawnOn(LocalDate day) {
        return history.stream()
                .filter(t -> t.type() == Transaction.Type.WITHDRAWAL && t.date().equals(day))
                .mapToLong(Transaction::amount)
                .sum();
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
