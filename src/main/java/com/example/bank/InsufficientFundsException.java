package com.example.bank;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String accountNumber, long requested, long available) {
        super("Account " + accountNumber + " has " + available + " but " + requested + " was requested");
    }
}
