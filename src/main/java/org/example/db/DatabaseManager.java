package org.example.db;

import org.example.module.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    public Connection getDbConnection() {
        String url = "jdbc:sqlite:sbs.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            logger.info("Connection to db established!");
        } catch (SQLException e) {
            logger.error("Connection to db failed!", e);
            throw new RuntimeException(e.getMessage());
        }
        return conn;
    }

    public void closeDbConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                logger.info("Connection closed!");
            }
        } catch (SQLException e) {
            logger.error("Error closing connection!");
            throw new RuntimeException(e.getMessage());
        }
    }

    public void createNewDbTable(Connection connection) {

        String query = "create table if not exists BANK("
                + "ACCOUNT_ID varchar(225) NOT NULL, "
                + "BALANCE varchar(225) NOT NULL, "
                + "PRIMARY KEY (ACCOUNT_ID) )";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            preparedStatement.close();

            logger.info("Table created successfully.");
        } catch (Exception e) {
            logger.error("Failed to create a table!");
        }
    }

    public void insertDataIntoDb(Connection connection, Account account) {
        String query = "insert into BANK (account_id, balance) values (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(account.getNumber()));
            preparedStatement.setBigDecimal(2, account.getBalance());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Failed to insert data: account number {}, balance {}!", account.getNumber(), account.getBalance());
            throw new RuntimeException(e);
        }
    }

    public void updateDataInDb(Connection connection, UUID accountNumber, BigDecimal balance) {
        String query = "update BANK set balance = ? where account_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBigDecimal(1, balance);
            preparedStatement.setString(2, accountNumber.toString());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Failed to update data: account number {}, balance {}!", accountNumber, balance);
            throw new RuntimeException(e);
        }
    }

    public void retrieveDataFromDb(Connection connection) {
        String query = "select * from BANK";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("account_id");
                String balance = resultSet.getString("balance");
                logger.info("Account_id: {},  Balance: {}", id, balance);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.error("Failed to retrieve data from db!");
            throw new RuntimeException(e);
        }
    }
}
