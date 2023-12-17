package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

/*
 * TODO:
 * Create new user
 * Check username and password for login
 */

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService()
    {
        this.accountDAO = new AccountDAO();
    }

    public List<Account> getAllAccounts()
    {
        return accountDAO.getAllAccounts();
    }

    public Account addAccount(Account toAdd)
    {
        return accountDAO.addAccount(toAdd);
    }

    public Account checkValidUser(Account toCheck)
    {
        return accountDAO.checkValidUser(toCheck);
    }
}
