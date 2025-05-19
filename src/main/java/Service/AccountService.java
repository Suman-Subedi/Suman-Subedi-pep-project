package Service;

import DAO.AccountDAO;
import Model.Account;

/*
 * Service Layer Class for Handling account-realted business logic
 * Acts as an intermediary between controller and the AccountDAO class
 */

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    // Registers a new account after validating the input
    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() <= 4) {
            return null;
        }
        
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }
        
        return accountDAO.insertAccount(account);
    }

    // Authenticates a user's login credentials.

    public Account login(Account account) {
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());

        //verfies if the account exist and the password matches
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }
        return null;
    }

    // Retrieves an account by its unique ID.

    public Account getAccountById(int accountId){
        return accountDAO.getAccountById(accountId);
    }
}
