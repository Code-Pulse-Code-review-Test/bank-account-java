package com.example.bank;

import java.time.LocalDate;

public record Transaction(Type type, long amount, LocalDate date) {

    public enum Type {
        DEPOSIT,
        WITHDRAWAL,
        INTEREST
    }
}
