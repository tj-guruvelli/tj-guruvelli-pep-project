package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

/**
 * Data Access Object for Account table operations
 */
public class AccountDAO {

    /**
     * Creates a new account in the database
     * @param account The account to create
     * @return The created account with account_id if successful, null otherwise
     */
    public Account createAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();
            
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                account.setAccount_id(generatedId);
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves an account by username
     * @param username The username to search for
     * @return The account if found, null otherwise
     */
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setString(1, username);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                int accountId = resultSet.getInt("account_id");
                String retrievedUsername = resultSet.getString("username");
                String password = resultSet.getString("password");
                
                return new Account(accountId, retrievedUsername, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves an account by its ID
     * @param accountId The ID of the account to retrieve
     * @return The account if found, null otherwise
     */
    public Account getAccountById(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, accountId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                int retrievedAccountId = resultSet.getInt("account_id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                
                return new Account(retrievedAccountId, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}