package com.example.bank;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Bank {

    private final Map<String, Account> accounts = new HashMap<>();
    private int nextNumber = 1000;

    public Account open(String owner) {
        String number = "ACC" + nextNumber++;
        Account account = new Account(number, owner);
        accounts.put(number, account);
        return account;
    }

    public Optional<Account> find(String number) {
        return Optional.ofNullable(accounts.get(number));
    }

    public void transfer(String from, String to, long amount) {
        Account source = get(from);
        Account target = get(to);
        source.withdraw(amount);
        target.deposit(amount);
    }

    public void applyMonthlyInterest(double monthlyRate) {
        accounts.values().forEach(account -> account.addInterest(monthlyRate));
    }

    public long totalDeposits() {
        return accounts.values().stream().mapToLong(Account::getBalance).sum();
    }

    private Account get(String number) {
        return find(number).orElseThrow(() -> new IllegalArgumentException("Unknown account " + number));
    }
}
