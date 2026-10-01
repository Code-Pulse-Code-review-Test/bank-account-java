package com.example.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    void depositAndWithdraw() {
        Account account = new Account("A1", "Test");
        account.deposit(1000);
        account.withdraw(400);
        assertEquals(600, account.getBalance());
        assertEquals(2, account.getHistory().size());
    }

    @Test
    void cannotOverdraw() {
        Account account = new Account("A1", "Test");
        account.deposit(100);
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(101));
    }

    @Test
    void rejectsNegativeAmounts() {
        Account account = new Account("A1", "Test");
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-5));
    }

    @Test
    void transferMovesMoney() {
        Bank bank = new Bank();
        Account a = bank.open("A");
        Account b = bank.open("B");
        a.deposit(500);
        bank.transfer(a.getNumber(), b.getNumber(), 200);
        assertEquals(300, a.getBalance());
        assertEquals(200, b.getBalance());
    }

    @Test
    void stopsWithdrawalsOverTheDailyLimit() {
        Account account = new Account("A1", "Test");
        account.deposit(200_000);
        account.withdraw(80_000);
        assertThrows(IllegalStateException.class, () -> account.withdraw(30_000));
        account.withdraw(20_000);
        assertEquals(100_000, account.getBalance());
    }
}
