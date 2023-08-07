package org.example;

import org.example.db.DatabaseManager;
import org.example.module.Account;

import java.math.BigDecimal;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getDbConnection();

        try {
            databaseManager.createNewDbTable(connection);
            for (int i = 0; i < 5; i++) {
                Account account = new Account();
                databaseManager.insertDataIntoDb(connection, account);
                account.deposit(new BigDecimal(100 * (i + 1)));
                databaseManager.updateDataInDb(connection, account.getNumber(), account.getBalance());
                account.withdraw(new BigDecimal(10 * (i + 1)));
                databaseManager.updateDataInDb(connection, account.getNumber(), account.getBalance());
            }
            databaseManager.retrieveDataFromDb(connection);
        } finally {
            databaseManager.closeDbConnection(connection);
        }

    }
}