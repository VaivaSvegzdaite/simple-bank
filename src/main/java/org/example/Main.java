package org.example;

import org.example.module.Account;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        //create account
        Account account = new Account();
        account.getBalance();

        //create second account
        Account destination = new Account();

        //deposit account
        account.deposit(new BigDecimal(150));

        //deposit account negative funds
        account.deposit(new BigDecimal(-50));

        //withdraw insufficient fund
        account.withdraw(new BigDecimal(200));

        //withdraw successful
        account.withdraw(new BigDecimal(20));

        //get balance
        account.getBalance();

        //transfer to destination account and check balance
        account.transfer(destination, new BigDecimal(50));
        destination.getBalance();
        account.getBalance();

    }
}