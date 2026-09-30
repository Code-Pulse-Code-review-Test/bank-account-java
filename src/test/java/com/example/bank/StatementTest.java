package com.example.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.Test;

class StatementTest {

    private final List<Transaction> history = List.of(
            new Transaction(Transaction.Type.DEPOSIT, 10_000, LocalDate.of(2026, 8, 20)),
            new Transaction(Transaction.Type.WITHDRAWAL, 2_500, LocalDate.of(2026, 9, 3)),
            new Transaction(Transaction.Type.DEPOSIT, 4_000, LocalDate.of(2026, 9, 15)),
            new Transaction(Transaction.Type.INTEREST, 57, LocalDate.of(2026, 9, 30)),
            new Transaction(Transaction.Type.WITHDRAWAL, 1_000, LocalDate.of(2026, 10, 1)));

    private final Statement september = new Statement("ACC1000", "Nimal", history, YearMonth.of(2026, 9));

    @Test
    void openingBalanceCountsEarlierMonths() {
        assertEquals(10_000, september.openingBalance());
    }

    @Test
    void closingBalanceStopsAtTheEndOfTheMonth() {
        assertEquals(11_557, september.closingBalance());
    }

    @Test
    void listsOnlyThatMonthsTransactions() {
        assertEquals(3, september.transactions().size());
    }

    @Test
    void rendersBalancesAndOneLinePerTransaction() {
        String text = september.render();
        assertTrue(text.contains("Opening balance: 10,000"));
        assertTrue(text.contains("Closing balance: 11,557"));
        assertEquals(7, text.lines().count());
    }

    @Test
    void forMonthUsesTheAccountHistory() {
        Account account = new Account("A1", "Test");
        account.deposit(500);
        Statement current = Statement.forMonth(account, YearMonth.now());
        assertEquals(0, current.openingBalance());
        assertEquals(500, current.closingBalance());
    }
}
