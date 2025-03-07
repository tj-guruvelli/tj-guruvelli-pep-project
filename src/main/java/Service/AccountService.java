package Service;

import DAO.AccountDAO;
import Model.Account;

/**
 * Service class for Account-related business logic.
 * Acts as an intermediary between controllers and DAOs.
 */
public class AccountService {
    
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    /**
     * Registers a new account if the input is valid
     * @param account The account to register
     * @return The registered account with account_id if successful, null otherwise
     */
    public Account registerAccount(Account account) {
        // Validate username is not blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return null;
        }

        // Validate password is at least 4 characters
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }

        // Check if username already exists
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }

        // If all validations pass, register the account
        return accountDAO.createAccount(account);
    }

    /**
     * Attempts to login with the provided credentials
     * @param account The account containing login credentials
     * @return The account with account_id if login is successful, null otherwise
     */
    public Account login(Account account) {
        // Get account by username
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());

        // Check if account exists and password matches
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }

        return null;
    }

    /**
     * Gets an account by its ID
     * @param accountId The ID of the account to retrieve
     * @return The account if found, null otherwise
     */
    public Account getAccountById(int accountId) {
        return accountDAO.getAccountById(accountId);
    }
}