package com.example.bank;

import java.time.YearMonth;

public class Main {

    public static void main(String[] args) {
        Bank bank = new Bank();
        Account nimal = bank.open("Nimal");
        Account saman = bank.open("Saman");

        nimal.deposit(50_000);
        saman.deposit(10_000);
        bank.transfer(nimal.getNumber(), saman.getNumber(), 7_500);
        bank.applyMonthlyInterest(0.005);

        System.out.println(nimal.getOwner() + ": " + nimal.getBalance());
        System.out.println(saman.getOwner() + ": " + saman.getBalance());
        System.out.println("Total: " + bank.totalDeposits());

        System.out.println();
        System.out.print(Statement.forMonth(nimal, YearMonth.now()).render());
    }
}
