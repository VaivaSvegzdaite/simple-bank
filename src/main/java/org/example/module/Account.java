package org.example.module;

import org.example.exceptions.InsufficientFundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {

    private static final Logger logger = LoggerFactory.getLogger(Account.class);

    private UUID number;
    private BigDecimal balance;

    public Account() {
        this.balance = BigDecimal.ZERO;
        this.number = UUID.randomUUID();
    }

    public BigDecimal getBalance() {
        logger.info("Account balance is {}", balance);
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    public UUID getNumber() {
        return number;
    }

    public void setNumber(UUID number) {
        this.number = number;
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(amount);
            logger.info("Successful account deposit by {} amount. Total balance: {} ", amount, balance);
        } else {
            logger.error("Failed to deposit account by negative amount {} !", amount);
        }
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0
                && balance.subtract(amount).compareTo(BigDecimal.ZERO) >= 0) {
            balance = balance.subtract(amount);
            logger.info("Successful account withdraw by: {} amount. Total balance: {}", amount, balance);
        } else {
            logger.error("Failed to withdraw from account amount of {}! Insufficient funds!", amount);
        }
    }

    public synchronized void transfer(Account destination, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount to transfer must be positive!");
        }
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds!");
        }
        balance = balance.subtract(amount);
        destination.balance = destination.balance.add(amount);
        logger.info("Transfer of {} amount was successful!", amount);
    }

}
