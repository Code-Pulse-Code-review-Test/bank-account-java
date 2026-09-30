package com.example.bank;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

public class Statement {

    private final String accountNumber;
    private final String owner;
    private final List<Transaction> history;
    private final YearMonth month;

    public Statement(String accountNumber, String owner, List<Transaction> history, YearMonth month) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.history = List.copyOf(history);
        this.month = month;
    }

    public static Statement forMonth(Account account, YearMonth month) {
        return new Statement(account.getNumber(), account.getOwner(), account.getHistory(), month);
    }

    public long openingBalance() {
        return balanceBefore(month.atDay(1));
    }

    public long closingBalance() {
        return balanceBefore(month.plusMonths(1).atDay(1));
    }

    public List<Transaction> transactions() {
        return history.stream().filter(t -> YearMonth.from(t.date()).equals(month)).toList();
    }

    public String render() {
        StringBuilder out = new StringBuilder();
        out.append("Statement for ").append(accountNumber).append(" (").append(owner).append(")\n");
        out.append("Month: ").append(month).append('\n');
        out.append(String.format(Locale.ROOT, "Opening balance: %,d%n", openingBalance()));
        for (Transaction t : transactions()) {
            out.append(String.format(Locale.ROOT, "%s  %-10s %,12d%n", t.date(), t.type(), signedAmount(t)));
        }
        out.append(String.format(Locale.ROOT, "Closing balance: %,d%n", closingBalance()));
        return out.toString();
    }

    private long balanceBefore(LocalDate day) {
        long balance = 0;
        for (Transaction t : history) {
            if (t.date().isBefore(day)) {
                balance += signedAmount(t);
            }
        }
        return balance;
    }

    private static long signedAmount(Transaction t) {
        return t.type() == Transaction.Type.WITHDRAWAL ? -t.amount() : t.amount();
    }
}
